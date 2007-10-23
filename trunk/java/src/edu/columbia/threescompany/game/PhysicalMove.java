package edu.columbia.threescompany.game;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.gameobjects.GameObject;

public class PhysicalMove extends GameMoveComponent {
	private double _fx, _fy, _duration = 0;
	
	public PhysicalMove(double dx, double dy, GameObject target) {
		_target = target;
		_fx = dx * _target.getWeight();
		_fy = dy * _target.getWeight();
	}
	
	public void setDuration(int duration) {
		// This is called once all moves for the turn are known
		_duration = duration;
	}
	
	public void execute() {
		if (_duration == 0)
			throw new RuntimeException("Duration of PhysicalMove must be set!");
		
		Force f = Force.newRawForce(_fx / _duration,
									_fy / _duration);
		_target.applyForce(f);
	}
}