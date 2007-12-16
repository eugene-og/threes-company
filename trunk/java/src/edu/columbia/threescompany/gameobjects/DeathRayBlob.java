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
		Coordinate intersection = new Coordinate(_position.x + u * vec.x,
												 _position.y + u * vec.y);
		
		double distance = intersection.distanceFrom(obj.getPosition());
		
		System.out.println("Distance from death line to obj center is " + distance);
		
		return (distance <= obj.getRadius() && distance < GameParameters.DEATH_RAY_RANGE);
	}

	public void applyForce(Force force) {
		super.applyForce(force);
		_rayDirection = force.toUnitVector();
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
		_rayDirection = dest.minus(_position);
	}
	
	public double getTheta() {
		return _theta;
	}

	private Coordinate _rayDirection = new Coordinate(1, 0);
	private double _theta = 0.0;
}