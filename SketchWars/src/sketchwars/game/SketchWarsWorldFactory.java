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

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import sketchwars.animation.Animation;
import sketchwars.animation.AnimationSet;
import sketchwars.animation.CharacterAnimations;

public class SketchWarsWorldFactory
{
    private static final int NUM_TEAMS = 2;
    private static final int CHARS_PER_TEAM = 3;

    private final SketchWarsWorld world;
    private final Physics physics;
    private final SceneManager<Scenes> sceneManager;
    private Scene<GameLayers> gameScene;
    private WeaponLogic weaponLogic;

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
            createGameLogic();
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
        
        mapLayer.setZOrder(-1);
        characterLayer.setZOrder(0);
        projectileLayer.setZOrder(1);
        
        gameScene.addLayer(GameLayers.MAP, mapLayer);
        gameScene.addLayer(GameLayers.CHARACTER, characterLayer);
        gameScene.addLayer(GameLayers.PROJECTILE, projectileLayer);
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
        }
        catch(IOException e) 
        {
            System.err.println(e);
        }

        TestMap map = new TestMap(mapFGTexture, mapBGTexture);

        PixelCollider mapCollider = new PixelCollider(mapImageMask);
        mapCollider.setElasticity(1.0f);

        try {
            gameScene.getLayer(GameLayers.MAP).addDrawableObject(map);
        } catch (SceneException ex) {
            System.err.println(ex.getMessage());
        }
        
        physics.addCollider(mapCollider);
        world.setMap(map);
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
        HashMap<WeaponTypes, AbstractWeapon> weapons = WeaponFactory.createDefaultWeaponSet(new ProjectileFactory(world, physics, gameScene));
        
        for(int c = 0; c < CHARS_PER_TEAM; c++)
        {
            //dont do randomness for now to simplify networking
            //random between -900, 900
            //double r = (rng.nextDouble() - 0.5) * 1800.0;
            double r = ((double)c * 1500.0 / CHARS_PER_TEAM) - 800.0 + teamNum * 100;
            SketchCharacter character = createCharacter(Vectors.create(r, 800.0), rng);
            character.setWeapon(weapons.get(WeaponTypes.MELEE_WEAPON));
            characters.add(character);
        }
        return new Team(characters, weapons);
    }

    private SketchCharacter createCharacter(long vPosition, Random rng)
    {
        SketchCharacter character = new SketchCharacter();
        AnimationSet<CharacterAnimations> animationSet = createCharacterAniamtions(rng);
        
        character.setAnimationSet(animationSet);
        PixelCollider charCollider = new PixelCollider(BitMaskFactory.createRectangle(100, 120));
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

    private void createGameLogic() {
        weaponLogic = new WeaponLogic(world, gameScene, physics);
        world.setWeaponLogic(weaponLogic);
    }

    private AnimationSet<CharacterAnimations> createCharacterAniamtions(Random rng) {
        AnimationSet<CharacterAnimations> animationSet = new AnimationSet<>();
        
        try {
            Texture idleSpriteSheet = Texture.loadTexture("content/animation/characters/default/charSheet.png", true);
            Animation idle = new Animation(idleSpriteSheet, 12, 12, 1, 5000, true);
            idle.start(rng.nextInt(750));
            animationSet.addAnimation(CharacterAnimations.IDLE, idle);
            
            Texture jumpSpriteSheet = Texture.loadTexture("content/animation/characters/default/charSheet_jump.png", true);
            Animation jump = new Animation(jumpSpriteSheet, 36, 6, 6, 1000, true);
            jump.start();
            animationSet.addAnimation(CharacterAnimations.JUMP, jump);
            
            Texture walkLeftSpriteSheet = Texture.loadTexture("content/animation/characters/default/charSheet_walkleft.png", true);
            Animation walkLeft = new Animation(walkLeftSpriteSheet, 12, 12, 1, 2000, true);
            walkLeft.start();
            animationSet.addAnimation(CharacterAnimations.WALK_LEFT, walkLeft);
            
            Texture walkRightSpriteSheet = Texture.loadTexture("content/animation/characters/default/charSheet_walkright.png", true);
            Animation walkRight = new Animation(walkRightSpriteSheet, 12, 12, 1, 2000, true);
            walkRight.start();
            animationSet.addAnimation(CharacterAnimations.WALK_RIGHT, walkRight);
            
            animationSet.setCurrentAnimation(CharacterAnimations.IDLE);
        } catch (AnimationException ex) {
            System.out.println(ex.getMessage());
        }
        
        return animationSet;
    }

    private void preloadTextures() {
        Texture.loadTexture("content/animation/explosions/explosion.png", false);
    }

}