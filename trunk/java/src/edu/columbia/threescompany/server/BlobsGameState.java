package edu.columbia.threescompany.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.quickserver.net.server.ClientData;

public class BlobsGameState {
	
	private static BlobsGameState gameState = null;
	private HashMap<String, PlayerServerData> playerMap;
	
	private BlobsGameState() {
		playerMap = new HashMap<String, PlayerServerData>();
	}
	
	public static BlobsGameState instance() {
		if (gameState == null) {
			gameState = new BlobsGameState();
		}
		return gameState;
	}

	public void addPlayer(String id, PlayerServerData player) {
		playerMap.put(id, player);
	}
	
	public PlayerServerData getPlayer(String id) {
		return (PlayerServerData) playerMap.get(id);
	}

	public List<PlayerServerData> getAllPlayers() {
		return new ArrayList<PlayerServerData>(playerMap.values());
	}
	
	public int getPlayerCount() {
		return playerMap.size();
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

	public void removePlayer(ClientData clientData) {
		playerMap.remove(((PlayerServerData)clientData).getHandle());
	}

	public boolean isHandleTaken(String username) {
		for (Iterator<PlayerServerData> iterator = getAllPlayers().iterator(); iterator.hasNext();) {
			PlayerServerData currentPlayer = (PlayerServerData) iterator.next();
			if (currentPlayer.getHandle().equals(username)) {
				return true;
			}
		}
		return false;
	}


}
