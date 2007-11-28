package edu.columbia.threescompany.game;

import java.io.Serializable;

/**
 * Players are considered the same if they have the same name.
 */
public class Player implements Serializable {
	private static final long serialVersionUID = -8662542280020716885L;

	public static final Player NULL_PLAYER = new Player("Nobody");
	
	private String name;
	private int _abilityPoints;
	
	public Player(String name) {
		this.name = name;
		this._abilityPoints = 10;
	}

	public boolean equals(Player rhs) {
		return (name == rhs.name);
	}

	public String getName() {
		return name;
	}

	public int getAbilityPoints() {
		return _abilityPoints;
	}

	public void setAbilityPoints(int points) {
		_abilityPoints = points;
	}

	public String toString() {
		return name;
	}
}
