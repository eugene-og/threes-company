package edu.columbia.threescompany.gameobjects.tests;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.ForceBlob;
import edu.columbia.threescompany.gameobjects.PullBlob;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.tests.BaseTestCase;

public class ForceBlobTest extends BaseTestCase {
	public void testPullForceOrientation() {
		ForceBlob actor = new PullBlob(0, 0, BlobTestTools.PLAYER);
		Blob victim = BlobTestTools.getBoringBlob(2, 0);
		
		Force force = actor.actOn(victim);
		assertPositive(force.x);
		assertZero(force.y);
		
		victim = BlobTestTools.getBoringBlob(-3, -3);
		force = actor.actOn(victim);
		
		assertNegative(force.x);
		assertNegative(force.y);
	}
	
	public void testPushForceOrientation() {
		PushBlob actor = new PushBlob(0, 0, BlobTestTools.PLAYER);	
		Blob victim = BlobTestTools.getBoringBlob(2, 0);
		
		Force force = actor.actOn(victim);
		assertNegative(force.x);
		assertZero(force.y);
		
		victim = BlobTestTools.getBoringBlob(-3, -3);
		force = actor.actOn(victim);
		
		assertPositive(force.x);
		assertPositive(force.y);
	}
}
