package edu.columbia.threescompany.client.communication;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.GameMove;

public class ExecuteMoveMessage extends ServerMessage {
	private static final long serialVersionUID = -6413509777790004968L;
	
	private GameMove _move;
	private LocalGameState _initialState;
	
	public ExecuteMoveMessage(GameMove move, LocalGameState initialState) {
		_move = move;
		_initialState = initialState;
	}
	
	public GameMove getMove() {
		return _move;
	}
	
	public LocalGameState getInitialState() {
		return _initialState;
	}
}
