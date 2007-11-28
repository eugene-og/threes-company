package edu.columbia.threescompany.gameobjects;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public abstract class Blob extends GameObject implements Serializable {
	public abstract Force actOn(GameObject obj);
	public abstract void activate(boolean activated);
	public abstract GameObject clone();	/* make this explicit -- each Blob may need
									 * to define it separately. */
	
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

	public GameObject spawn() {
		// TODO this positioning can be done more cleverly
		_radius /= 2;
		_position.x -= _radius;
		
		try {
			Constructor<? extends Blob> con = getClass().getConstructor(
					new Class[]{double.class, double.class, double.class, Player.class});
			return con.newInstance(_position.x + _radius * 1.1,
								   _position.y, _radius, _owner);
		} catch (Exception e) {
			throw new RuntimeException("Couldn't spawn blob in Java!", e);
		}
	}
	
	public void applyForce(Force force) {
		if (isAnchored()) return;
		applyIrresistibleForce(force);
	}
	
	public void applyIrresistibleForce(Force force) {
		// You can't fight the invisible hand of the player!
		_position.x += force.x / _weight;
		_position.y += force.y / _weight;
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
	
	public void checkCollision(GameObject rhs) {
		if (rhs == this) return;
		if (!collidingWith(rhs)) return;
		if (rhs.getRadius() <= _radius) rhs.die();
	}

	private void elasticReboundFrom(GameObject rhs) {
		// TODO Figure out the physics of this
	}

	public boolean isAnchored() {
		return _anchored;
	}
	
	public void setAnchored(boolean anchored) {
		_anchored = anchored;
	}

	protected Player _owner;
	protected boolean _dead;
	protected boolean _anchored;
}

