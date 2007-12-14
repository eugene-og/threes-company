package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public class DeathRayBlob extends Blob {
	private static final long serialVersionUID = 4191070966886734000L;
	private boolean _activated;
	
	public DeathRayBlob(double x, double y, double radius, Player owner, double _theta) {
		super(x, y, radius, owner);
		recalculateStrength();
		this._theta = _theta;
	}
	
	public DeathRayBlob(double x, double y, double radius, Player owner) {
		super(x, y, radius, owner);
		recalculateStrength();
		_theta = Math.random() * 360;
	}
	
	public DeathRayBlob(double x, double y, Player owner) {
		super(x, y, owner);
		recalculateStrength();
		_theta = Math.random() * 360;
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
	
	public GameObject spawn() {
		DeathRayBlob spawnedBlob = (DeathRayBlob) super.spawn();
		
		recalculateStrength();
		spawnedBlob.recalculateStrength();
		return spawnedBlob;
	}
	
	private void recalculateStrength() {
		_strength = GameParameters.DEATH_RAY_RANGE_MULTIPLIER/_radius;
	}
	
	public GameObject clone() {
		return new DeathRayBlob(_position.x, _position.y, _radius, _owner, _theta);
	}
	
	public Coordinate getLastMoveVector() {
		return new Coordinate(getPolarX(_theta), getPolarY(_theta)); 		
	}
	
	public double getPolarX(double theta) {
		return getPosition().x + ((GameParameters.BLOB_INITIAL_SIZE + getRadius()) * Math.cos(Math.toRadians(theta)));
	}

	private double getPolarY(double theta) {
		return getPosition().y - ((GameParameters.BLOB_INITIAL_SIZE + getRadius()) * Math.sin(Math.toRadians(theta)));
	}
	
	public void setTheta(Coordinate dest) {
		Coordinate src = getPosition();
		double x = dest.x - src.x;
		double y = src.y - dest.y;
		int quad = 0;
		
		if (x < 0) quad = 180;
		else if (y < 0) quad = 360;
		_theta = Math.toDegrees(Math.atan(y/x)) + quad;
	}
	
	public double getTheta() {
		return _theta;
	}

	private Coordinate _lastMove = null;
	private double _theta = 0.0;
	private double _strength;
}