package edu.columbia.threescompany.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;

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
		activations = move.getBlobsToActivate();
		moves = new ArrayList<PhysicalMove>();
		
		Map<Blob, Coordinate> finalPositions = move.getFinalPositions();
		for (Blob blob : finalPositions.keySet()) {
			// FIXME Moves should be a constant force!
			Coordinate pos = finalPositions.get(blob);
			moves.add(new PhysicalMove(pos, blob));
		}
		
		// TODO figure out activation timing
	}

	public List<PhysicalMove> granularMovesAt(int i) {
		// FIXME: Are all moves really happening at all times? This isn't
		//		  clearly spec'd right now.
		return moves;
	}
	
	public List<EventMove> eventMovesAt(int i) {
		// FIXME
		return Collections.EMPTY_LIST;
	}
	
	private List<PhysicalMove> moves;
	private List<Blob> activations;
}