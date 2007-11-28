package edu.columbia.threescompany.game;

import java.io.Serializable;

import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;

public class EventMove extends GameMoveComponent implements Serializable {
	public enum MOVE_TYPE {
		ACTIVATE, SPAWN
	};
	private static final long serialVersionUID = -4695883001161759780L;
	private MOVE_TYPE _type;

	public EventMove(GameObject target, int activationTime) {
		_target = target;
		_activationTime = activationTime;
		_type = MOVE_TYPE.ACTIVATE;	// TODO
	}

	public void execute() {
		((Blob) _target).activate(true);
	}

	public int getActivationTime() {
		return _activationTime;
	}
	
	public MOVE_TYPE getMoveType() {
		return _type;
	}

	public String toString() {
		return _type.toString();
	}
	
	private int _activationTime;
}
