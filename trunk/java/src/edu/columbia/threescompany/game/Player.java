package edu.columbia.threescompany.game;

public class Player {
	public static final int NOBODY = -1;
	
	private String name;
	private int id;
	private int _abilityPoints;
	
	public Player(int id, String name) {
		this.id = id;
		this.name = name;
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
