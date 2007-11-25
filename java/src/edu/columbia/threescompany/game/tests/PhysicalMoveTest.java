package edu.columbia.threescompany.game.tests;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.PhysicalMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.gameobjects.tests.BlobTestTools;
import edu.columbia.threescompany.tests.BaseTestCase;

public class PhysicalMoveTest extends BaseTestCase {
	public void testMove() {
		/* Assertion: Applying the same physical move GRANULARITY times causes
		 * a blob to move by (dx, dy). */
		
		Blob blob = BlobTestTools.getBoringBlob(0, 0);
		PhysicalMove move = new PhysicalMove(new Coordinate(2, 3), blob);
		
		for (int i = 0; i < GameParameters.GRANULARITY_OF_PHYSICS; i++)
			move.execute();
		
		assertRoughlyEqual("Blob should have moved to specified position",
				   2.0, blob.getPosition().x);
		assertRoughlyEqual("Blob should have moved to specified position",
				   3.0, blob.getPosition().y);
	}
}
