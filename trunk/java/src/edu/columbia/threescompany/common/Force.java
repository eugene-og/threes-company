package edu.columbia.threescompany.common;

import edu.columbia.threescompany.gameobjects.GameParameters;

public class Force extends Coordinate {
	private static final long serialVersionUID = 4919007009094730983L;
	public static final Force NULL_FORCE = new Force(0, 0);

	public Force(double Fx, double Fy) {
		/* Physics granularity kicks in here. These forces are going to be
		 * applied GRANULARITY times in sequence, so we want to apply
		 * 1/GRANULARITY of the total force -- i.e., if everything remains
		 * constant for the turn, Fx will be the NET force.
		 */
		super(Fx / GameParameters.GRANULARITY_OF_PHYSICS,
			  Fy / GameParameters.GRANULARITY_OF_PHYSICS);
	}
	
	public static Force newRawForce(double Fx, double Fy) {
		return new Force(Fx * GameParameters.GRANULARITY_OF_PHYSICS * 10,
						 Fy * GameParameters.GRANULARITY_OF_PHYSICS * 10);
	}

	public Force inverse() {
		return newRawForce(-this.x, -this.y);
	}
}
