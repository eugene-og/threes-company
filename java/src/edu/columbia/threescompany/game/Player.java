package edu.columbia.threescompany.game;

import java.io.Serializable;

/**
 * Players are considered the same if they have the same name.
 */
public class Player implements Serializable {
	private static final long serialVersionUID = -8662542280020716885L;

	public static final Player NULL_PLAYER = new Player("Nobody");
	
	private String name;
	private double _actionPoints;
	
	public Player(String name) {
		this.name = name;
		this._actionPoints = 10.0d;
	}

	public boolean equals(Player rhs) {
		return name.equals(rhs.getName());
	}

	public String getName() {
		return name;
	}

	public double getActionPoints() {
		return _actionPoints;
	}

	public void setActionPoints(double points) {
		_actionPoints = points;
	}

	public String toString() {
		return name;
	}

	public void addActionPoints(double d) {
		_actionPoints += d;
	}

}
