package edu.columbia.threescompany.game;

import java.io.Serializable;

import edu.columbia.threescompany.gameobjects.Blob;

public class EventMove extends GameMoveComponent implements Serializable {
	private static final long serialVersionUID = -4695883001161759780L;

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
