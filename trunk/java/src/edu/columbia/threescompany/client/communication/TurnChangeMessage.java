package edu.columbia.threescompany.client.communication;

public class TurnChangeMessage extends ServerMessage {
	private static final long serialVersionUID = -3151972268456439697L;
	
	public TurnChangeMessage(int id) {
		_playerID = id;
	}
	
	private int _playerID;
	
	public int whoseTurn() {
		return _playerID;
	}
}
