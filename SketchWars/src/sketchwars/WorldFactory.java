package sketchwars;

import sketchwars.physics.*;
import sketchwars.scenes.*;
import sketchwars.character.Character;
import sketchwars.character.projectiles.*;
import sketchwars.character.weapon.*;
import sketchwars.map.*;
import sketchwars.exceptions.SceneManagerException;

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
            createCharacter(wep);
            createProjectile();
        } catch (SceneManagerException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void createGameScene() throws SceneManagerException
    {
        scene = new GameScene();
        scene.init();
        sceneManager.addScene(SketchWars.Scenes.GAME, scene);
        sceneManager.setCurrentScene(SketchWars.Scenes.GAME);
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

    private void createCharacter(AbstractWeapon wep)
    {
        Character character = new Character();
        character.init();
        character.setWeapon(wep);
        PixelCollider charCollider = new PixelCollider(BitMaskFactory.createRectangle(new BoundingBox(-50, 0, 50, 100)));
        charCollider.setMass(10);
        charCollider.setElasticity(1.0f);
        character.setCollider(charCollider);

        physics.addCollider(charCollider);
        scene.AddDrwableObject(character);
        world.addCharacter(character);
    }
    private AbstractWeapon createWeapon()
    {
        AbstractWeapon weapon = new TestWeapon();
        weapon.init();
        return weapon;
    }
    private void createProjectile()
    {        
        AbstractProjectile projectile = new TestBullet();
        projectile.init();

        PixelCollider projCollider = new PixelCollider(BitMaskFactory.createCircle(32.0));
        projCollider.setPosition(Vectors.create(100, 300));
        projCollider.setMass(1);
        projCollider.setElasticity(1.0f);

        projectile.setCollider(projCollider);

        physics.addCollider(projCollider);
        scene.AddDrwableObject(projectile);
        world.addGameObject(projectile);
    }


}