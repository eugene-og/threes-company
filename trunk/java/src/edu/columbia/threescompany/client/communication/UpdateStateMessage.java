package edu.columbia.threescompany.client.communication;

import edu.columbia.threescompany.client.LocalGameState;

public class UpdateStateMessage extends ServerMessage {
	private static final long serialVersionUID = 3625858331947584360L;
	
	private LocalGameState _newGameState;
	
	public UpdateStateMessage(LocalGameState newGameState) {
		_newGameState = newGameState;
	}
	
	public LocalGameState getGameState() {
		return _newGameState;
	}
}
