package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public abstract class ImmovableGameObject extends GameObject {
	public Player getOwner() {
		return Player.NULL_PLAYER;
	}
	
	public double getWeight() {
		return 0;
	}
	
	public void applyForce(Force force) {
		/* I AM IMMUNE TO FORCE */
	}
}
