package edu.columbia.threescompany.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.quickserver.net.server.ClientData;

import edu.columbia.threescompany.game.Player;

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

	public void addPlayerServerData(String playerName, PlayerServerData playerServerData) {
		playerServerDataMap.put(playerName, playerServerData);
	}
	
	public PlayerServerData getPlayerServerData(String playerName) {
		return playerServerDataMap.get(playerName);
	}

	public Collection<PlayerServerData> getAllPlayerServerData() {
		return new HashSet<PlayerServerData>(playerServerDataMap.values());
	}
	
	public List<Player> getAllPlayers() {
		List <Player> playersList = new ArrayList<Player>();
		for (PlayerServerData psd : playerServerDataMap.values()) {
			playersList.addAll(psd.getPlayers());
		}
		return playersList;
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
		for (PlayerServerData currentPlayer : getAllPlayerServerData()) {
			if (!currentPlayer.isReadyToPlay()) {
				return false;
			}
		}
		return true;
	}

	public void removePlayerServerData(ClientData clientData) {
		playerServerDataMap.remove(((PlayerServerData)clientData).getHandles());
	}

	public boolean isHandleTaken(String username, String hostAddress) {
		for (PlayerServerData currentPsd : getAllPlayerServerData()) {
			if (currentPsd.getHandlesList().contains(username) && !currentPsd.getHostAddress().equals(hostAddress)) {
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
