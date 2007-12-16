package edu.columbia.threescompany.gameobjects.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.DeathRayBlob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.tests.BaseTestCase;

public class DeathRayBlobTest extends BaseTestCase {
	public void testAngleChecking() {
		DeathRayBlob death = new DeathRayBlob(1, 1, 3, BlobTestTools.PLAYER);
		GameObject victim = BlobTestTools.getBoringBlob(1, 3.1);
		
		death.setTheta(new Coordinate(1, 2));
		death.activate(true);
		death.actOn(victim);
		assertTrue("Death ray should have hit victim", victim.isDead());
		
		death = new DeathRayBlob(1, 1, 3, BlobTestTools.PLAYER);
		victim = BlobTestTools.getBoringBlob(3, 3);
		death.setTheta(new Coordinate(0, 1));
		death.activate(true);
		death.actOn(victim);
		
		assertFalse("Death ray shouldn't have hit victim", victim.isDead());
	}
	
	public void testACaseThatActuallyFailed() {
		/* IF THIS FAILS, YOUR SCALING PARAMETERS TRULY SUCK */
		DeathRayBlob death = new DeathRayBlob(3.54256, 8.41652, 1.5, BlobTestTools.PLAYER);
		Blob victim = new PushBlob(4.0, 5.0, 1.5, BlobTestTools.PLAYER);
		
		death.setTheta(new Coordinate(3.54256, 9.41652));
		death.activate(true);
		death.actOn(victim);
		
		assertTrue("Death ray should have hit victim", victim.isDead());
	}
	
	public void testThetaOrientation() {
		DeathRayBlob death = new DeathRayBlob(1.0, 1.0, 1.0, BlobTestTools.PLAYER);
		death.applyIrresistibleForce(Force.newRawForce(1.0, 1.0));
		
		assertEquals("Should be 45 deg angle", death.getTheta(), 45.0);
		
		LocalGameState state = LocalGameState.getSpecifiedGameState(Arrays.asList(new GameObject[] { death }));
		
		death.applyIrresistibleForce(Force.newRawForce(-1.0, -1.0));
		
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>(1);
		finalPositions.put(death, new Coordinate(4, 3));
		
		GUIGameMove move = new GUIGameMove(finalPositions);
		
		state.executeMove(new GameMove(move));
		
		assertRoughlyEqual("Theta should be atan2(2, 3)", Math.toDegrees(Math.atan2(2, 3)), death.getTheta());
	}
}
