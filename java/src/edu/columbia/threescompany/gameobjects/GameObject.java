package edu.columbia.threescompany.gameobjects;

import java.io.Serializable;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.Player;

/* Any object that appears on the game board. */
public interface GameObject extends Serializable {
	public Coordinate getPosition();
	public double getRadius();
	public double getWeight();
	
	public void applyForce(Force force);
	public void grow();
	
	public Player getOwner();
	
	/* Determines the resultant force that this object can apply to
	 * obj. (The inverse of that force will presumably be applied to
	 * this object. */
	public Force actOn(GameObject obj);
	
	public void die();
	public boolean isDead();
}
