package edu.columbia.threescompany.game;

import java.util.LinkedList;
import java.util.List;

import edu.columbia.threescompany.gameobjects.GameObject;

public class BlobsGame {
	public BlobsGame() {
		blobs = new LinkedList<GameObject>();
	}
	
	public static void main(String[] args) {
		// TODO get player information
		List<Player> players = null;
		// TODO start up graphics engine
		// TODO generate blobs
		while (!gameOver()) {
			// TODO redraw board
			// TODO get a move from somebody
			// TODO act on it
		}
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