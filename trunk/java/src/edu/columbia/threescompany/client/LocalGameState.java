package edu.columbia.threescompany.client;

import java.util.List;

import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.gameobjects.GameObject;

public class LocalGameState {
	private List<GameObject> _gameObjects;
	private List<Player> _players;
	private List<Player> _localPlayers;
	private Player _activePlayer;
	
	public List<GameObject> getObjects() {
		return _gameObjects;
	}

	public List<Player> getPlayers() {
		return _players;
	}
	
	public int getAP() {
		return _activePlayer.getAbilityPoints();
	}

	public Player getActivePlayer() {
		return _activePlayer;
	}
	
	public void updateActivePlayer(int id) {
		for (Player player : _players) {
			if (id == player.getId()) {
				_activePlayer = player;
				return;
			}
		}
		throw new RuntimeException("Can't set active player to " + id +
								   ", that player doesn't exist!");
	}

	public boolean isLocalPlayer(int somePlayer) {
		for (Player player : _localPlayers)
			if (player.getId() == somePlayer) return true;
		return false;
	}
}
