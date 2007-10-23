package edu.columbia.threescompany.client.communication;

public class TurnChangeMessage extends ServerMessage {
	private int _playerID;
	
	public int whoseTurn() {
		return _playerID;
	}
}
