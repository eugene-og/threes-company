package edu.columbia.threescompany.gameobjects;

public class GameParameters {
	public static final double BLOB_SIZE_LIMIT = 1.5;
	public static final double BLOB_INITIAL_SIZE = 0.5;
	public static final double BLOB_GROWTH_FACTOR = 1.095;
	public static final double SLIPPERY_SPOT_SHRINK_FACTOR = 1.07;
	
	public static final double DEATH_RAY_RANGE = 10;
	public static final double BOARD_SIZE = 40; // TODO Can we make this an int? I think it makes screen to world coordinate conversion saner.
	
	/* How many pieces moves are broken down into (basically 1/dt).
	 * I may later replace this with integrals. --ZvS */
	public static final double GRANULARITY_OF_PHYSICS = 115;
	
	/* After all blobs have stopped being moved by PLAYERS, we want to sit and
	 * let the force blobs do their thing. How long should this be?
	 * (In terms of GRANULARITY)
	 */
	public static final double ADDITIONAL_SIMULATION_LENGTH = 2 * GRANULARITY_OF_PHYSICS;
	
	/* How hard does a move push? This probably shouldn't be messed with, as
	 * GUI framerate is going to be affected by it. */
	public static final double FORCE_OF_USERS_HAND = 15;
	
	/* Time between simulation frames. */
	public static final int AVERAGE_MS_FRAME_GAP = 50;
	
	/* Time between animation frames. */
	public static final int FRAME_DELAY = 20;

	/* How much does stuff slide over a slippery spot? */
	public static final int SLIPPERINESS = 3;
	
	public static final int FORCE_BLOB_STRENGTH = 23;
	/* Area of effect for blob forces */
	public static final double FORCE_RADIUS = BOARD_SIZE / 5;
	
	public static final double EXPLOSION_RADIUS_MULTIPLIER = 2.5;
	public static final double SLIPPERY_RADIUS_MULTIPLIER =
		EXPLOSION_RADIUS_MULTIPLIER;
	
	/* Percentage difference between two colliding blobs for double kill */ 
	public static final double PERCENTAGE_DIFFERENCE_FOR_KILL = 0.10;
	
	/* Base AP costs (tweakable) */
	public static final int BASE_PHYSICAL_MOVE_COST = 0;
	public static final int BASE_FORCE_COST = 3;
	public static final int BASE_EXPLODE_COST = 3;
	public static final int BASE_SPLIT_COST = 3;
	public static final int BASE_FIRE_SLIPPERY_COST = 6;
	public static final int BASE_FIRE_DEATH_RAY_COST = 9;
	
}
