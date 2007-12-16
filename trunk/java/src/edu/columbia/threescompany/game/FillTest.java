package edu.columbia.threescompany.game;

import java.util.HashMap;
import java.util.Map;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.EventMove.MOVE_TYPE;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.Hole;
import edu.columbia.threescompany.gameobjects.tests.BlobTestTools;
import edu.columbia.threescompany.tests.BaseTestCase;

public class FillTest extends BaseTestCase {
	static double HOLE_SIZE = 1.5;
	
	public void testBasicFilling() {
		LocalGameState state = BlobTestTools.getSingleBlobState(3.0, 3.0);
		Blob blob = (Blob) state.getObjects().get(0);
		
		Hole hole = new Hole(4.0, 4.0, HOLE_SIZE);
		state.addObject(hole);
		
		Map<Blob, MOVE_TYPE> activations = new HashMap<Blob, MOVE_TYPE>(1);
		activations.put(blob, MOVE_TYPE.FILL);
		
		GUIGameMove move = new GUIGameMove(new HashMap<Blob, Coordinate>(), activations);
		state.executeMove(new GameMove(move));
		
		assertTrue("Blob should die", blob.isDead());
		assertFalse("Hole shouldn't die", hole.isDead());
		assertSignificantlyLessThan("Hole should shrink", hole.getRadius(), HOLE_SIZE);
	}
	
	public void testFullFilling() {
		LocalGameState state = BlobTestTools.getSingleBlobState(3.0, 3.0);
		Blob blob = (Blob) state.getObjects().get(0);
		
		Hole hole = new Hole(3.8, 3.8, 0.2);
		state.addObject(hole);
		
		Map<Blob, MOVE_TYPE> activations = new HashMap<Blob, MOVE_TYPE>(1);
		activations.put(blob, MOVE_TYPE.FILL);
		
		GUIGameMove move = new GUIGameMove(new HashMap<Blob, Coordinate>(), activations);
		state.executeMove(new GameMove(move));
		
		assertTrue("Blob should die", blob.isDead());
		assertTrue("Hole should die", hole.isDead());
	}
}
