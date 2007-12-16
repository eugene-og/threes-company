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
		_duration = timeNeededToMove(target.getWeight(), _vector.length());
	}
	
	public int timeNeededToMove(double weight, double distance) {
		double time = distance * weight;
		time /= GameParameters.FORCE_OF_USERS_HAND;
		time *= GameParameters.GRANULARITY_OF_PHYSICS;
		return (int) Math.ceil(time + 0.5);
	}
	
	public void execute(LocalGameState state) {
		if (_target.isDead()) return;
		
		_step++;
		/* Not a tweakable             vvv -- don't change it! It's based on the
		 * integral int(0, 1) 2x dx = 1. */
		double relativeForceStrength = 2.0 * (((double) (_duration - _step)) / (double) _duration);
		
		/* This simulates friction. */
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