package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.game.Player;

public class PushBlob extends ForceBlob {
	public PushBlob(double x, double y, Player player) {
		super(x, y, player);
		_directionModifier = -1;
	}
}
