package edu.columbia.threescompany.gameobjects;

import java.io.Serializable;

import edu.columbia.threescompany.game.Player;

public class PushBlob extends ForceBlob implements Serializable {
	private static final long serialVersionUID = -2875768398484308555L;

	public PushBlob(double x, double y, Player owner) {
		super(x, y, owner);
		_directionModifier = -1;
	}
	
	public PushBlob(double x, double y, double radius, Player owner) {
		super(x, y, radius, owner);
		_directionModifier = -1;
	}
	
	public Blob clone() {
		return new PushBlob(_position.x, _position.y, _radius, _owner);
	}
}
