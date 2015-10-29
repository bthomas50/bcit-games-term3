package sketchwars.game;

import sketchwars.physics.*;
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
import sketchwars.HUD.HealthBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import org.joml.Matrix3d;
import sketchwars.animation.Animation;
import sketchwars.animation.AnimationSet;
import sketchwars.animation.CharacterAnimations;
import sketchwars.physics.colliders.CharacterCollider;

public class SketchWarsWorldFactory
{
    private static final float CHARACTER_SCALE = 0.07f;
    
    private static final int NUM_TEAMS = 2;
    private static final int CHARS_PER_TEAM = 3;

    private final SketchWarsWorld world;
    private final Physics physics;
    private final SceneManager<Scenes> sceneManager;
    private Scene<GameLayers> gameScene;

    public SketchWarsWorldFactory(SketchWarsWorld world, Physics physics, SceneManager<Scenes> sceneManager)
    {
        this.world = world;
        this.physics = physics;
        this.sceneManager = sceneManager;
    }

    public void startGame()
    {
        try {
            preloadTextures();
            createGameScene();
            createMap();
            createTeams();
            SoundPlayer.playMusic(0, true, -15);
        } catch (SceneManagerException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
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
        Texture mapBGTexture = Texture.loadTexture("content/map/clouds.png", false);
        Texture mapFGTexture = Texture.loadTexture("content/map/map.png", false);
        //it'll be empty if an error occurs when loading the map texture.
        BitMask mapImageMask = BitMaskFactory.createEmpty();
        try 
        {
            BufferedImage mapImage = Texture.loadImageFile("content/map/map.png");
            mapImageMask = BitMaskFactory.createFromImageAlpha(mapImage, physics.getBounds());
            
            MapCollider mapCollider = new MapCollider(mapImageMask);
            mapCollider.setElasticity(0.5f);
            TestMap map = new TestMap(mapCollider, mapBGTexture, mapFGTexture, mapImage);
            mapCollider.attachGameObject(map);
            
            try {
                gameScene.getLayer(GameLayers.MAP).addDrawableObject(map);
            } catch (SceneException ex) {
                System.err.println(ex.getMessage());
            }

            physics.addCollider(mapCollider);
            world.setMap(map);
        }
        catch(IOException e) 
        {
            System.err.println(e);
        }
    }

    private void createTeams() 
    {
        Random rng = new Random();
        
        for(int t = 0; t < NUM_TEAMS; t++)
        {
            Team team = createTeam(rng, t);
            world.addTeam(team);
        }
    }

    private Team createTeam(Random rng, int teamNum)
    {
        ArrayList<SketchCharacter> characters = new ArrayList<>(CHARS_PER_TEAM);
        HashMap<WeaponTypes, AbstractWeapon> weapons = new HashMap<>();
        HealthBar charHealthBar, teamHealthBar;
        
        try
        {
            weapons = WeaponFactory.createDefaultWeaponSet(new ProjectileFactory(world, physics, gameScene), world);
        }
        catch(SceneException ex)
        {
            System.err.println(ex);
        }
        for(int c = 0; c < CHARS_PER_TEAM; c++)
        {
            //dont do randomness for now to simplify networking
            //random between -900, 900
            //double r = (rng.nextDouble() - 0.5) * 1800.0;
            double r = ((double)c * 1500.0 / CHARS_PER_TEAM) - 800.0 + teamNum * 100;
            SketchCharacter character = createCharacter(Vectors.create(r, 800.0), rng);
            character.setWeapon(weapons.get(WeaponTypes.MELEE_WEAPON));
            //character.setMaxHealth(100);
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
        return team;
    }

    private SketchCharacter createCharacter(long vPosition, Random rng)
    {
        SketchCharacter character = new SketchCharacter();
        AnimationSet<CharacterAnimations> animationSet = createCharacterAnimations(rng);
        
        character.setAnimationSet(animationSet);
        
        Animation idle = animationSet.getAnimation(CharacterAnimations.IDLE);
        
        CharacterCollider charCollider;
        
        if (idle != null) {
            double ratio = idle.getSpriteHeight()/idle.getSpriteWidth();
            float screenAspectRatio = OpenGL.getAspectRatio();
            int widthP = (int)(CHARACTER_SCALE * 1024.0f);
            int heightP = (int)(widthP * ratio * screenAspectRatio);
            
            charCollider = new CharacterCollider(character, BitMaskFactory.createRectangle(widthP, heightP));
        } else {
            charCollider = new CharacterCollider(character, BitMaskFactory.createRectangle(100, 120));
        }
        
        charCollider.addCollisionListener(character);
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

    private AnimationSet<CharacterAnimations> createCharacterAnimations(Random rng) {
        AnimationSet<CharacterAnimations> animationSet = new AnimationSet<>();
        
        try {
            //Texture idleSpriteSheet = Texture.loadTexture("content/animation/characters/default/charSheet_idle.png", true);
            Texture idleSpriteSheet = Texture.loadTexture("content/animation/characters/stickman/idle.png", true);
            Animation idle = new Animation(idleSpriteSheet, 36, 6, 6, 5000, true);
            idle.start(rng.nextInt(750));
            animationSet.addAnimation(CharacterAnimations.IDLE, idle);
            //Texture jumpSpriteSheet = Texture.loadTexture("content/animation/characters/default/charSheet_jump.png", true);
            Texture jumpSpriteSheet = Texture.loadTexture("content/animation/characters/stickman/jump.png", true);
            Animation jump = new Animation(jumpSpriteSheet, 36, 6, 6, 1000, true);
            jump.start();
            animationSet.addAnimation(CharacterAnimations.JUMP, jump);
            
            //Texture walkLeftSpriteSheet = Texture.loadTexture("content/animation/characters/default/charSheet_walk_left.png", true);
            Texture walkLeftSpriteSheet = Texture.loadTexture("content/animation/characters/stickman/run.png", true);
            
            Animation walkLeft = new Animation(walkLeftSpriteSheet, 36, 6, 6, 2000, true);
            walkLeft.start();
            animationSet.addAnimation(CharacterAnimations.WALK_RIGHT, walkLeft);
            
            Animation walkRight = new Animation(walkLeftSpriteSheet, 36, 6, 6, 2000, true);
            Matrix3d trans = new Matrix3d();
            trans.scale(-1, 1, 1); //flip horizontal
            walkRight.setTransform(trans, false);
            walkRight.start();
            animationSet.addAnimation(CharacterAnimations.WALK_LEFT, walkRight);
            
            animationSet.setCurrentAnimation(CharacterAnimations.IDLE);
        } catch (AnimationException ex) {
            System.out.println(ex.getMessage());
        }
        
        return animationSet;
    }

    private void preloadTextures() {
        Texture.loadTexture("content/animation/explosions/explosion.png", false);
        HealthBar.loadTextures();
    }
}