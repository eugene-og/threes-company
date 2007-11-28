package edu.columbia.threescompany.game.tests;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.PhysicalMove;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.tests.BlobTestTools;
import edu.columbia.threescompany.tests.BaseTestCase;

public class PhysicalMoveTest extends BaseTestCase {
	public void testMove() {
		/* Assertion: Applying the same physical move DURATION times causes
		 * a blob to move by (dx, dy). */
		
		GameObject blob = BlobTestTools.getBoringBlob(0, 0);
		PhysicalMove move = new PhysicalMove(new Coordinate(2, 3), blob);
		
		for (int i = 0; i < move.getDuration(); i++)
			move.execute(null);
		
		assertRoughlyEqual("Blob should have moved to specified position",
				   2.0, blob.getPosition().x);
		assertRoughlyEqual("Blob should have moved to specified position",
				   3.0, blob.getPosition().y);
	}
}
