package edu.columbia.threescompany.client.communication;

import java.io.Serializable;

public class AuthenticationObject implements Serializable {

	private static final long serialVersionUID = -8794589749577853286L;

	private Boolean _isFirstConnection;
	private Object[] _players;
	
	public AuthenticationObject(Object[] players, Boolean isFirstConnection) {
		_players = players;
		_isFirstConnection = isFirstConnection;
	}

	public Object[] getPlayers() {
		return _players;
	}
	
	public boolean isFirstConnection() {
		return _isFirstConnection.booleanValue();
	}
}