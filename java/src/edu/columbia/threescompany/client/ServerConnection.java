package edu.columbia.threescompany.client;

import java.util.List;

import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.gameobjects.GameObject;

public class ServerConnection {
	public void sendMove(GameMove move) {
		// TODO Transmit a list of moves
	}

	public List<GameObject> receiveState() {
		// TODO Get the GameObject list from the server, deserialize and return
		// TODO We need to return some sort of transition between the two states
		//			in order to animate this
		return null;
	}

	public void sendPlayer(Player me) {
		// TODO Serialize & send the server your info
	}

	public boolean gameOver() {
		// TODO
		return false;
	}
	
	public void waitForGame() {
		// TODO how do we know when all players enter?
	}
}
