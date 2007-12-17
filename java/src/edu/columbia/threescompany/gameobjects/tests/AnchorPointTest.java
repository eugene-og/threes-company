package edu.columbia.threescompany.gameobjects.tests;

import java.util.HashMap;
import java.util.Map;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.EventMove.MOVE_TYPE;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.AnchorPoint;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.tests.BaseTestCase;

public class AnchorPointTest extends BaseTestCase {
	public void testCollision() {
		GameObject blob = BlobTestTools.getBoringBlob(0, 0);
		AnchorPoint point = new AnchorPoint(0.8, 0);
		assertTrue("Should collide", point.collidingWith(blob));
		blob = BlobTestTools.getBoringBlob(-0.5, 0);
		assertFalse("Should not collide", point.collidingWith(blob));
	}
	
	public void testAnchoring() {
		/* P is push blob, B is boring, ^ is anchor point.
		 * Initial:
		 * ===P===B==^===
		 * 
		 * Final:
		 * =P========B===
		 */
		
		LocalGameState state = BlobTestTools.getSingleBlobState(7.5, 0);
		GameObject boringBlob = state.getObjects().get(0);
		Blob pushBlob = new PushBlob(5.0, 0.0, 1.0, BlobTestTools.PLAYER);
		AnchorPoint anchorPoint = new AnchorPoint(9.5, 0);
		
		state.addObject(anchorPoint);
		state.addObject(pushBlob);
		
		Map<Blob, MOVE_TYPE> activations = new HashMap<Blob, MOVE_TYPE>(1);
		activations.put(pushBlob, MOVE_TYPE.ACTIVATE);
		
		GUIGameMove move = new GUIGameMove(new HashMap<Blob, Coordinate>(), activations);
		state.executeMove(new GameMove(move));
		
		/* Blob should be caught on the anchor and not make it to its destination.
		 * BUT the Push Blob should continue to repel it, meaning that Newton's
		 * 3rd is pushing it in the negative-X direction. */
		assertFalse("Nobody should be dead", boringBlob.isDead());
		assertFalse("Nobody should be dead", pushBlob.isDead());
		assertEquals("Boring blob shouldn't move in Y-direction at all",
					 boringBlob.getPosition().y, 0.0);
		assertEquals("Anchor point shouldn't move at all",
				     new Coordinate(9.5, 0), anchorPoint.getPosition());
		assertSignificantlyGreaterThan("Boring blob should move", boringBlob.getPosition().x, 7.5);
		assertTrue("Boring blob shouldn't move after hitting the anchor",
				   boringBlob.getPosition().x < 8.6);
		assertTrue("Push blob should have moved more than boring blob did " +
				   "(its position was " + pushBlob.getPosition() + ") -- " +
				   " probably a Newton's 3rd failure",
				   boringBlob.getPosition().x - 7.5 < 3.0 - (pushBlob.getPosition().x));
		assertEquals("Push blob shouldn't move in Y-direction at all",
				     0.0, pushBlob.getPosition().y);
	}
	
	public void testPlayerForcesOverrideAnchor() {
		/* If you push your blob MANUALLY over an anchor point, it keeps
		 * moving. */
		LocalGameState state = BlobTestTools.getSingleBlobState(0, 0);
		Blob boringBlob = (Blob) state.getObjects().get(0);
		AnchorPoint anchorPoint = new AnchorPoint(1.2, 0);
		state.addObject(anchorPoint);
		
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>();
		finalPositions.put(boringBlob, new Coordinate(5.0, 0));
		
		GUIGameMove move = new GUIGameMove(finalPositions);
		state.executeMove(new GameMove(move));
		
		assertFalse("Boring blob should be alive", boringBlob.isDead());
		assertFalse("Anchor point should be alive", anchorPoint.isDead());
		assertEquals("Anchor point shouldn't move at all",
					 new Coordinate(1.2, 0), anchorPoint.getPosition());
		assertEquals("Boring blob shouldn't move in Y-direction",
					 0.0, boringBlob.getPosition().y);
		assertRoughlyEqual("Boring blob should reach its destination unhindered",
						   5.0, boringBlob.getPosition().x);
	}
}
