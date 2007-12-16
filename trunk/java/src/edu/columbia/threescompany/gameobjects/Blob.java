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
		double dRadius = GameParameters.BLOB_SIZE_LIMIT * GameParameters.BLOB_GROWTH_FACTOR;
		_position.x -= dRadius;
		
		return copyBlob(_position.x + 2 * dRadius, _position.y, _radius);
	}
	
	protected GameObject copyBlob(double x, double y, double radius) {
		try {
			Constructor<? extends Blob> con = getClass().getConstructor(
					new Class[]{double.class, double.class, double.class, Player.class});
			return con.newInstance(x, y, radius, _owner);
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
		_position.x += force.x / getEffectiveWeight();
		_position.y += force.y / getEffectiveWeight();
	}
	
	private double getEffectiveWeight() {
		return _weight / (_sliding ? GameParameters.SLIPPERINESS : 1);
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
		if (isDead()) return;
		System.out.println("KILLING " + this);
		_dead = true;
	}
	
	public boolean checkCollision(GameObject rhs) {
		/* Return true if RHS should be killed */
		if (rhs == this) return false;
		if (!(rhs instanceof Blob)) return false;
		if (!collidingWith(rhs)) return false;
		return ((int)(rhs.getRadius()*10) <= (int)(_radius*10));
	}

	public boolean isAnchored() {
		return _anchored;
	}
	
	public boolean isEnergized() {
		return _energized;
	}
	
	public void setAnchored(boolean anchored) {
		_anchored = anchored;
	}
	
	public void setEnergized(boolean energized) {
		_energized = energized;
	}
	
	public void setSliding(boolean sliding) {
		_sliding = sliding;
	}

	protected Player _owner;
	protected boolean _dead;
	protected boolean _anchored;
	protected boolean _energized;
	protected boolean _sliding;
}