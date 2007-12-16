package edu.columbia.threescompany.graphics.tests;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.graphics.Board;
import edu.columbia.threescompany.graphics.GraphicalGameState;
import edu.columbia.threescompany.tests.BaseTestCase;

public class BoardTest extends BaseTestCase {

	private Board board;
	private double BOARD_SIZE = GameParameters.BOARD_SIZE;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		board = new Board(new GraphicalGameState());
	}
	
	private void assertScreenToWorldEquals(double screenX, double screenY, double worldX, double worldY) {
		Coordinate worldOutput = board.screenToWorld(new Coordinate(screenX, screenY));
		Coordinate correctWorld = new Coordinate(worldX, worldY);
		assertRoughlyEqual("Coordinates aren't even close", worldOutput.x, correctWorld.x);
		assertRoughlyEqual("Coordinates aren't even close", worldOutput.y, correctWorld.y);
	}
	
	private void assertWorldToScreenEquals(double worldX, double worldY, double screenX, double screenY) {
		Coordinate screenOutput = board.worldToScreen(new Coordinate(worldX, worldY));
//		System.out.printf("converted %f,%f to %f,%f\n", worldX, worldY, screenOutput.x, screenOutput.y);
		Coordinate correctScreen = new Coordinate(screenX, screenY);
		assertRoughlyEqual("Coordinates aren't even close", screenOutput.x, correctScreen.x);
		assertRoughlyEqual("Coordinates aren't even close", screenOutput.y, correctScreen.y);
	}
	
	public void testScreenToWorld() {
		assertScreenToWorldEquals(0, 0, 0, 0);
		assertScreenToWorldEquals(board.getWidth(), board.getWidth(), BOARD_SIZE, BOARD_SIZE);
		assertScreenToWorldEquals(board.getWidth(), 0, BOARD_SIZE, 0);
		assertScreenToWorldEquals(0, board.getHeight(), 0, BOARD_SIZE);
		// TODO test negatives
	}
	
	public void testWorldToScreeen() {
		assertWorldToScreenEquals(0, 0, 0, 0);
		assertWorldToScreenEquals(BOARD_SIZE, BOARD_SIZE, board.getWidth(), board.getWidth());
		assertWorldToScreenEquals(BOARD_SIZE, 0, board.getWidth(), 0);
		assertWorldToScreenEquals(0, BOARD_SIZE, 0, board.getHeight());		
	}
}
