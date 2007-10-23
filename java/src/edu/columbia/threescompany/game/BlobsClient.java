package edu.columbia.threescompany.game;

import java.util.LinkedList;
import java.util.List;

import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.graphics.Gui;
import edu.columbia.threescompany.players.Player;

public class BlobsClient implements Runnable {
	protected int turn;
	protected List<Player> players;
	private Gui _gui = new Gui();
	public List<GameObject> _blobs = new LinkedList<GameObject>();
	
	public BlobsClient(List<Player> players) {
		this.turn = 0;
		this.players = players;
	}
	
	public void runGame() {
		// Event processing loop / Game loop
		while(!gameOver()) {
			// Wake up twice a second and see if any players have anything for us to do.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			// If we think this is too ugly, we can wait() and have the player wake us up if it has something to do.
			
			// This loop does not process players by turn. It is needed so any player can chat. The turn is explicitly
			// tracked in turn.
			for (Player player : this.players) {
				GameMove move = player.getMove();
				if (move != null) {
					processEvent(player, move);
				}
			}
			_gui.drawState(_blobs);
		}
	}

	/**
	 * @param player The player that generated the event.
	 */
	protected void processEvent(Player player, GameMove move) {
		// if move.type == CHAT
			// send to all players
		// else if move.type == MOVE
			// if it is this player's turn
				player.disableInput();
				// process simulation
				for (BlobMove blobMove : move.getBlobMoves()) {
					/* Possibile BlobMoves:
					 * Split
					 * Move
					 * Activate force
					 * Death ray
					 * Explode
					 * Activate slipperize
					 * Fill hole
					 */
					// update _blobs to start these actions
					// while (!moveDone()) {
						// stepSimulation(...);
						// gui.render(_blobs)
					// }
				}
				turn = (turn + 1) % 2;
				players.get(turn).enableInput();
	}

	protected boolean gameOver() {
		return false; // we will increase market share by never allowing games to end
	}

	@Override
	public void run() {
		runGame();
	}
}
