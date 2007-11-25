package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public abstract class ForceBlob extends Blob {
	private boolean _activated;
	
	protected ForceBlob(double x, double y, double radius, Player owner) {
		super(x, y, radius, owner);
	}

	protected ForceBlob(double x, double y, Player owner) {
		super(x, y, owner);
	}
	
	public void activate(boolean activated) {
		_activated = activated;
	}

	public Force actOn(GameObject obj) {
		if (!_activated) return Force.NULL_FORCE;
		if (obj == this) return Force.NULL_FORCE;
			
		Coordinate pos = obj.getPosition();
		double distance = _position.distanceFrom(pos);
		
		Force force = new Force(_position.x - pos.x, _position.y - pos.y);
		force.x *= _directionModifier;
		force.y *= _directionModifier;
		
		/* 1/(r*r) */
		force.x /= distance * distance;
		force.y /= distance * distance;
		
		return force;
	}

	/* Set in subclasses */
	protected int _directionModifier = 0;
}