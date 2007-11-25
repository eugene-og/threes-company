package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public class DeathRayBlob extends Blob {
	private static final long serialVersionUID = 4191070966886734000L;
	private boolean _activated;
	
	public DeathRayBlob(double x, double y, double radius, Player owner) {
		super(x, y, radius, owner);
		recalculateStrength();
	}
	
	public DeathRayBlob(double x, double y, Player owner) {
		super(x, y, owner);
		recalculateStrength();
	}

	public void activate(boolean activated) {
		_activated = activated;	
	}
	
	public Force actOn(GameObject obj) {
		if (!_activated) return Force.NULL_FORCE;
		if (_lastMove == null) return Force.NULL_FORCE;
		
		Coordinate pos = obj.getPosition();
		if (_position.distanceFrom(pos) < _strength && isInDeathRayAngle(obj)) 
			obj.die();
		
		return Force.NULL_FORCE;
	}

	private boolean isInDeathRayAngle(GameObject obj) {
		Coordinate intersectionVector = _lastMove.times(_strength);
		
		for (int i = 0; i < 4; i++) {
			if (vectorCollides(intersectionVector, obj)) return true;
			intersectionVector = intersectionVector.rotate(90);
		}
		return false;
	}
	
	public boolean vectorCollides(Coordinate vec, GameObject obj) {
		// TODO Is the distance from obj.pos to
		//	the line (_pos + vec.times(n)) < obj.radius?
		return false;
	}

	public void applyForce(Force force) {
		super.applyForce(force);
		_lastMove = force.toUnitVector();
	}

	public void grow() {
		super.grow();
		recalculateStrength();
	}
	
	public Blob spawn() {
		DeathRayBlob spawnedBlob = (DeathRayBlob) super.spawn();
		
		recalculateStrength();
		spawnedBlob.recalculateStrength();
		return spawnedBlob;
	}
	
	private void recalculateStrength() {
		_strength = GameParameters.DEATH_RAY_RANGE_MULTIPLIER/_radius;
	}
	
	public Blob clone() {
		return new DeathRayBlob(_position.x, _position.y, _radius, _owner);
	}
	
	private Coordinate _lastMove = null;
	private double _strength;
}