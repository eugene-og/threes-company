package edu.columbia.threescompany.client.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.gameobjects.tests.BlobTestTools;
import edu.columbia.threescompany.tests.BaseTestCase;

public class LocalGameStateTest extends BaseTestCase {
	public void testBasicMoveExecution() {
		LocalGameState state = BlobTestTools.getSingleBlobState(0, 0);
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>();
		
		Blob blob = (Blob) state.getObjects().get(0);
		
		finalPositions.put(blob, new Coordinate(2, 3));
		
		GUIGameMove guiMove = new GUIGameMove(finalPositions, new ArrayList<Blob>(), new ArrayList<Blob>());
		GameMove move = new GameMove(guiMove);
		
		state.executeMove(move);
		
		assertFalse("Blob should be alive", blob.isDead());
		assertRoughlyEqual("Blob should be in new position",
						   2, blob.getPosition().x);
		assertRoughlyEqual("Blob should be in new position",
				   		   3, blob.getPosition().y);
		
		finalPositions.put(blob, new Coordinate(5, 0));
		guiMove = new GUIGameMove(finalPositions, new ArrayList<Blob>(), new ArrayList<Blob>());
		move = new GameMove(guiMove);
		
		state.executeMove(move);
		
		assertFalse("Blob should be alive", blob.isDead());
		assertRoughlyEqual("Blob should be in new position",
						   5, blob.getPosition().x);
		assertRoughlyEqual("Blob should be in new position",
				   		   0, blob.getPosition().y);
	}
	
	public void testActivationTiming() {
		/* Looks like this (B is boring, P is push, single lines are forcefield)
		 * The O is the destination for the boring blob's move;
		 * the x is where it's actually going to end up.
		 * 
		 *                      |      x
		 *                   \  |  /
		 *         ======B->==\=|=/====O======
		 *                     \|/ 
		 *                -----(P)-----
		 *                      
		 * B is moving to the right while P is activated, so we should see
		 * B's path deviate significantly upward from the x-axis. 
		 *  
		 * Among other things, this verifies that activations happen at the
		 * beginning (if a blob has no other moves)
		 */
		
		LocalGameState state = BlobTestTools.getSingleBlobState(-1, 0);
		Blob boringBlob = (Blob) state.getObjects().get(0);
		PushBlob pushBlob = new PushBlob(0, -2.2, 1, BlobTestTools.PLAYER);
		state.addObject(pushBlob);
		
		List<Blob> activations = new ArrayList<Blob>();
		activations.add(pushBlob);
		
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>();
		finalPositions.put(boringBlob, new Coordinate(1, 0));
		
		GUIGameMove move = new GUIGameMove(finalPositions, activations, new ArrayList<Blob>());
		state.executeMove(new GameMove(move));
		
		assertFalse("Boring blob should be alive", boringBlob.isDead());
		assertFalse("Push blob should be alive", pushBlob.isDead());
		
		assertTrue("Boring blob should have significantly deviated from x-axis" +
				   " (y-position was " + boringBlob.getPosition().y + ")",
				   boringBlob.getPosition().y > 2 * BaseTestCase.EPSILON);
		assertTrue("Push blob should have been pushed backward by Newton's 3rd",
				   pushBlob.getPosition().y < -2.2 + 2 * BaseTestCase.EPSILON);
		
		/* Note that PushBlob and BoringBlob's x-coordinates can't be guessed
		 * all that easily, since Newton's 3 means that PushBlob was initially
		 * moving to the right (and resisting BoringBlob's attempt to reach x=1)
		 */
	}
}