package edu.columbia.threescompany.server;

import org.quickserver.net.server.ClientData;
import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.ClientIdentifiable;

public class PlayerServerData implements ClientData, ClientIdentifiable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2022551820772269910L;

	private ClientHandler _client;

	private String _handle;
	
	private boolean _isReadyToPlay;
	private boolean _isSimulating;
	private boolean _hasTurn;

	public boolean isReadyToPlay() {
		return _isReadyToPlay;
	}

	public void setIsReadyToPlay(boolean isReady) {
		this._isReadyToPlay = isReady;
	}

	public ClientHandler getClient() {
		return _client;
	}

	public void setClient(ClientHandler client) {
		this._client = client;
	}

	public String getHandle() {
		return _handle;
	}

	public void setHandle(String handle) {
		this._handle = handle;
	}

	public boolean isSimulating() {
		return _isSimulating;
	}

	public void setIsSimulating(boolean simulating) {
		_isSimulating = simulating;
	}

	public boolean hasTurn() {
		return _hasTurn;
	}

	public void setHasTurn() {
		if (BlobsGameState.instance().getPlayerCount() == 0) {
			_hasTurn = true;
		} else {
			_hasTurn = false;
		}
	}
	
	public void setHasTurn(boolean turn) {
		_hasTurn = turn;
	}

	public String getClientId() {
		return _handle;
	}

	public String getClientInfo() {
		return getClientId() + " - info";
	}

	public String getClientKey() {
		return getClientId() + " - key";
	}

}
