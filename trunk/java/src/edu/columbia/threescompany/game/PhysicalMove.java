package edu.columbia.threescompany.game;

import java.io.Serializable;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;

public class PhysicalMove extends GameMoveComponent implements Serializable {
	private static final long serialVersionUID = -8445815830982267666L;
	private double _fx, _fy;
	private int _duration;
	private int _step = 0;
	private Coordinate _vector;
	
	public PhysicalMove(Coordinate finalPos, GameObject target) {
		this(finalPos.minus(target.getPosition()).x,
             finalPos.minus(target.getPosition()).y,
             target);
	}
	
	public PhysicalMove(double dx, double dy, GameObject target) {
		_target = target;
		
		_vector = new Coordinate(dx, dy);
		double theta = _vector.theta();
		_fx = GameParameters.FORCE_OF_USERS_HAND * Math.cos(theta);
		_fy = GameParameters.FORCE_OF_USERS_HAND * Math.sin(theta);
		
		/* Moves can take an arbitrarily long time -- the force that the user's
		 * "shove" applies is constant, so just figure out how long we need
		 * to animate this. */
		_duration = (int) (GameParameters.GRANULARITY_OF_PHYSICS * _vector.length() * target.getWeight() /
							GameParameters.FORCE_OF_USERS_HAND);
	}
	
	public void execute(LocalGameState state) {
		if (_target.isDead()) return;
		
		/* This integrates to 1 (almost) over the curve, but creates friction */
		_step++;
		double relativeForceStrength = 2.1 * (((double) (_duration - _step)) / (double) _duration);
		System.out.println("Step " + _step + " of " + _duration + ", rel str = " + relativeForceStrength);
		
		Force f = new Force(_fx * relativeForceStrength,
							_fy * relativeForceStrength);
		((Blob) _target).applyIrresistibleForce(f);
	}
	
	public int getDuration() {
		return _duration;
	}
	
	public String toString() {
		return "PhysicalMove by delta " + _vector.toString();
	}
}