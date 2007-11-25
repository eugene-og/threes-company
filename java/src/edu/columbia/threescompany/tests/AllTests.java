package edu.columbia.threescompany.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.columbia.threescompany.common.tests.CoordinateTest;
import edu.columbia.threescompany.gameobjects.tests.BasicBlobTest;
import edu.columbia.threescompany.gameobjects.tests.DeathRayBlobTest;
import edu.columbia.threescompany.gameobjects.tests.ForceBlobTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Tests for the Blobs game");
		
		//$JUnit-BEGIN$
		suite.addTestSuite(CoordinateTest.class);
		suite.addTestSuite(ForceBlobTest.class);
		suite.addTestSuite(DeathRayBlobTest.class);
		suite.addTestSuite(ServerAuthenticationTest.class);
		suite.addTestSuite(BasicBlobTest.class);
		//$JUnit-END$
		
		return suite;
	}

}