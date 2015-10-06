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
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WorldFactory
{
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
            AbstractWeapon wep = createWeapon();
            createCharacter(wep, Vectors.create(-300, 1024));
            createCharacter(wep, Vectors.create(300, 1024));
            createProjectile();
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

    /*private void createCharacter(AbstractWeapon wep)
    {
        Texture texture = new Texture();
        texture.loadTexture("content/char/char1.png");
        Character character = new Character(texture);
        
        character.setWeapon(wep);
        PixelCollider charCollider = new PixelCollider(BitMaskFactory.createRectangle(new BoundingBox(-100, 0, 50, 100)));
        charCollider.setMass(10);
        charCollider.setElasticity(1.0f);
        character.setCollider(charCollider);

        physics.addCollider(charCollider);
        scene.AddDrwableObject(character);
        world.addCharacter(character);
    }*/
    
    private void createCharacter(AbstractWeapon wep, long vPosition)
    {
        Texture texture = new Texture();
        texture.loadTexture("content/char/char1.png");
        Character character = new Character(texture);
        ArrayList<Character> charList = new ArrayList<>();
        Team team;
        
        character.setWeapon(wep);
        PixelCollider charCollider = new PixelCollider(BitMaskFactory.createCircle(50));
        charCollider.setPosition(vPosition);
        charCollider.setMass(10);
        charCollider.setElasticity(0.0f);
        character.setCollider(charCollider);
        charList.add(character);
        team = new Team(charList);
        
        physics.addCollider(charCollider);
        scene.AddDrwableObject(character);
        world.addTeam(team);
        world.addCharacter(character);
        
    }
    
    private AbstractWeapon createWeapon()
    {
        Texture texture = new Texture();
        texture.loadTexture("content/char/weapons/grenade.png");
        AbstractWeapon weapon = new GrenadeWeapon(texture, 0.1);
        return weapon;
    }
    private void createProjectile()
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