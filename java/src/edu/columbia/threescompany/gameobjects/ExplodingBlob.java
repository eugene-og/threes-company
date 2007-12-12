package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public class ExplodingBlob extends StateModifyingBlob {
	private static final long serialVersionUID = -5592289336246561407L;
	private boolean _activated;

	public ExplodingBlob(double x, double y, double radius, Player owner, LocalGameState state) {
		super(x, y, radius, owner, state);
	}
	
	public ExplodingBlob(double x, double y, Player owner, LocalGameState state) {
		this(x, y, GameParameters.BLOB_INITIAL_SIZE, owner, state);
	}

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}
	
	public void activate(boolean activated) {
		_activated = activated;
		if (_activated) {
			this.die();
			_state.addObject(new Hole(this.getPosition().x, this.getPosition().y, this.getRadius() * 2));	
		}
	}
	
	public GameObject clone() {
		return new ExplodingBlob(_position.x, _position.y, _radius, _owner, _state);
	}
}
