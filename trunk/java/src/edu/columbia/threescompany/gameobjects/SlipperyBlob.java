package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public class SlipperyBlob extends StateModifyingBlob {
	public SlipperyBlob(double x, double y, double radius, Player owner, LocalGameState state) {
		super(x, y, radius, owner, state);
	}
	
	public SlipperyBlob(double x, double y, Player owner, LocalGameState state) {
		this(x, y, GameParameters.BLOB_INITIAL_SIZE, owner, state);
	}

	private static final long serialVersionUID = 8421390274567998819L;

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}

	public void activate(boolean activated) {
		if (!activated || isDead()) return;
		
		this.die();
		SlipperySpot spot = new SlipperySpot(this.getPosition().x, this.getPosition().y, this.getRadius() * 2);
		_state.addObject(spot);	
	}

	public GameObject clone() {
		return new SlipperyBlob(_position.x, _position.y, _radius, _owner, _state);
	}
}
