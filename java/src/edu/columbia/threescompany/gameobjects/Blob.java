package edu.columbia.threescompany.gameobjects;

import java.lang.reflect.Constructor;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.Player;

public abstract class Blob implements GameObject {
	public abstract Force actOn(GameObject obj);

	protected Blob(double x, double y, double radius, Player owner) {
		_position = new Coordinate(x, y);
		this._radius = radius;
		recalculateWeight();
		this._owner = owner;
	}
	
	protected Blob(double x, double y, Player owner) {
		this (x, y, GameParameters.BLOB_INITIAL_SIZE, owner);
	}
	
	private void recalculateWeight() {
		_weight = _radius * _radius;
	}

	public Blob spawn() {
		// TODO this can be done more cleverly
		_radius /= 2;
		_position.x -= _radius;
		
		try {
			Constructor<? extends Blob> con = getClass().getConstructor(
					new Class[]{Double.class, Double.class, Double.class});
			return con.newInstance(_position.x + _radius * 1.1,
								   _position.y,
								   _radius);
		} catch (Exception e) {
			throw new RuntimeException("Couldn't spawn blob in Java!", e);
		}
	}
	
	public void applyForce(Force force) {
		_position.x += force.x / _weight;
		_position.y += force.y / _weight;
	}

	public Coordinate getPosition() {
		return _position.copy();
	}

	public double getRadius() {
		return _radius;
	}

	public double getWeight() {
		return _weight;
	}

	public void grow() {
		_radius *= GameParameters.BLOB_GROWTH_FACTOR;
		if (_radius > GameParameters.BLOB_SIZE_LIMIT)
			_radius = GameParameters.BLOB_SIZE_LIMIT;
		recalculateWeight();
	}
	
	public Player getOwner() {
		return _owner;
	}
	
	public boolean isDead() {
		return _dead;
	}
	
	public void die() {
		_dead = true;
	}
	
	protected Coordinate _position;
	protected double _weight;
	protected double _radius;
	protected Player _owner;
	protected boolean _dead;
}

