/*
 * Setting to be set before game start, created by Host.
 */
package network;

import java.io.Serializable;
import sketchwars.character.weapon.WeaponSetTypes;
import sketchwars.game.GameModeType;
import sketchwars.game.Maps;

/**
 *
 * @author Salman Shaharyar
 */
public class GameSetting implements Serializable {
    
    private int maxPlayer;
    private int timePerTurn;
    private int characterPerTeam;
    private int characterHealth;
    private Maps mapSelected;
    private GameModeType gameModeSelected;
    private WeaponSetTypes weaponSetSelected;
    
    public static GameSetting createTutorialSettings() {
        return new GameSetting();
    }
        
    public GameSetting() 
    {
        maxPlayer = 2;
        timePerTurn = 15;
        characterPerTeam = 2;
        characterHealth = 200;
        mapSelected = Maps.NORMAL;
        gameModeSelected = GameModeType.Normal;
        weaponSetSelected = WeaponSetTypes.MIX;
    }
    
    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public int getTimePerTurn() {
        return timePerTurn;
    }

    public void setTimePerTurn(int timePerTurn) {
        this.timePerTurn = timePerTurn;
    }

    public int getCharacterPerTeam() {
        return characterPerTeam;
    }

    public void setCharacterPerTeam(int characterPerTeam) {
        this.characterPerTeam = characterPerTeam;
    }

    public int getCharacterHealth() {
        return characterHealth;
    }

    public void setCharacterHealth(int characterHealth) {
        this.characterHealth = characterHealth;
    }

    public Maps getMapSelected() {
        return mapSelected;
    }

    public void setMapSelected(Maps mapSelected) {
        this.mapSelected = mapSelected;
    }

    public GameModeType getGameModeSelected() {
        return gameModeSelected;
    }

    public void setGameModeSelected(GameModeType gameModeSelected) {
        this.gameModeSelected = gameModeSelected;
    }

    public WeaponSetTypes getWeaponSetSelected() {
        return weaponSetSelected;
    }

    public void setWeaponSetSelected(WeaponSetTypes weaponSetSelected) {
        this.weaponSetSelected = weaponSetSelected;
    }
    // sort order
}
