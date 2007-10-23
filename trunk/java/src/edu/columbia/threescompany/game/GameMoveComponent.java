package edu.columbia.threescompany.game;

import edu.columbia.threescompany.gameobjects.GameObject;

public abstract class GameMoveComponent {
	protected GameObject _target;
	
	public GameObject getTarget() {
		return _target;
	}
	
	public abstract void execute();
}
