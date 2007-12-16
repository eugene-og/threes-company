package edu.columbia.threescompany.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.columbia.threescompany.client.tests.LocalGameStateTest;
import edu.columbia.threescompany.common.tests.CoordinateTest;
import edu.columbia.threescompany.common.tests.GameBoardTest;
import edu.columbia.threescompany.game.FillTest;
import edu.columbia.threescompany.game.tests.ActionPointCapIncreaserTest;
import edu.columbia.threescompany.game.tests.PhysicalMoveTest;
import edu.columbia.threescompany.gameobjects.tests.AnchorPointTest;
import edu.columbia.threescompany.gameobjects.tests.BasicBlobTest;
import edu.columbia.threescompany.gameobjects.tests.DeathRayBlobTest;
import edu.columbia.threescompany.gameobjects.tests.ExplodingBlobTest;
import edu.columbia.threescompany.gameobjects.tests.ForceBlobTest;
import edu.columbia.threescompany.gameobjects.tests.SlipperyBlobTest;
import edu.columbia.threescompany.gameobjects.tests.SlipperySpotTest;
import edu.columbia.threescompany.gameobjects.tests.SpringTest;
import edu.columbia.threescompany.gameobjects.tests.WallTest;
import edu.columbia.threescompany.graphics.tests.BoardTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Tests for the Blobs game");
		
		//$JUnit-BEGIN$
		
		/* Basic game mechanics */
		suite.addTestSuite(CoordinateTest.class);
		suite.addTestSuite(GameBoardTest.class);
		
		/* Blobs */
		suite.addTestSuite(BasicBlobTest.class);
		suite.addTestSuite(ForceBlobTest.class);
		suite.addTestSuite(ExplodingBlobTest.class);
		suite.addTestSuite(DeathRayBlobTest.class);
		suite.addTestSuite(SlipperyBlobTest.class);
		
		/* Fancy objects */
//		suite.addTestSuite(WallTest.class);
//		suite.addTestSuite(SpringTest.class);
		
		suite.addTestSuite(ActionPointCapIncreaserTest.class);
		suite.addTestSuite(AnchorPointTest.class);
		suite.addTestSuite(SlipperySpotTest.class);
		suite.addTestSuite(FillTest.class);
		
		/* Simulation */
		suite.addTestSuite(PhysicalMoveTest.class);
		suite.addTestSuite(LocalGameStateTest.class);
		
		/* Standalone functionality */
		suite.addTestSuite(ServerAuthenticationTest.class);

		/* Graphical functionality */
		suite.addTestSuite(BoardTest.class);
		
		//$JUnit-END$
		
		return suite;
	}

}
