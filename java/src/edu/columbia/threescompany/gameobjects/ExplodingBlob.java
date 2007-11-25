package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public class ExplodingBlob extends Blob {
	private static final long serialVersionUID = -5592289336246561407L;

	public ExplodingBlob(double x, double y, double radius, Player owner) {
		super(x, y, radius, owner);
	}
	
	public ExplodingBlob(double x, double y, Player owner) {
		super(x, y, owner);
	}

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}
	
	public void activate(boolean activated) {
		if (activated) return;
		
		// TODO die and create a Hole [on deactivation]
	}
	
	public Blob clone() {
		return new ExplodingBlob(_position.x, _position.y, _radius, _owner);
	}
}
