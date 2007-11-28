package edu.columbia.threescompany.game;

import java.io.Serializable;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.gameobjects.GameObject;

public abstract class GameMoveComponent implements Serializable {
	protected GameObject _target;
	
	public GameObject getTarget() {
		return _target;
	}
	
	public abstract void execute(LocalGameState state);
}
