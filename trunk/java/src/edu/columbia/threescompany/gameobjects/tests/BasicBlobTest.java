package edu.columbia.threescompany.gameobjects.tests;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.tests.BaseTestCase;

public class BasicBlobTest extends BaseTestCase {
	public void testGrowth() {
		Blob blob = BlobTestTools.getBoringBlob(1, 1);
		double oldRad = blob.getRadius();
		blob.grow();
		assertTrue("Blob should be bigger", blob.getRadius() > oldRad);
	}
	
	public void testForce() {
		Blob blob = BlobTestTools.getBoringBlob(1, 1);
		Force xForce = new Force(2, 0);
		Force yForce = new Force(0, 2);
		
		blob.applyForce(xForce);
		assertTrue("Blob should have moved right", blob.getPosition().x > 1);
		assertTrue("Blob should not have moved vertically", blob.getPosition().y == 1);
		
		blob.applyForce(yForce);
		assertTrue("Blob should still be at right", blob.getPosition().x > 1);
		assertTrue("Blob should have moved up", blob.getPosition().y > 1);
	}
	
	public void testSpawn() {
		Blob blob = BlobTestTools.getBoringBlob(1, 1);
		double oldRad = blob.getRadius();
		Blob blob2 = blob.spawn();
		
		assertTrue("Blob1 should have small radius", blob.getRadius() < oldRad);
		assertTrue("Blob2 should have small radius", blob2.getRadius() < oldRad);
	}
}
