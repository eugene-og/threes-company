package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;

public class APCIPoint extends ImmovableGameObject {
	private static final long serialVersionUID = -6043582531590989181L;

	public APCIPoint(double x, double y) {
		_position = new Coordinate(x, y);
		this._radius = 0;
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
		if (collidesWith(blob)) { //add 1 AP
			blob.getOwner().addActionPoints(1.0);
		}
		return false; //because nothing gets killed when colliding with ACPI point
	}

	public boolean collidesWith(GameObject blob) {
		return _position.distanceFrom(blob.getPosition()) <= blob.getRadius();
	}

	public GameObject clone() {
		return new APCIPoint(_position.x, _position.y);
	}
}
