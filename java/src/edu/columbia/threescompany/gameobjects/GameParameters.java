package edu.columbia.threescompany.gameobjects;

public class GameParameters {
	public static final double BLOB_SIZE_LIMIT = 1.5;
	public static final double BLOB_INITIAL_SIZE = 0.5;
	public static final double BLOB_GROWTH_FACTOR = 1.095;
	
	public static final double DEATH_RAY_RANGE_MULTIPLIER = 3;
	public static final double BOARD_SIZE = 20; // TODO Can we make this an int? I think it makes screen to world coordinate conversion saner.
	
	/* How many pieces moves are broken down into (basically 1/dt).
	 * I may later replace this with integrals. --ZvS */
	public static final double GRANULARITY_OF_PHYSICS = 150;
	
	/* After all blobs have stopped being moved by PLAYERS, we want to sit and
	 * let the force blobs do their thing. How long should this be?
	 * (In terms of GRANULARITY)
	 */
	public static final double ADDITIONAL_SIMULATION_LENGTH = 2 * GRANULARITY_OF_PHYSICS;
	
	/* How hard does a move push? This probably shouldn't be messed with, as
	 * GUI framerate is going to be affected by it. */
	public static final double FORCE_OF_USERS_HAND = 3;
	
	/* How many milliseconds between average animation frames? (Variable!) */
	public static final int AVERAGE_MS_FRAME_GAP = 10;
}
