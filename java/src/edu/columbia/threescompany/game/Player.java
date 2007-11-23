package edu.columbia.threescompany.game;

import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = -8662542280020716885L;

	public static final int NOBODY = -1;
	
	private String name;
	private int id;
	private int _abilityPoints;
	
	public Player(int id, String name) {
		this.id = id;
		this.name = name;
		this._abilityPoints = 10;
	}

	public boolean equals(Player rhs) {
		return (id == rhs.id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getAbilityPoints() {
		return _abilityPoints;
	}

	public void setAbilityPoints(int points) {
		_abilityPoints = points;
	}

	public int getId() {
		return id;
	}
}
