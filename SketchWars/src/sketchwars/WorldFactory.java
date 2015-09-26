package sketchwars;

import sketchwars.physics.Physics;
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
        scene.AddDrwableObject(map);
        world.setMap(map);
    }
    private void createCharacter(AbstractWeapon wep)
    {
        Character character = new Character();
        character.init();
        character.setWeapon(wep);
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
        projectile.setPosition(0.3, 0.3);
        scene.AddDrwableObject(projectile);
        world.addGameObject(projectile);
    }


}