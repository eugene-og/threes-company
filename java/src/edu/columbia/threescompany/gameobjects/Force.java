package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;

public class Force extends Coordinate {
	public static final Force NULL_FORCE = new Force(0, 0);

	public Force(double Fx, double Fy) {
		super(Fx, Fy);
	}
}
