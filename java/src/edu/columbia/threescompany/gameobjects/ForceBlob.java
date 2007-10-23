package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public abstract class ForceBlob extends Blob {
	private boolean _activated;
	
	public ForceBlob(double x, double y, double radius, Player owner) {
		super(x, y, radius, owner);
	}

	public ForceBlob(double x, double y, Player owner) {
		super(x, y, owner);
	}
	
	public void activate(boolean activated) {
		_activated = activated;
	}

	public Force actOn(GameObject obj) {
		if (!_activated) return Force.NULL_FORCE;
			
		Coordinate pos = obj.getPosition();
		double distance = _position.distanceFrom(pos);
		
		Force force = new Force(pos.x - _position.x, pos.y - _position.y);
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