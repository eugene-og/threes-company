package edu.columbia.threescompany.gameobjects.tests;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.DeathRayBlob;
import edu.columbia.threescompany.tests.BaseTestCase;

public class DeathRayBlobTest extends BaseTestCase {
	public void testAngleChecking() {
		DeathRayBlob death = new DeathRayBlob(0, -1, 1, BlobTestTools.PLAYER);
		Blob victim = BlobTestTools.getBoringBlob(0, 2);
		/* If this isn't reached by the death ray, we're in trouble */
		
		death.applyForce(new Force(0, 1));
		death.actOn(victim);
		assertTrue("Death ray should have hit victim", victim.isDead());
		
		death = new DeathRayBlob(1, 1, 1, BlobTestTools.PLAYER);
		victim = BlobTestTools.getBoringBlob(-1, -1);
		death.applyForce(new Force(-1, -1));
		death.actOn(victim);
		assertTrue("Death ray should have hit victim", victim.isDead());
	}
}
