package edu.columbia.threescompany.game;

import edu.columbia.threescompany.gameobjects.Blob;

public class EventMove extends GameMoveComponent {
	public EventMove(Blob target, int activationTime) {
		_target = target;
		_activationTime = activationTime;
	}

	public void execute() {
		((Blob) _target).activate(true);
	}

	public int getActivationTime() {
		return _activationTime;
	}
	
	private int _activationTime;
}
