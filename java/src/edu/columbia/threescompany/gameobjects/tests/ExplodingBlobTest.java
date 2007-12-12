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
import edu.columbia.threescompany.gameobjects.ExplodingBlob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.Hole;
import edu.columbia.threescompany.tests.BaseTestCase;

public class ExplodingBlobTest extends BaseTestCase {
	public void testHoleCreation() {
		LocalGameState state = LocalGameState.getSpecifiedGameState(new ArrayList<GameObject>());
		ExplodingBlob explodingBlob = new ExplodingBlob(3.0, 3.0, 1.0, Player.NULL_PLAYER, state);
		state.addObject(explodingBlob);
		
		List<Blob> activation = Arrays.asList(new Blob[] { explodingBlob });
		GUIGameMove move = new GUIGameMove(new HashMap<Blob, Coordinate>(), activation, new ArrayList<Blob>());
		
		state.executeMove(new GameMove(move));
		
		assertTrue("Exploding blob should be dead", explodingBlob.isDead());
		List<GameObject> objects = state.getObjects();
		assertEquals("Should be a new object created", 2, objects.size());
		
		Hole hole = null;
		try {
			hole = (Hole) objects.get(1);
		} catch (ClassCastException e) {
			fail("Object should be a hole! Was: " + objects.get(1));
		}
		
		assertEquals("Position should be the same", explodingBlob.getPosition(),
					 hole.getPosition());
		assertSignificantlyGreaterThan("Radius should be larger", hole.getRadius(),
									   explodingBlob.getRadius());
	}
}
