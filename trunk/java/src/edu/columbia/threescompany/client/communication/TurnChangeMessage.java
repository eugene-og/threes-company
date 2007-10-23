package edu.columbia.threescompany.client.communication;

import edu.columbia.threescompany.client.ServerMessage;

public class TurnChangeMessage extends ServerMessage {
	private int _playerID;
	
	public int whoseTurn() {
		return _playerID;
	}
}
