package edu.columbia.threescompany.gameobjects;

import java.io.Serializable;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

/* Any object that appears on the game board. */
public abstract class GameObject implements Serializable {
	public Coordinate getPosition() {
		return _position.copy();
	}
	
	public double getRadius() {
		return _radius;
	}

	public double getWeight() {
		return _weight;
	}
	
	public abstract void applyForce(Force force);
	public abstract void grow();
	
	public abstract Player getOwner();
	
	/* Determines the resultant force that this object can apply to
	 * obj. (The inverse of that force will presumably be applied to
	 * this object. */
	public abstract Force actOn(GameObject obj);
	
	/* Things that don't die can ignore die events. */
	public abstract void die();
	public abstract boolean isDead();
	
	public abstract GameObject clone();
	
	public abstract boolean checkCollision(GameObject rhs);

	public boolean collidingWith(GameObject obj) {
		if (obj == this) return false;
		// TODO Can we move this to GameObject and make it public? If so I'll use it in the gui
		double distance = _position.distanceFrom(obj.getPosition());
		return (distance <= _radius + obj.getRadius());
	}

	public String toString() {
		String type = this.getClass().toString();
		type = type.substring(type.lastIndexOf('.') + 1);
		return (isDead() ? "DEAD " : "") + "Game object of type " + type + 
				" owned by " + getOwner() +
				" with position " + this._position + 
				" and radius " + this._radius;
	}
	
	protected Coordinate _position;
	protected double _weight;
	protected double _radius;
}
