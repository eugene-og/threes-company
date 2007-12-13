package edu.columbia.threescompany.gameobjects;

import java.lang.reflect.Constructor;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.Player;

public abstract class StateModifyingBlob<T extends GameObject> extends Blob {
	protected StateModifyingBlob(double x, double y, double radius, Player owner, LocalGameState state) {
		super(x, y, radius, owner);
		_state = state;
	}

	public Force actOn(GameObject obj) {
		return Force.NULL_FORCE;
	}
	
	protected GameObject copyBlob(double x, double y, double radius) {
		try {
			Constructor<? extends Blob> con = getClass().getConstructor(
					new Class[]{double.class, double.class, double.class, Player.class, LocalGameState.class});
			return con.newInstance(x, y, radius, _owner, _state);
		} catch (Exception e) {
			throw new RuntimeException("Couldn't spawn blob in Java!", e);
		}
	}
	
	protected LocalGameState _state;
}
