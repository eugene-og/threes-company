package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.game.Player;

public abstract class ImmovableGameObject extends GameObject {
	public Player getOwner() {
		// TODO Add a symbolic, graceful "not you" player?
		return null;
	}
	
	public double getWeight() {
		return 0;
	}
	
	public void applyForce(Force force) {
		/* I AM IMMUNE TO FORCE */
	}
}
