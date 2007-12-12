package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;

public class AnchorPoint extends ImmovableGameObject {
	private static final long serialVersionUID = 2365645717684731802L;

	public AnchorPoint(double x, double y) {
		_position = new Coordinate(x, y);
		this._radius = 0;
	}	
	
	public void die() { /* I AM IMMUNE TO DEATH */ }

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}

	public boolean isDead() {
		return false;
	}

	public boolean checkCollision(GameObject rhs) {
		if (!(rhs instanceof Blob)) return false;	/* We don't care. */
		Blob blob = (Blob) rhs;
		
		blob.setAnchored(collidingWith(blob));
		return false;
	}

	public boolean collidingWith(GameObject blob) {
		/* Has no radius! */
		return _position.distanceFrom(blob.getPosition()) <= blob.getRadius();
	}

	public GameObject clone() {
		return new AnchorPoint(_position.x, _position.y);
	}
}
