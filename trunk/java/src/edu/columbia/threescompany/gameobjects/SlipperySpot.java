package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;

public class SlipperySpot extends ImmovableGameObject {
	private static final long serialVersionUID = 8260564646695768837L;

	public SlipperySpot(double x, double y, double radius) {
		_position = new Coordinate(x, y);
		_radius = radius;
	}
	
	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}

	public void die() {
		/* I AM IMMUNE TO DEATH */
	}

	public boolean isDead() {
		return false;
	}

	public GameObject clone() {
		return new SlipperySpot(_position.x, _position.y, _radius);
	}

	public boolean checkCollision(GameObject rhs) {
		if (!(rhs instanceof Blob)) return false;	/* We don't care. */
		Blob blob = (Blob) rhs;
		
		blob.setSliding(collidingWith(blob));
		return false;
	}

}
