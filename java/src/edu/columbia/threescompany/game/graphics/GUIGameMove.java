package edu.columbia.threescompany.game.graphics;

import java.util.HashMap;
import java.util.Map;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.EventMove;
import edu.columbia.threescompany.game.EventMove.MOVE_TYPE;
import edu.columbia.threescompany.gameobjects.Blob;

public class GUIGameMove {
	/* ZvS says:
	 * 
	 * OK. Since we decided that all blobs move at once, and that activating a
	 * blob stops its motion, a GameMove can be expressed very simply -- as a
	 * terminal position (that is, the one the player is "trying" to move it to,
	 * not necessarily where it ends the turn) and a boolean value that tells
	 * us whether it's activated at the end of the turn or not.
	 * 
	 * So, really, _finalPositions is a map from blobs to where the player's
	 * mouse drag ended.
	 */

	private Map<Blob, Coordinate> _finalPositions;
	private Map<Blob, EventMove.MOVE_TYPE> _blobsToActivate;
	
	public GUIGameMove(Map<Blob, Coordinate> finalPositions, Map<Blob, MOVE_TYPE> blobsToActivate) {
		_finalPositions = finalPositions;
		_blobsToActivate = blobsToActivate;
	}
	
	public GUIGameMove(Map<Blob, Coordinate> finalPositions) {
		this(finalPositions, new HashMap<Blob, MOVE_TYPE>());
	}
	
	public Map<Blob, Coordinate> getFinalPositions() {
		return _finalPositions;
	}
	
	public Map<Blob, MOVE_TYPE> getBlobsToActivate() {
		return _blobsToActivate;
	}
}
