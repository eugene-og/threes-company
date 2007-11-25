package edu.columbia.threescompany.game;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;

public class PhysicalMove extends GameMoveComponent {
	private double _fx, _fy;
	private int _duration;
	
	public PhysicalMove(Coordinate finalPos, GameObject target) {
		this(finalPos.minus(target.getPosition()).x,
             finalPos.minus(target.getPosition()).y,
             target);
	}
	
	public PhysicalMove(double dx, double dy, GameObject target) {
		_target = target;
		
		Coordinate vector = new Coordinate(dx, dy);
		double theta = vector.theta();
		_fx = GameParameters.FORCE_OF_USERS_HAND * Math.cos(theta);
		_fy = GameParameters.FORCE_OF_USERS_HAND * Math.sin(theta);
		
		/* Moves can take an arbitrarily long time -- the force that the user's
		 * "shove" applies is constant, so just figure out how long we need
		 * to animate this. */
		_duration = (int) (GameParameters.GRANULARITY_OF_PHYSICS * vector.length() * target.getWeight() /
							GameParameters.FORCE_OF_USERS_HAND);
	}
	
	public void execute() {
		Force f = new Force(_fx, _fy);
		((Blob) _target).applyIrresistibleForce(f);
	}
	
	public int getDuration() {
		return _duration;
	}
}