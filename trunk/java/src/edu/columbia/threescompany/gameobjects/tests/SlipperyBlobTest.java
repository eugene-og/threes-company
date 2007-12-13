package edu.columbia.threescompany.gameobjects.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.SlipperyBlob;
import edu.columbia.threescompany.gameobjects.SlipperySpot;
import edu.columbia.threescompany.tests.BaseTestCase;

public class SlipperyBlobTest extends BaseTestCase {
	public void testSpotCreation() {
		LocalGameState state = LocalGameState.getSpecifiedGameState(new ArrayList<GameObject>());
		SlipperyBlob slipperyBlob = new SlipperyBlob(3.0, 3.0, 1.0, Player.NULL_PLAYER, state);
		state.addObject(slipperyBlob);
		
		List<Blob> activation = Arrays.asList(new Blob[] { slipperyBlob });
		GUIGameMove move = new GUIGameMove(new HashMap<Blob, Coordinate>(), activation, new ArrayList<Blob>());
		
		state.executeMove(new GameMove(move));
		
		assertTrue("Slippery blob should be dead", slipperyBlob.isDead());
		List<GameObject> objects = state.getObjects();
		assertEquals("Should be a new object created", 2, objects.size());
		
		SlipperySpot spot = null;
		try {
			spot = (SlipperySpot) objects.get(1);
		} catch (ClassCastException e) {
			fail("Object should be a hole! Was: " + objects.get(1));
		}
		
		assertEquals("Position should be the same", slipperyBlob.getPosition(),
					 spot.getPosition());
		assertSignificantlyGreaterThan("Radius should be larger", spot.getRadius(),
									   slipperyBlob.getRadius());
	}
	
	public void testSpawning() {
		LocalGameState state = LocalGameState.getSpecifiedGameState(new ArrayList<GameObject>());
		SlipperyBlob slipperyBlob = new SlipperyBlob(0.0, 0.0, Player.NULL_PLAYER, state);
		state.addObject(slipperyBlob);
		
		/* Just checking for exceptions. */
		slipperyBlob.spawn();
	}
}
