package edu.columbia.threescompany.client.communication;

import edu.columbia.threescompany.client.ServerMessage;

public class YourTurnMessage extends ServerMessage {
	private int _playerID;
	
	public int whoseTurn() {
		return _playerID;
	}
}