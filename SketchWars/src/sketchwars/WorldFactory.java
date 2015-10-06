package sketchwars;

import sketchwars.physics.*;
import sketchwars.scenes.*;
import sketchwars.character.Character;
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

public class WorldFactory
{
    private static final int NUM_TEAMS = 2;
    private static final int CHARS_PER_TEAM = 3;

    private World world;
    private Physics physics;
    private SceneManager<SketchWars.Scenes> sceneManager;
    private GameScene scene;

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
            createProjectiles();
        } catch (SceneManagerException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void createGameScene() throws SceneManagerException
    {
        scene = (GameScene) sceneManager.getScene(SketchWars.Scenes.GAME);
    }

    private void createMap()
    {
        Texture mapBGTexture = new Texture();
        mapBGTexture.loadTexture("content/map/clouds.png");
        Texture mapFGTexture = new Texture();
        //it'll be empty if an error occurs when loading the map texture.
        BitMask mapImageMask = BitMaskFactory.createEmpty();
        try 
        {
            BufferedImage mapImage = mapFGTexture.loadTextureAndReturnImageData("content/map/map.png");
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
        scene.AddDrwableObject(map);
        world.setMap(map);
    }

    private void createTeams() 
    {
        Random rng = new Random();
        for(int t = 0; t < NUM_TEAMS; t++)
        {
            Team team = createTeam(rng);
            world.addTeam(team);
        }
    }

    private Team createTeam(Random rng)
    {
        ArrayList<Character> characters = new ArrayList<>(CHARS_PER_TEAM);
        HashMap<AbstractWeapon.WeaponEnum, AbstractWeapon> weapons = WeaponFactory.createDefaultWeaponSet();
        for(int c = 0; c < CHARS_PER_TEAM; c++)
        {
            //random between -900, 900
            double r = (rng.nextDouble() - 0.5) * 1800.0;
            Character character = createCharacter(Vectors.create(r, 800.0));
            characters.add(character);
        }
        return new Team(characters, weapons);
    }

    private Character createCharacter(long vPosition)
    {
        Texture texture = new Texture();
        texture.loadTexture("content/char/char1.png");
        Character character = new Character(texture);
        
        PixelCollider charCollider = new PixelCollider(BitMaskFactory.createCircle(64.0));
        charCollider.setPosition(vPosition);
        charCollider.setMass(10);
        charCollider.setElasticity(0.0f);
        character.setCollider(charCollider);
        
        physics.addCollider(charCollider);
        scene.AddDrwableObject(character);
        world.addCharacter(character);
        return character;
    }
    
    private void createProjectiles()
    {        
        Texture bulletTexture = new Texture();
        bulletTexture.loadTexture("content/char/weapons/grenade.png");
        AbstractProjectile projectile = new GrenadeProjectile(bulletTexture);
        
        PixelCollider projCollider = new PixelCollider(BitMaskFactory.createCircle(32.0));
        projCollider.setPosition(Vectors.create(100, 300));
        projCollider.setMass(projectile.getMass());
        projCollider.setElasticity(projectile.getElasticity());
        
        projectile.setCollider(projCollider);
        
        physics.addCollider(projCollider);
        scene.AddDrwableObject(projectile);
        world.addGameObject(projectile);
        
        AbstractProjectile projectile2 = new GrenadeProjectile(bulletTexture);
        
        PixelCollider projCollider2 = new PixelCollider(BitMaskFactory.createCircle(32.0));
        projCollider2.setPosition(Vectors.create(0, 300));
        projCollider2.setMass(projectile2.getMass());
        projCollider2.setElasticity(projectile2.getElasticity());

        projectile2.setCollider(projCollider2);

        physics.addCollider(projCollider2);
        scene.AddDrwableObject(projectile2);
        world.addGameObject(projectile2);
    }


}