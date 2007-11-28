package edu.columbia.threescompany.game.graphics;

import java.util.List;
import java.util.Map;

import edu.columbia.threescompany.common.Coordinate;
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
	private List<Blob> _blobsToActivate;
	private List<Blob> _blobsToSpawn;
	
	public GUIGameMove(Map<Blob, Coordinate> finalPositions, List<Blob> blobsToActivate,
					   List<Blob> blobsToSpawn) {
		_finalPositions = finalPositions;
		_blobsToActivate = blobsToActivate;
		_blobsToSpawn = blobsToSpawn;
	}
	
	public Map<Blob, Coordinate> getFinalPositions() {
		return _finalPositions;
	}
	
	public List<Blob> getBlobsToActivate() {
		return _blobsToActivate;
	}
	
	public List<Blob> getBlobsToSpawn() {
		return _blobsToSpawn;
	}
}
