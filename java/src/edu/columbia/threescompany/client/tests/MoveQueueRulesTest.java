package edu.columbia.threescompany.client.tests;

import java.util.HashMap;
import java.util.Map;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.EventMove.MOVE_TYPE;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.tests.BlobTestTools;
import edu.columbia.threescompany.tests.BaseTestCase;

public class MoveQueueRulesTest extends BaseTestCase {
	public void testCancelEventsAfterCollision() {
		/* verifies fix for:
		 * Issue 28 -- If a blob has a "split" event queued and is destroyed,
		 * it then goes on to split, which fucks up the deadness. */
		
		LocalGameState state = BlobTestTools.getSingleBlobState(0.0, 2.0);
		Blob blob = (Blob) state.getObjects().get(0);
		Blob blob2 = BlobTestTools.getBoringBlob(0.0, 0.0);
		state.addObject(blob2);
		
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>();
		finalPositions.put(blob2, new Coordinate(0.0, 3.0));
		
		Map<Blob, MOVE_TYPE> activations = new HashMap<Blob, MOVE_TYPE>(1);
		activations.put(blob2, MOVE_TYPE.SPAWN);
		
		GUIGameMove move = new GUIGameMove(finalPositions, activations);
		state.executeMove(new GameMove(move));
		
		assertTrue("Everything should be dead", blob.isDead());
		assertTrue("Everything should be dead", blob2.isDead());
		assertEquals("Nothing should be spawned", 2, state.getObjects().size());
	}
}
