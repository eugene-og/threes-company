package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;

public class SlipperySpot extends ImmovableGameObject {
	private static final long serialVersionUID = 8260564646695768837L;
	private boolean _isDead;

	public SlipperySpot(double x, double y, double radius) {
		_position = new Coordinate(x, y);
		_radius = radius;
	}
	
	public void grow() {
		_radius /= GameParameters.SLIPPERY_SPOT_SHRINK_FACTOR;
		if (_radius < GameParameters.BLOB_INITIAL_SIZE)
			die();
	}
	
	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}

	public void die() {
		_isDead = true;
		_radius = 0.0d;
	}

	public boolean isDead() {
		return _isDead;
	}

	public GameObject clone() {
		return new SlipperySpot(_position.x, _position.y, _radius * GameParameters.SLIPPERY_RADIUS_MULTIPLIER);
	}

	public boolean checkCollision(GameObject rhs) {
		if (!(rhs instanceof Blob)) return false;	/* We don't care. */
		Blob blob = (Blob) rhs;
		
		blob.setSliding(collidingWith(blob));
		return false;
	}

}
