package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.Player;

public class ExplodingBlob extends StateModifyingBlob<Hole> {
	private static final long serialVersionUID = -5592289336246561407L;

	public ExplodingBlob(double x, double y, double radius, Player owner, LocalGameState state) {
		super(x, y, radius, owner, state);
	}
	
	public ExplodingBlob(double x, double y, Player owner, LocalGameState state) {
		this(x, y, GameParameters.BLOB_INITIAL_SIZE, owner, state);
	}
	
	public GameObject clone() {
		return new ExplodingBlob(_position.x, _position.y, _radius, _owner, _state);
	}
	
	public void activate(boolean activated) {
		if (!activated || isDead()) return;
		
		this.die();
		Hole hole = new Hole(this.getPosition().x, this.getPosition().y, this.getRadius() * 2);
		_state.addObject(hole);	
	}
}
