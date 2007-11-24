package edu.columbia.threescompany.game;

import java.io.Serializable;
import java.util.List;

import edu.columbia.threescompany.game.graphics.GUIGameMove;

public class GameMove implements Serializable {
	/* README FIRST
	 * 
	 * Hi Dan and John:
	 * 
	 * For the sake of simplifying the link between our two classes, please
	 * don't use GameMove -- I'm changing it to handle all the simulation stuff.
	 * GUIGameMove is now how our classes talk, and I've defined it roughly
	 * (feel free to change the implementation). I figured I'd minimize the
	 * amount of data we use to converse.
	 */
	
	private static final long serialVersionUID = -6368788904877021374L;

	public GameMove(GUIGameMove move) {
		this._move = move;
	}
	
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
	
	private GUIGameMove _move;
}