package edu.columbia.threescompany.client.communication;

import edu.columbia.threescompany.game.GameMove;

public class ExecuteMoveMessage extends ServerMessage {
	private GameMove _move;
	
	public ExecuteMoveMessage(GameMove move) {
		_move = move;
	}
	
	public GameMove getMove() {
		return _move;
	}
}
