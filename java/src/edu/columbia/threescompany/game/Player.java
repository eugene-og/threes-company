package edu.columbia.threescompany.game;

public class Player {
	public static final int NOBODY = -1;
	
	public String name;
	public int id;
	
	public Player(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public boolean equals(Player rhs) {
		return (id == rhs.id);
	}
}
