package edu.columbia.threescompany.players;

import java.util.List;

import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.gameobjects.GameObject;

/**
 * Players call notifyAll when they have an event for the game to handle.
 */
public abstract class Player {
	protected String _name;
	
	public Player(String name) {
		this._name = name;
	}
	
	/**
	 * @return null if the player has no move or a GameMove if they do.
	 */
	public abstract GameMove getMove();
	
	public abstract void processMoves(GameMove move);
	
	public abstract void updateState(List<GameObject> boardState);

	public abstract void disableInput();

	public abstract void enableInput();
}