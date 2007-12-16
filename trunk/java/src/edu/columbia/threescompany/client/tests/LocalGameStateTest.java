package edu.columbia.threescompany.client.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.EventMove.MOVE_TYPE;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.gameobjects.tests.BlobTestTools;
import edu.columbia.threescompany.tests.BaseTestCase;

public class LocalGameStateTest extends BaseTestCase {
	public void testBasicMoveExecution() {
		LocalGameState state = BlobTestTools.getSingleBlobState(0, 0);
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>();
		
		Blob blob = (Blob) state.getObjects().get(0);
		
		finalPositions.put(blob, new Coordinate(2, 3));
		
		GUIGameMove guiMove = new GUIGameMove(finalPositions);
		GameMove move = new GameMove(guiMove);
		
		state.executeMove(move);
		
		assertFalse("Blob should be alive", blob.isDead());
		assertRoughlyEqual("Blob should be in new position",
						   2, blob.getPosition().x);
		assertRoughlyEqual("Blob should be in new position",
				   		   3, blob.getPosition().y);
		
		finalPositions.put(blob, new Coordinate(5, 0));
		guiMove = new GUIGameMove(finalPositions);
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
		
		LocalGameState state = BlobTestTools.getSingleBlobState(3, 5);
		Blob boringBlob = (Blob) state.getObjects().get(0);
		PushBlob pushBlob = new PushBlob(5, 3, 1, BlobTestTools.PLAYER);
		state.addObject(pushBlob);
		
		Map<Blob, MOVE_TYPE> activations = new HashMap<Blob, MOVE_TYPE>();
		activations.put(pushBlob, MOVE_TYPE.ACTIVATE);
		
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>();
		finalPositions.put(boringBlob, new Coordinate(8, 5));
		
		GUIGameMove move = new GUIGameMove(finalPositions, activations);
		state.executeMove(new GameMove(move));
		
		assertFalse("Boring blob should be alive", boringBlob.isDead());
		assertFalse("Push blob should be alive", pushBlob.isDead());
		
		assertSignificantlyGreaterThan("Boring blob should have significantly deviated from (x=5) line",
								boringBlob.getPosition().y, 5);
		assertSignificantlyLessThan("Push blob should have been pushed backward by Newton's 3rd",
				   				pushBlob.getPosition().y, 3);
		
		/* Note that PushBlob and BoringBlob's x-coordinates can't be guessed
		 * all that easily, since Newton's 3 means that PushBlob was initially
		 * moving to the right (and resisting BoringBlob's attempt to reach x=1)
		 */
	}
	
	public void testCollision() {
		/* Initial:		Final:
		 * ==B===B==    ======B==
		 */
		LocalGameState state = BlobTestTools.getSingleBlobState(1.8, 0);
		Blob blob1 = (Blob) state.getObjects().get(0);
		GameObject blob2 = new BlobTestTools.BoringBlob(5.0, 0.0, 2.0, BlobTestTools.PLAYER2);
		state.addObject(blob2);
		
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>();
		finalPositions.put(blob1, new Coordinate(5.0, 0));
		
		GUIGameMove move = new GUIGameMove(finalPositions);
		state.executeMove(new GameMove(move));
		
		assertTrue("Left blob should die", blob1.isDead());
		assertFalse("Right blob shouldn't die", blob2.isDead());
		
		blob2.die();
		
		assertTrue("Game should be over", state.gameOver());
	}
	
	public void testSpawning() {
		List<GameObject> blobs = new ArrayList<GameObject>(1);
		BlobTestTools.BoringBlob blob = new BlobTestTools.BoringBlob(0, 0, GameParameters.BLOB_SIZE_LIMIT);
		blobs.add(blob);
		
		LocalGameState state = LocalGameState.getSpecifiedGameState(blobs);
		
		Map<Blob, MOVE_TYPE> activations = new HashMap<Blob, MOVE_TYPE>(1);
		activations.put(blob, MOVE_TYPE.SPAWN);
		
		GUIGameMove move = new GUIGameMove(new HashMap<Blob, Coordinate>(),
										   activations);
		state.executeMove(new GameMove(move));
		
		assertEquals("Should have spawned", 2, state.getObjects().size());
	}
}