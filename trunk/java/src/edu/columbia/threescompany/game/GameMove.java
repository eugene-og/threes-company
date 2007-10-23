package edu.columbia.threescompany.game;

import java.io.Serializable;
import java.util.List;

public class GameMove implements Serializable {
	private static final long serialVersionUID = -6368788904877021374L;

	public List<EventMove> instantMovesAt(int i) {
		// TODO Return the instant events happening at time i (which is an
		// integer ranging from 0..(GRANULARITY_OF_PHYSICS-1)). This refers
		// only to events like a blob exploding or activating.
		return null;
	}

	public List<PhysicalMove> granularMovesAt(int i) {
		// TODO Return the granular events happening at time i (same range.)
		// This refers to BlobMoves, which are all motion-based.
		return null;
	}
}