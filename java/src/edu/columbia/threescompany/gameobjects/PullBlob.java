package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.game.Player;

public class PullBlob extends ForceBlob {
	private static final long serialVersionUID = 5430255748901796459L;

	public PullBlob(double x, double y, Player owner) {
		super(x, y, owner);
		_directionModifier = 1;
	}
}
