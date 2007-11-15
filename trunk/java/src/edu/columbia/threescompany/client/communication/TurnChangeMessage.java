package edu.columbia.threescompany.client.communication;

public class TurnChangeMessage extends ServerMessage {
	private static final long serialVersionUID = -3151972268456439697L;
	
	public TurnChangeMessage(String playerId) {
		_playerID = playerId;
	}
	
	private String _playerID;
	
	public String whoseTurn() {
		return _playerID;
	}
}
