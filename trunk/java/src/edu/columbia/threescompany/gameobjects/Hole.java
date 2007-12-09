package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;

public class Hole extends ImmovableGameObject {
	private static final long serialVersionUID = 2365645717684731802L;

	public Hole(double x, double y, double r) {
		_position = new Coordinate(x, y);
		this._radius = r;
	}	
	
	public void die() { /* I AM IMMUNE TO DEATH */ }
	public void grow() { /* I AM IMMUNE TO GROWTH */ }

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}

	public boolean isDead() {
		return false;
	}

	public boolean checkCollision(GameObject rhs) {
		if (!(rhs instanceof Blob)) return false;	/* We don't care. */
		Blob blob = (Blob) rhs;
		if (blob.getRadius() > _radius) return false;
		
		return collidesWith(blob);
	}

	public boolean collidesWith(GameObject blob) {
		return _position.distanceFrom(blob.getPosition()) <= _radius;
	}

	public GameObject clone() {
		return new Hole(_position.x, _position.y, _radius);
	}
}
