package edu.columbia.threescompany.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.quickserver.net.server.ClientData;

public class BlobsGameState {
	
	private static BlobsGameState gameState = null;
	private static int playerCount = 0;
	private HashMap<String, PlayerServerData> playerServerDataMap;
	
	private BlobsGameState() {
		playerServerDataMap = new HashMap<String, PlayerServerData>();
	}
	
	public static BlobsGameState instance() {
		if (gameState == null) {
			gameState = new BlobsGameState();
		}
		return gameState;
	}

	public void addPlayerServerData(String id, PlayerServerData player) {
		playerServerDataMap.put(id, player);
	}
	
	public PlayerServerData getPlayerServerData(String id) {
		return playerServerDataMap.get(id);
	}

	public List<PlayerServerData> getAllPlayers() {
		return new ArrayList<PlayerServerData>(playerServerDataMap.values());
	}
	
	public int getPlayerCount() {
		return playerCount;
	}
	
	public void increasePlayerCount() {
		playerCount++;
	}
	
	// TODO delete this and the things supporting only it
	public boolean allPlayersReady() {
		if (getPlayerCount() < 2) {
			return false;
		}
		for (PlayerServerData currentPlayer : getAllPlayers()) {
			if (!currentPlayer.isReadyToPlay()) {
				return false;
			}
		}
		return true;
	}

	public void removePlayerServerData(ClientData clientData) {
		playerServerDataMap.remove(((PlayerServerData)clientData).getHandle());
	}

	public boolean isHandleTaken(String username, String hostAddress) {
		for (PlayerServerData currentPlayer : getAllPlayers()) {
			if (currentPlayer.getHandle().equals(username) && !currentPlayer.getHostAddress().equals(hostAddress)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAlreadyAuthenticated(String name, String hostAddress) {
		PlayerServerData psd = getPlayerServerData(name);
		if (psd != null && psd.getHostAddress().equals(hostAddress))
			return true;
		return false;
	}
}
