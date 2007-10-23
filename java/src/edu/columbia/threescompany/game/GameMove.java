package edu.columbia.threescompany.game;

import java.io.Serializable;
import java.util.List;

public class GameMove implements Serializable {
	private static final long serialVersionUID = -6368788904877021374L;

	public List<EventMove> instantMovesAt(int i) {
		// TODO Return the instant events happening at time i (which is an
		// integer ranging from 0..(GRANULARITY_OF_PHYSICS-1)). This refers
		// only to events like a blob exploding or activating.
		
		// This will only return move M for ONE value of i.
		return null;
	}

	public List<PhysicalMove> granularMovesAt(int i) {
		// TODO Return the granular events happening at time i (same range.)
		// This refers to BlobMoves, which are all motion-based.
		
		// This will return move M for MULTIPLE values of i.
		return null;
	}
	
	/* TODO: There's an email thread to determine the inner workings of this
	 * class! Nobody implement it! -- ZvS
	 */
}