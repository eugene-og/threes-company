package edu.columbia.threescompany.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.quickserver.net.server.ClientData;

public class BlobsGameState {
	
	private static BlobsGameState gameState = null;
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
		return (PlayerServerData) playerServerDataMap.get(id);
	}

	public List<PlayerServerData> getAllPlayers() {
		return new ArrayList<PlayerServerData>(playerServerDataMap.values());
	}
	
	public int getPlayerCount() {
		return playerServerDataMap.size();
	}
	
	public boolean allPlayersReady() {
		if (getPlayerCount() < 2) {
			return false;
		}
		for (Iterator<PlayerServerData> iterator = getAllPlayers().iterator(); iterator.hasNext();) {
			PlayerServerData currentPlayer = (PlayerServerData) iterator.next();
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
		for (Iterator<PlayerServerData> iterator = getAllPlayers().iterator(); iterator.hasNext();) {
			PlayerServerData currentPlayer = (PlayerServerData) iterator.next();
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
