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
	
	public abstract void checkCollision(GameObject rhs);

	protected Coordinate _position;
	protected double _weight;
	protected double _radius;
}
