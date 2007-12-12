package edu.columbia.threescompany.gameobjects.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.SlipperySpot;
import edu.columbia.threescompany.tests.BaseTestCase;

public class SlipperySpotTest extends BaseTestCase {
	public void testSlipperySpots() {
		/* Boring Blob B will be moved to position X; but it encounters
		 * slippery spot O and ends up at Y as a result of the reduced friction.
		 * --B------O------X--Y--
		 */
		
		LocalGameState state = BlobTestTools.getSingleBlobState(3.0, 0.0);
		Blob blob = (Blob) state.getObjects().get(0);
		SlipperySpot spot = new SlipperySpot(0.0, 0.0, 6.0);
		
		state.addObject(spot);
		
		Map<Blob, Coordinate> finalPositions = new HashMap<Blob, Coordinate>();
		finalPositions.put(blob, new Coordinate(9.0, 0.0));
		
		GUIGameMove move = new GUIGameMove(finalPositions, new ArrayList<Blob>(), new ArrayList<Blob>());
		
		state.executeMove(new GameMove(move));
		
		assertFalse("Blob should be alive", blob.isDead());
		assertEquals("Blob shouldn't have deviated from x-axis", 0.0, blob.getPosition().y);
		assertSignificantlyGreaterThan("Blob should have slid past target", blob.getPosition().x, 9.0);
	}
}
