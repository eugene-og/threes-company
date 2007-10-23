package edu.columbia.threescompany.client;

import java.util.List;

import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.gameobjects.GameObject;

public class LocalGameState {
	private List<GameObject> _gameObjects;
	private List<Player> _players;
	private Player _localPlayer;
	
	public List<GameObject> getObjects() {
		return _gameObjects;
	}

	public List<Player> getPlayers() {
		return _players;
	}
	
	public int getAP() {
		return _localPlayer.getAbilityPoints();
	}
}
