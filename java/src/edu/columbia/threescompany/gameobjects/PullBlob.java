package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.game.Player;

public class PullBlob extends ForceBlob {
	public PullBlob(double x, double y, Player owner) {
		super(x, y, owner);
		_directionModifier = 1;
	}
}
