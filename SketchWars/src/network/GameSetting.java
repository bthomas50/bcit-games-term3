/*
 * Setting to be set before game start, created by Host.
 */
package network;

import sketchwars.character.weapon.WeaponSetTypes;
import sketchwars.game.GameModeType;

/**
 *
 * @author Salman Shaharyar
 */
public class GameSetting {
    
    private int maxPlayer;
    private int timePerTurn;
    private int characterPerTeam;
    private int characterHealth;
    private GameModeType mapSelected;
    private GameModeType gameModeSelected;
    private WeaponSetTypes weaponSetSelected;
    
    public GameSetting() 
    {
        maxPlayer = 2;
        timePerTurn = 15;
        characterPerTeam = 2;
        characterHealth = 200;
        mapSelected = GameModeType.Normal;
        gameModeSelected = GameModeType.Normal;
        weaponSetSelected = weaponSetSelected.MIX;
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

    public GameModeType getMapSelected() {
        return mapSelected;
    }

    public void setMapSelected(GameModeType mapSelected) {
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
