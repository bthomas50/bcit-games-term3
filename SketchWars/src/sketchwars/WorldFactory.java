package sketchwars;

import java.util.ArrayList;
import sketchwars.physics.*;
import sketchwars.scenes.*;
import sketchwars.character.Character;
import sketchwars.character.projectiles.*;
import sketchwars.character.weapon.*;
import sketchwars.map.*;
import sketchwars.exceptions.SceneManagerException;
import sketchwars.graphics.Texture;
import sketchwars.character.Team;

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
            createCharacter(wep, new BoundingBox(-100, 0, 50, 100));
            createCharacter(wep, new BoundingBox(-100, -150, 50, -50));
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
        AbstractMap map = new TestMap();
        map.init();
        PixelCollider mapCollider = new PixelCollider(BitMaskFactory.createRectangle(1800, 800));
        mapCollider.setPosition(Vectors.create(-900, -1024));
        mapCollider.setElasticity(1.0f);
        map.setCollider(mapCollider);

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
    
    private void createCharacter(AbstractWeapon wep, BoundingBox box)
    {
        Texture texture = new Texture();
        texture.loadTexture("content/char/char1.png");
        Character character = new Character(texture);
        ArrayList<Character> charList = new ArrayList<>();
        Team team;
        
        character.setWeapon(wep);
        PixelCollider charCollider = new PixelCollider(BitMaskFactory.createRectangle(box));
        charCollider.setMass(10);
        charCollider.setElasticity(1.0f);
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