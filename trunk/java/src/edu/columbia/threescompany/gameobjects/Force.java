package edu.columbia.threescompany.gameobjects;

import edu.columbia.threescompany.common.Coordinate;

public class Force extends Coordinate {
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
	
	private Force newRawForce(double Fx, double Fy) {
		return new Force(Fx * GameParameters.GRANULARITY_OF_PHYSICS,
						 Fy * GameParameters.GRANULARITY_OF_PHYSICS);
	}

	public Force inverse() {
		return newRawForce(-this.x, -this.y);
	}
}
