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
		this(x, y, radius, owner, Math.random() * 360);
	}
	
	public DeathRayBlob(double x, double y, Player owner) {
		this(x, y, GameParameters.BLOB_INITIAL_SIZE, owner);
	}

	public void activate(boolean activated) {
		_activated = activated;	
	}
	
	public Force actOn(GameObject obj) {
		if (!_activated) return Force.NULL_FORCE;
		
		Coordinate pos = obj.getPosition();
		if (_position.distanceFrom(pos) < _strength && isInDeathRayAngle(obj)) 
			obj.die();
		
		return Force.NULL_FORCE;
	}

	private boolean isInDeathRayAngle(GameObject obj) {
		return (vectorCollides(_lastMove, obj));
	}
	
	public boolean vectorCollides(Coordinate vec, GameObject obj) {
		double xCtr = obj.getPosition().x;
		double yCtr = obj.getPosition().y;
		
		/* Number of unit vectors to the intersection of the perpendicular line from
		 * vector-line to obj */
		double u = ((xCtr - _position.x)*(vec.x) + (yCtr - _position.y)*(vec.y)) /
					(vec.length() * vec.length());
		Coordinate intersection = new Coordinate(_position.x + u * vec.x,
												 _position.y + u * vec.y);
		
		double distance = intersection.distanceFrom(obj.getPosition());
		
		System.out.println("Distance from death line to obj center is " + distance);
		
		return (distance <= obj.getRadius() && distance < _strength);
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
		_strength = 2 + GameParameters.DEATH_RAY_RANGE_MULTIPLIER/_radius;
	}
	
	public GameObject clone() {
		return new DeathRayBlob(_position.x, _position.y, _radius, _owner, _theta);
	}
	
	public Coordinate getThetaCoordinate() {
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
		
		_theta = Math.toDegrees(Math.atan2(y, x)) % 360;
		_lastMove = dest.minus(_position);
	}
	
	public double getTheta() {
		return _theta;
	}

	private Coordinate _lastMove = new Coordinate(1, 0);
	private double _theta = 0.0;
	private double _strength;
}