package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.game.Player;

public class ExplodingBlob extends Blob {
	protected ExplodingBlob(double x, double y, double radius, Player owner) {
		super(x, y, radius, owner);
	}
	
	protected ExplodingBlob(double x, double y, Player owner) {
		super(x, y, owner);
	}

	public Force actOn(GameObject obj) {
		// TODO how's this turn into a hole?
		return Force.NULL_FORCE;
	}
	
}