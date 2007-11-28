package edu.columbia.threescompany.gameobjects.tests;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.tests.BaseTestCase;

public class BasicBlobTest extends BaseTestCase {
	public void testGrowth() {
		GameObject blob = BlobTestTools.getBoringBlob(1, 1);
		double oldRad = blob.getRadius();
		blob.grow();
		assertTrue("Blob should be bigger", blob.getRadius() > oldRad);
	}
	
	public void testForce() {
		GameObject blob = BlobTestTools.getBoringBlob(1, 1);
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
		GameObject blob2 = blob.spawn();
		
		assertTrue("Blob1 should have small radius", blob.getRadius() < oldRad);
		assertTrue("Blob2 should have small radius", blob2.getRadius() < oldRad);
	}
	
	public void testCollidesWith() {
		Blob blob = BlobTestTools.getBoringBlob(0, 0);
		Blob blob2 = BlobTestTools.getBoringBlob(0.5, 0.5);
		
		assertTrue("Blobs should collide", blob.collidingWith(blob2));
		assertTrue("Blobs should collide", blob2.collidingWith(blob));
		assertFalse(blob.collidingWith(blob));
		assertFalse(blob2.collidingWith(blob2));
	}
}
