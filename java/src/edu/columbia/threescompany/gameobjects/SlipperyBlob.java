package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public class SlipperyBlob extends Blob {
	protected SlipperyBlob(double x, double y, double radius, Player owner) {
		super(x, y, radius, owner);
	}

	private static final long serialVersionUID = 8421390274567998819L;

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}

	public void activate(boolean activated) {
		if (activated) return;
		
		// TODO die and create an OilSlick [on deactivation]
	}

}
