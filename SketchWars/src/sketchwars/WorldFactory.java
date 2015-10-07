package sketchwars;

import sketchwars.physics.*;
import sketchwars.scenes.*;
import sketchwars.character.SketchCharacter;
import sketchwars.character.projectiles.*;
import sketchwars.character.weapon.*;
import sketchwars.map.*;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.Texture;
import sketchwars.character.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import sketchwars.sound.SoundPlayer;

public class WorldFactory
{
    private static final int NUM_TEAMS = 2;
    private static final int CHARS_PER_TEAM = 3;

    private World world;
    private Physics physics;
    private SceneManager<SketchWars.Scenes> sceneManager;
    private GameScene scene;
    private WeaponLogic weaponLogic;

    public WorldFactory(World world, Physics physics, SceneManager<SketchWars.Scenes> sceneManager)
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
        } catch (SceneManagerException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    private void createGameScene() throws SceneManagerException
    {
        scene = (GameScene) sceneManager.getScene(SketchWars.Scenes.GAME);
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

        physics.addCollider(mapCollider);
        scene.addDrawableObject(map);
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
        HashMap<WeaponEnum, AbstractWeapon> weapons = WeaponFactory.createDefaultWeaponSet(new ProjectileFactory(world, physics, scene));
        for(int c = 0; c < CHARS_PER_TEAM; c++)
        {
            //dont do randomness for now to simplify networking
            //random between -900, 900
            //double r = (rng.nextDouble() - 0.5) * 1800.0;
            double r = ((double)c * 1500.0 / CHARS_PER_TEAM) - 800.0 + teamNum * 100;
            SketchCharacter character = createCharacter(Vectors.create(r, 800.0));
            character.setWeapon(weapons.get(WeaponEnum.MELEE_WEAPON));
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
        scene.addDrawableObject(character);
        world.addCharacter(character);
        return character;
    }

    private void createGameLogic() {
        weaponLogic = new WeaponLogic(world, scene, physics);
        world.setWeaponLogic(weaponLogic);
        scene.addDrawableObject(weaponLogic);
    }

}