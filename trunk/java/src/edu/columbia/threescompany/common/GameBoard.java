package edu.columbia.threescompany.common;

import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;

public class GameBoard {
	public static double BOARD_HEIGHT = GameParameters.BOARD_SIZE;
	public static double BOARD_WIDTH = GameParameters.BOARD_SIZE;
	
	public static boolean isOnBoard(Coordinate c) {
		return (c.x > 0 && c.x < BOARD_WIDTH &&
				c.y > 0 && c.y < BOARD_HEIGHT);
	}
	
	public static boolean isOnBoard(GameObject obj) {
		return (isOnBoard(obj.getPosition()));
	}
}
