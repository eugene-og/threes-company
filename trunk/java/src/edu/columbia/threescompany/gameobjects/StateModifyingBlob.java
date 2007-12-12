package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.Player;

public abstract class StateModifyingBlob extends Blob {
	protected StateModifyingBlob(double x, double y, double radius, Player owner, LocalGameState state) {
		super(x, y, radius, owner);
		_state = state;
	}
	
	protected LocalGameState _state;
}
