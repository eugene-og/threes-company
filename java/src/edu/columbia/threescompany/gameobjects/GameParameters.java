package edu.columbia.threescompany.gameobjects;

public class GameParameters {
	public static final double BLOB_SIZE_LIMIT = 3;
	public static final double BLOB_INITIAL_SIZE = 1;
	public static final double BLOB_GROWTH_FACTOR = 1.095;
	
	public static final double DEATH_RAY_RANGE_MULTIPLIER = 3;
	public static final double BOARD_SIZE = 20;
	
	/* How many pieces moves are broken down into (basically 1/dt).
	 * I may later replace this with integrals. --ZvS */
	public static final double GRANULARITY_OF_PHYSICS = 100;
}
