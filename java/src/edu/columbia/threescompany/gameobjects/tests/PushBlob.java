package edu.columbia.threescompany.gameobjects.tests;

import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.gameobjects.Force;
import edu.columbia.threescompany.gameobjects.ForceBlob;
import edu.columbia.threescompany.gameobjects.GameObject;

public class PushBlob extends ForceBlob {
	public PushBlob(double x, double y, Player player) {
		super(x, y, player);
		_directionModifier = -1;
	}
}
