package edu.columbia.threescompany.game.tests;

import java.util.HashMap;
import java.util.Map;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.APCIPoint;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.tests.BlobTestTools;
import edu.columbia.threescompany.tests.BaseTestCase;

public class ActionPointCapIncreaserTest extends BaseTestCase {
	public void testCapIncreaser() {
		LocalGameState state = BlobTestTools.getSingleBlobState(3, 3);
		APCIPoint point = new APCIPoint(4, 5);
		
		Blob blob = (Blob) state.getObjects().get(0);
		state.addObject(point);
		
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>();
		finalPositions.put(blob, point.getPosition());
		
		GUIGameMove move = new GUIGameMove(finalPositions);
		state.executeMove(new GameMove(move));
		
		assertFalse("Nobody should die", blob.isDead());
		System.out.println("Player has " + BlobTestTools.PLAYER.getActionPoints() + " AP");
		assertTrue("Should have extra AP", BlobTestTools.PLAYER.getActionPoints() > 10);
	}
}
