package edu.columbia.threescompany.server;

import org.quickserver.net.server.ClientData;
import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.ClientIdentifiable;

import edu.columbia.threescompany.game.Player;

public class PlayerServerData implements ClientData, ClientIdentifiable {
	private static final long serialVersionUID = -2022551820772269910L;

	private ClientHandler _gameClientHandler;
	private ClientHandler _chatClientHandler;
	private String _hostAddress;

	private Player _player;
	
	private String _handle;
	
	private boolean _isReadyToPlay;
	private boolean _isSimulating;
	private boolean _hasTurn;

	public boolean isReadyToPlay() {
		return _isReadyToPlay;
	}

	public void setPlayer(Player player) {
		_player = player;
	}
	
	public Player getPlayer() {
		return _player;
	}
	
	public void setIsReadyToPlay(boolean isReady) {
		this._isReadyToPlay = isReady;
	}

	public ClientHandler getGameClientHandler() {
		return _gameClientHandler;
	}

	public void setGameClientHandler(ClientHandler client) {
		this._gameClientHandler = client;
	}

	public ClientHandler getChatClientHandler() {
		return _chatClientHandler;
	}

	public void setChatClientHandler(ClientHandler clientHandler) {
		_chatClientHandler = clientHandler;
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

	public String getHostAddress() {
		return _hostAddress;
	}

	public void setHostAddress(String address) {
		_hostAddress = address;
	}


}
