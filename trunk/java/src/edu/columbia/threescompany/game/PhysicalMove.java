package edu.columbia.threescompany.game;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.gameobjects.GameObject;

public class PhysicalMove extends GameMoveComponent {
	private double _fx, _fy;
	
	public PhysicalMove(Coordinate finalPos, GameObject target) {
		this(finalPos.minus(target.getPosition()).x,
             finalPos.minus(target.getPosition()).y,
             target);
	}
	
	public PhysicalMove(double dx, double dy, GameObject target) {
		_target = target;
		_fx = dx * _target.getWeight();
		_fy = dy * _target.getWeight();
	}
	
	public void execute() {
		Force f = new Force(_fx, _fy);
		_target.applyForce(f);
	}
}