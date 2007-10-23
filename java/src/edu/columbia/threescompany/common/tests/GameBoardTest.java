package edu.columbia.threescompany.common.tests;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.common.GameBoard;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.tests.BaseTestCase;

public class GameBoardTest extends BaseTestCase {
	public void testBoardBoundaries() {
		assert(GameBoard.isOnBoard(
				new Coordinate(GameParameters.BOARD_SIZE / 2,
							   GameParameters.BOARD_SIZE / 2)));
	}
}
