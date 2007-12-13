package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public abstract class StateModifyingBlob<T extends GameObject> extends Blob {
	protected StateModifyingBlob(double x, double y, double radius, Player owner, LocalGameState state) {
		super(x, y, radius, owner);
		_state = state;
	}

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}
	
	protected LocalGameState _state;
}
