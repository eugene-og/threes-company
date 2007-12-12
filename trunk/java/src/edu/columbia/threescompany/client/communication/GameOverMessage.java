package edu.columbia.threescompany.client.communication;

import edu.columbia.threescompany.game.Player;

public class GameOverMessage extends ServerMessage {
	private static final long serialVersionUID = 673125466540089787L;
	
	public GameOverMessage(Player winner) {
		_winner = winner;
	}
	
	public Player getWinner() {
		return _winner;
	}
	
	private Player _winner;
}
