package edu.columbia.threescompany.client.communication;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.GameMove;

public class ServerConnection {
	public ServerMessage receiveMessage() {
		/* Block until a message is received from the server, then return it.
		 * If the game has ended, return null. */
		
		return null;
	}

	public void sendMove(GameMove move, LocalGameState state) {
		// TODO Auto-generated method stub
		// The server needs to propagate this move to all clients, who
		// then animate it. Clients need to agree with "state", so we're
		// passing that too, also to be serialized.
	}
}
