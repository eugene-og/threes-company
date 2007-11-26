package edu.columbia.threescompany.client.communication;

import java.io.Serializable;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.game.GameMove;

public class MoveStatePair implements Serializable {
	private static final long serialVersionUID = -1917229923350700923L;
	public MoveStatePair(GameMove move, LocalGameState state) {
		_move = move;
		_state = state;
	}
	
	public LocalGameState _state;
	public GameMove _move;
}