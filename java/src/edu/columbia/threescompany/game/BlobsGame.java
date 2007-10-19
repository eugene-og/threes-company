package edu.columbia.threescompany.game;

import java.util.LinkedList;
import java.util.List;

import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.graphics.Gui;

public class BlobsGame {
	private Gui gui;
	
	public BlobsGame() {
		blobs = new LinkedList<GameObject>();
		gui = new Gui();
	}
	
	public void runGame() {
		// TODO get player information
		List<Player> players = null;
		// TODO start up graphics engine
		// TODO generate blobs
		while (true) { //!gameOver()) {
			gui.drawState(blobs);
			// TODO redraw board
			// TODO get a move from somebody
			// TODO act on it
		}		
	}
	
	public static void main(String[] args) {
		BlobsGame game = new BlobsGame();
		game.runGame();
	}
	
	private static boolean gameOver() {
		if (blobs.size() == 0) return true;
		Player firstPlayer = blobs.get(0).getOwner();
		
		/* Do multiple players have blobs on the board? */
		for (GameObject obj : blobs) if (obj.getOwner().id != Player.NOBODY &&
										 !obj.isDead() &&
										 !obj.getOwner().equals(firstPlayer))
			return false;
		
		/* Nope, somebody won. */
		return true;
	}

	public static List<GameObject> blobs;
}