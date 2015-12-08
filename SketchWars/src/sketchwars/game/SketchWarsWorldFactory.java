package sketchwars.game;

import sketchwars.physics.*;
import sketchwars.physics.effects.*;
import sketchwars.scenes.*;
import sketchwars.character.*;
import sketchwars.character.weapon.*;
import sketchwars.character.projectiles.*;
import sketchwars.map.*;
import sketchwars.exceptions.*;
import sketchwars.graphics.Texture;
import sketchwars.character.Team;
import sketchwars.sound.SoundPlayer;
import sketchwars.*;
import sketchwars.HUD.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import org.joml.Matrix3d;
import sketchwars.animation.Animation;
import sketchwars.animation.AnimationSet;
import sketchwars.animation.CharacterAnimations;
import sketchwars.graphics.Shader;
import sketchwars.physics.colliders.CharacterCollider;
import sketchwars.util.Converter;
import network.GameSetting;

public class SketchWarsWorldFactory
{
    private static final float CHARACTER_SCALE = 0.07f;
    
    private static final int NUM_TEAMS = 4;
    private static final int CHARS_PER_TEAM = 3;

    private final SketchWarsWorld world;
    private final Physics physics;
    private final SceneManager<Scenes> sceneManager;
    private Scene<GameLayers> gameScene;
    private String[] charSprites;
    private GameSetting gameSetting;
    private final Random rng;

    public SketchWarsWorldFactory(SketchWarsWorld world, Physics physics, SceneManager<Scenes> sceneManager, Random rng)
    {
        this.world = world;
        this.physics = physics;
        this.sceneManager = sceneManager;
        this.rng = rng;
        initCharSprites();
    }
    private void initCharSprites()
    {
        charSprites = new String[4];
        charSprites[2] = "monster";
        charSprites[1] = "stickman";
        charSprites[0] = "tank";
        charSprites[3] = "default";
    }
    public void startGame(GameSetting gameSetting)
    {
        this.gameSetting = gameSetting;
        initPhysics();
        try 
        {
            preloadTextures();
            createGameScene();
            setupCamera();
            createMap();
            createTeams();
            try
            {
                SoundPlayer.playMusic(0, true, -15);
            }
            catch(Exception e)
            {
                System.err.println("error starting music: " + e.getMessage());
            }
        } 
        catch (SceneManagerException ex) 
        {
            System.err.println(ex.getMessage());
        }
    }

    private void initPhysics()
    {
        physics.addEffect(new Gravity());
        physics.addEffect(new Wind(Vectors.create(-200, 0)));
    }

    private void createGameScene() throws SceneManagerException
    {
        gameScene = sceneManager.getScene(Scenes.GAME);
        
        Layer mapLayer = new Layer();
        Layer characterLayer = new Layer();
        Layer projectileLayer = new Layer();
        Layer hudLayer = new Layer();
        
        mapLayer.setZOrder(-1);
        characterLayer.setZOrder(0);
        projectileLayer.setZOrder(1);
        hudLayer.setZOrder(2);
        
        gameScene.addLayer(GameLayers.MAP, mapLayer);
        gameScene.addLayer(GameLayers.CHARACTER, characterLayer);
        gameScene.addLayer(GameLayers.PROJECTILE, projectileLayer);
        gameScene.addLayer(GameLayers.HUD, hudLayer);
    }

    
    private void createMap()
    {
        Texture mapBGTexture = Texture.loadTexture(gameSetting.getMapSelected().foregroundPath, false);
        Texture mapFGTexture = Texture.loadTexture(gameSetting.getMapSelected().path, true);
        Texture waterTexture = Texture.loadTexture(gameSetting.getMapSelected().waterPath, true);
               
        try 
        {
            Camera camera = gameScene.getCamera();
            
            BufferedImage mapImage = Texture.loadImageFile(gameSetting.getMapSelected().path);

            BitMask mapImageMask = BitMaskFactory.createFromImageAlpha(mapImage, physics.getBounds());
            
            MapCollider mapCollider = new MapCollider(mapImageMask);
            mapCollider.setElasticity(0.5f);
            mapCollider.setStaticFriction(1.0f);
            mapCollider.setDynamicFriction(1.0f);
            BasicMap map = new BasicMap(camera, mapCollider, mapBGTexture, mapFGTexture, mapImage);
            mapCollider.attachGameObject(map);

            try {
                gameScene.getLayer(GameLayers.MAP).addDrawableObject(map);
            } catch (SceneException ex) {
                System.err.println(ex);
            }

            physics.addCollider(mapCollider);
            world.setMap(map);
            
            int waterWidith = Converter.GraphicsToPhysicsX(camera.getWorldWidth());
            int waterHeight = Converter.GraphicsToPhysicsY(camera.getWorldHeight());
            
            int waterLeft = Converter.GraphicsToPhysicsX(camera.getWorldLeft());
            int waterTop = Converter.GraphicsToPhysicsY(camera.getWorldBottom()) - (int)(waterHeight* 0.9f);
            BoundingBox waterBB = new BoundingBox(waterTop, waterLeft, waterTop + waterHeight, waterLeft + waterWidith);
            
            Collider waterCollider = new PixelCollider(BitMaskFactory.createRectangle(waterBB), CollisionBehaviour.NOTIFY);
            waterCollider.setPosition(Vectors.create(waterLeft, waterTop));
            
            Shader waterShader = new Shader("content/shader/2d_water/vertShader.vert", "content/shader/2d_water/fragShader.frag");
            MapWater mapWater = new MapWater(waterTexture, waterShader, waterCollider);
            waterCollider.addCollisionListener(mapWater);
            map.setMapWater(mapWater);
            
           // mapWater.riseWaterLevel();
            physics.addCollider(waterCollider);
        } catch(IOException e) {
            System.err.println(e);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void createTeams() 
    {
        for(int t = 0; t < gameSetting.getMaxPlayer(); t++)
        {
            Team team = createTeam(t);
            world.addTeam(team);
        }
    }

    private Team createTeam(int teamNum)
    {
        ArrayList<SketchCharacter> characters = new ArrayList<>(gameSetting.getCharacterPerTeam());
        HashMap<WeaponTypes, AbstractWeapon> weapons = new HashMap<>();
        HealthBar charHealthBar, teamHealthBar;
        
        try
        {
            weapons = WeaponFactory.createWeaponSet(new ProjectileFactory(world, physics, gameScene, rng), world, physics, gameSetting.getWeaponSetSelected());
        }
        catch(SceneException ex)
        {
            System.err.println(ex);
        }
        for(int c = 0; c < gameSetting.getMaxPlayer(); c++)
        {
            //random between -900, 900
            double r = (rng.nextDouble() - 0.5) * SketchWars.PHYSICS_WIDTH * 0.9;
            SketchCharacter character = createCharacter(Vectors.create(r, 800.0), teamNum);

            character.setWeapon(WeaponTypes.getDefaultWeapon(weapons));
            charHealthBar = new HealthBar(HealthBar.lifeBars[teamNum*2], 
                                        HealthBar.lifeBars[teamNum*2+1], 
                                        Vectors.create(character.getPosX(), character.getPosY()));
            character.setHealthBar(charHealthBar);
            characters.add(character);
        }
        
        teamHealthBar = new HealthBar(HealthBar.lifeBars[teamNum*2],
                                      HealthBar.lifeBars[teamNum*2+1],
                                      Vectors.create(-1.0f, -0.7f + (-0.1f * teamNum)),
                                      -0.5f,
                                      0.05f);
        
        teamHealthBar.setMaxHealth(characters.get(0).getMaxHealth() * characters.size());
        teamHealthBar.setHealth(characters.get(0).getHealth() * characters.size());
        
        try{
            gameScene.getLayer(GameLayers.HUD).addDrawableObject(teamHealthBar);
        } catch (SceneException ex) {
            System.err.println(ex.getMessage());
        }
        
        Team team = new Team(characters, weapons);
        team.setHealthBar(teamHealthBar);
        world.addGameObject(teamHealthBar);
        
        return team;
    }

    
    private SketchCharacter createCharacter(long vPosition, int teamNum)
    {
        SketchCharacter character = new SketchCharacter(gameSetting.getCharacterHealth(), gameSetting.getCharacterHealth());
        AnimationSet<CharacterAnimations> animationSet = createCharacterAnimations(teamNum);
        
        character.setAnimationSet(animationSet);
        
        Animation idle = animationSet.getAnimation(CharacterAnimations.IDLE_RIGHT);
        
        CharacterCollider charCollider;
        
        if (idle != null) {
            double ratio = idle.getSpriteHeight()/idle.getSpriteWidth();
            int widthP;
            if(teamNum == 0)
                widthP = Converter.GraphicsToPhysicsX(0.15f);
            else
                widthP = Converter.GraphicsToPhysicsX(CHARACTER_SCALE);
            
            int heightP = (int)(widthP * ratio);
            
            charCollider = new CharacterCollider(character, BitMaskFactory.createRectangle(widthP, heightP));
        } else {
            charCollider = new CharacterCollider(character, BitMaskFactory.createRectangle(100, 120));
        }
        
        charCollider.setPosition(vPosition);
        charCollider.setMass(10);
        charCollider.setElasticity(0.0f);
        character.setCollider(charCollider);

        physics.addCollider(charCollider);
        
        try {
            gameScene.getLayer(GameLayers.CHARACTER).addDrawableObject(character);
        } catch (SceneException ex) {
            System.err.println(ex.getMessage());
        }
        
        world.addCharacter(character);
        return character;
    }

    private AnimationSet<CharacterAnimations> createCharacterAnimations(int teamNum) {
        AnimationSet<CharacterAnimations> animationSet = new AnimationSet<>();
        
        try {
            Matrix3d trans = new Matrix3d();
            trans.scale(-1, 1, 1); //flip horizontal
            
            Texture idleSpriteSheet = Texture.loadTexture("content/animation/characters/" + charSprites[teamNum] + "/idle.png", true);
            Animation idleRight = new Animation(idleSpriteSheet, 36, 6, 6, 5000, true);
            idleRight.start(rng.nextInt(750));
            animationSet.addAnimation(CharacterAnimations.IDLE_RIGHT, idleRight);
            
            Animation idleLeft = new Animation(idleSpriteSheet, 36, 6, 6, 5000, true);
            idleLeft.setTransform(trans, false);
            idleRight.start();
            animationSet.addAnimation(CharacterAnimations.IDLE_LEFT, idleLeft);
            
            Texture jumpSpriteSheet = Texture.loadTexture("content/animation/characters/" + charSprites[teamNum] + "/jump.png", true);
            Animation jumpRight = new Animation(jumpSpriteSheet, 36, 6, 6, 1000, true);
            jumpRight.start();
            animationSet.addAnimation(CharacterAnimations.JUMP_RIGHT, jumpRight);
            
            Animation jumpLeft = new Animation(jumpSpriteSheet, 36, 6, 6, 1000, true);
            jumpLeft.setTransform(trans, false);
            jumpLeft.start();
            animationSet.addAnimation(CharacterAnimations.JUMP_LEFT, jumpLeft);
            
            Texture walkSpriteSheet = Texture.loadTexture("content/animation/characters/" + charSprites[teamNum] + "/run.png", true);
            Animation walkRight = new Animation(walkSpriteSheet, 36, 6, 6, 2000, true);
            walkRight.start();
            animationSet.addAnimation(CharacterAnimations.WALK_RIGHT, walkRight);
            
            Animation walkLeft = new Animation(walkSpriteSheet, 36, 6, 6, 2000, true);
            walkLeft.setTransform(trans, false);
            walkLeft.start();
            animationSet.addAnimation(CharacterAnimations.WALK_LEFT, walkLeft);
            
            animationSet.setCurrentAnimation(CharacterAnimations.IDLE_RIGHT);
        } catch (AnimationException ex) {
            System.out.println(ex.getMessage());
        }
        
        return animationSet;
    }

    private void preloadTextures() {
        Texture.loadTexture("content/animation/explosions/explosion.png", false);
        HealthBar.loadTextures();
    }

    private void setupCamera() {
        Camera camera = gameScene.getCamera();
        camera.setNextCameraSize(SketchWars.OPENGL_WIDTH/2.0f, SketchWars.OPENGL_WIDTH/2.0f);
        
        world.addGameObject(camera);
        world.setCamera(camera);
    }
}