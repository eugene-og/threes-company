package edu.columbia.threescompany.client.communication;

import edu.columbia.threescompany.game.Player;

public class TurnChangeMessage extends ServerMessage {
	private static final long serialVersionUID = -3151972268456439697L;
	
	public TurnChangeMessage(Player player) {
		_player = player;
	}
	
	private Player _player;
	
	public Player whoseTurn() {
		return _player;
	}
}
