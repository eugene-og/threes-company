package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.client.BlobsClient;
import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public class ExplodingBlob extends Blob {
	private static final long serialVersionUID = -5592289336246561407L;
	private boolean _activated;

	public ExplodingBlob(double x, double y, double radius, Player owner) {
		super(x, y, radius, owner);
	}
	
	public ExplodingBlob(double x, double y, Player owner) {
		super(x, y, owner);
	}

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}
	
	public void activate(boolean activated) {
		_activated = activated;
		if (_activated) {
			LocalGameState gameState = BlobsClient.getGameState();
			this.die();
			gameState.addObject(new Hole(this.getPosition().x, this.getPosition().y, this.getRadius() * 2));	
		}
	}
	
	public GameObject clone() {
		return new ExplodingBlob(_position.x, _position.y, _radius, _owner);
	}
}
