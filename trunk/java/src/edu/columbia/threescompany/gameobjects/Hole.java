package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;

public class Hole extends ImmovableGameObject {
	private static final long serialVersionUID = 2365645717684731802L;
	private boolean _isDead = false;

	public Hole(double x, double y, double r) {
		_position = new Coordinate(x, y);
		this._radius = r;
	}	
	
	public void die() {
		_isDead = true;
	}

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}

	public boolean isDead() {
		return _isDead;
	}

	public boolean checkCollision(GameObject rhs) {
		if (!(rhs instanceof Blob)) return false;	/* We don't care. */
		Blob blob = (Blob) rhs;
		
		return collidesWith(blob);
	}

	public boolean collidesWith(GameObject blob) {
		/* We're checking if that blob's center is in the hole */
		return _position.distanceFrom(blob.getPosition()) <= _radius;
	}

	public GameObject clone() {
		return new Hole(_position.x, _position.y, _radius);
	}

	public void shrink(double radius) {
		_radius -= radius;
		if (_radius <= 0) die();
	}
}
