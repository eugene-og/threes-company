package edu.columbia.threescompany.gameobjects.tests;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.gameobjects.DeathRayBlob;
import edu.columbia.threescompany.gameobjects.GameObject;
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
}
