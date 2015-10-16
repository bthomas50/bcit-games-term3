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
import org.joml.Vector2d;
import sketchwars.animation.Animation;

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
            createGameScene();
            createMap();
            createTeams();
            createGameLogic();
            SoundPlayer.playMusic(0, true, -15);
            
            //test animation - will remove when addign character animations
            Texture anim = Texture.loadTexture("content/animation/characters/1/charSheet.png");
            Animation test = new Animation(anim, 12, 2000, true);
            test.setDimension(new Vector2d(100, 100));
            test.setPosition(new Vector2d(100, 5));
            test.start();
            gameScene.getLayer(GameLayers.CHARACTER).addAnimation(test);
            
            Texture anim2 = Texture.loadTexture("content/testSprite.png");
            Animation test2 = new Animation(anim2, 9, 2000, true);
            test2.setDimension(new Vector2d(100, 100));
            test2.setPosition(new Vector2d(5, 5));
            test2.start();
            gameScene.getLayer(GameLayers.CHARACTER).addAnimation(test2);
            
            
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
        Texture mapBGTexture = Texture.loadTexture("content/map/clouds.png");
        Texture mapFGTexture = Texture.loadTexture("content/map/map.png");
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
            SketchCharacter character = createCharacter(Vectors.create(r, 800.0));
            character.setWeapon(weapons.get(WeaponTypes.MELEE_WEAPON));
            characters.add(character);
        }
        return new Team(characters, weapons);
    }

    private SketchCharacter createCharacter(long vPosition)
    {
        Texture texture = Texture.loadTexture("content/char/char1.png");
        SketchCharacter character = new SketchCharacter(texture);
        
        PixelCollider charCollider = new PixelCollider(BitMaskFactory.createCircle(64.0));
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

}