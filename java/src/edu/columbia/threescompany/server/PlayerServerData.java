package edu.columbia.threescompany.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quickserver.net.server.ClientData;
import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.ClientIdentifiable;

import edu.columbia.threescompany.game.Player;

public class PlayerServerData implements ClientData, ClientIdentifiable {
	private static final long serialVersionUID = -2022551820772269910L;

	private ClientHandler _gameClientHandler;
	private ClientHandler _chatClientHandler;
	private String _hostAddress;

	private Map<String, Player> _players = new HashMap<String, Player>();
	
	private boolean _isReadyToPlay;

	public boolean isReadyToPlay() {
		return _isReadyToPlay;
	}

	public void addPlayer(Player player) {
		_players.put(player.getName(), player);
	}
	
	public void removePlayer(String handle) {
		_players.remove(handle);
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

	public String getHandles() {
		return getClientId();
	}
	
	public List<String> getHandlesList() {
		return new ArrayList<String>(_players.keySet());
	}
	
	public String getClientId() {
		String result = "";
		for (String key : _players.keySet()) {
			result +=key + ",";
		}
		return result.substring(0, result.length()-1);
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

	public Collection<Player> getPlayers() {
		return _players.values();
	}


}
