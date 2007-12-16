package edu.columbia.threescompany.game;

import java.io.Serializable;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.Hole;

public class EventMove extends GameMoveComponent implements Serializable {
	public enum MOVE_TYPE {
		ACTIVATE, SPAWN, FILL, ROTATE
	};
	private static final long serialVersionUID = -4695883001161759780L;
	private MOVE_TYPE _moveType;

	public EventMove(GameObject target, int activationTime, MOVE_TYPE moveType) {
		_target = target;
		_activationTime = activationTime;
		_moveType = moveType;
	}

	public void execute(LocalGameState state) {
		if (_target.isDead()) return;
		
		if (_moveType == MOVE_TYPE.ACTIVATE)
			((Blob) _target).activate(true);
		else if (_moveType == MOVE_TYPE.SPAWN) {
			GameObject result = ((Blob) _target).spawn();
			if (result != null)
				state.addObject(result);
		} else if (_moveType == MOVE_TYPE.FILL) {
			for (GameObject obj : state.getObjects()) {
				if (!(obj instanceof Hole)) continue;
				if (obj.collidingWith(_target))
					((Hole) obj).shrink(_target.getRadius());
			}
			_target.die();
		}
	}

	public int getActivationTime() {
		return _activationTime;
	}
	
	public MOVE_TYPE getMoveType() {
		return _moveType;
	}

	public String toString() {
		return _moveType.toString();
	}
	
	private int _activationTime;
}
