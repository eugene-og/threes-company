package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public class DeathRayBlob extends Blob {
	private static final long serialVersionUID = 4191070966886734000L;
	private boolean _activated;
	
	public DeathRayBlob(double x, double y, double radius, Player owner, double _theta) {
		super(x, y, radius, owner);
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
		if (obj == this) return Force.NULL_FORCE;
		if (!_activated) return Force.NULL_FORCE;
		
		if (vectorCollides(_rayDirection, obj)) 
			obj.die();
		
		return Force.NULL_FORCE;
	}
	
	public boolean vectorCollides(Coordinate vec, GameObject obj) {
		double xCtr = obj.getPosition().x;
		double yCtr = obj.getPosition().y;
		
		/* Number of unit vectors to the intersection of the perpendicular line from
		 * vector-line to obj */
		double u = ((xCtr - _position.x)*(vec.x) + (yCtr - _position.y)*(vec.y)) /
					(vec.length() * vec.length());
		
		if (u < 0) return false;	/* don't shoot backwards */
		
		Coordinate intersection = new Coordinate(_position.x + u * vec.x,
												 _position.y + u * vec.y);
		
		double distance = intersection.distanceFrom(obj.getPosition());
		
		System.out.println("Distance from death line to obj center is " + distance);
		
		return (distance <= obj.getRadius() && distance < GameParameters.DEATH_RAY_RANGE);
	}

	public void applyIrresistibleForce(Force force) {
		super.applyIrresistibleForce(force);
		updateTheta(force);
	}
	
	public GameObject clone() {
		return new DeathRayBlob(_position.x, _position.y, _radius, _owner, _theta);
	}
	
	public Coordinate getThetaCoordinate() {
		return new Coordinate(getPolarX(), getPolarY()); 		
	}
	
	private double getPolarX() {
		return getPosition().x + ((GameParameters.BLOB_INITIAL_SIZE + getRadius()) * Math.cos(Math.toRadians(_theta)));
	}

	private double getPolarY() {
		return getPosition().y + ((GameParameters.BLOB_INITIAL_SIZE + getRadius()) * Math.sin(Math.toRadians(_theta)));
	}
	
	public void setTheta(Coordinate dest) {
		Coordinate diff = dest.minus(getPosition());
		updateTheta(diff);
	}
	
	private void updateTheta(Coordinate diff) {
		if (diff.x == 0 && diff.y == 0) return;
		
		_theta = Math.toDegrees(Math.atan2(diff.y, diff.x)) % 360;
		_rayDirection = diff.toUnitVector();
	}
	
	public double getTheta() {
		return _theta;
	}

	private Coordinate _rayDirection = new Coordinate(1, 0);
	private double _theta = 0.0;
}