package edu.columbia.threescompany.client.communication;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.GameMove;

public class MoveStatePair {
	public MoveStatePair(GameMove move, LocalGameState state) {
		_move = move;
		_state = state;
	}
	
	public LocalGameState _state;
	public GameMove _move;
}