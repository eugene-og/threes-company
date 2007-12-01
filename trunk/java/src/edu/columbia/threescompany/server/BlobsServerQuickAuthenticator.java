package edu.columbia.threescompany.server;

import java.io.IOException;

import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.DataMode;
import org.quickserver.net.server.DataType;
import org.quickserver.net.server.QuickAuthenticator;

import edu.columbia.threescompany.client.communication.AuthenticationObject;
import edu.columbia.threescompany.game.Player;

public class BlobsServerQuickAuthenticator extends QuickAuthenticator {
	private String _errorMsg;
	
	public boolean askAuthorisation(ClientHandler clientHandler) throws IOException {
		BlobsGameState gameState = BlobsGameState.instance();
		AuthenticationObject ao;
		clientHandler.setDataMode(DataMode.OBJECT, DataType.IN);
		try {
			ao = (AuthenticationObject) askObjectInput(clientHandler, null);
		} catch (Exception e) {
			throw new RuntimeException("Error deserializing player list", e);
		}
		
		Object[] players = (Object[])ao.getPlayers();
		boolean isFirstConnection = ao.isFirstConnection();
		
		if (!isFirstConnection) {
			updatePlayerServerData(players, clientHandler, gameState);
			return true;
		}
		
		boolean retval = true;
		_errorMsg = "Auth Failed";
		
		if (players.length == 2 && (((Player) players[0]).getName().equals(((Player) players[1]).getName()))) {
			_errorMsg = "Auth Failed - Duplicate usernames in Hotseat mode";
			retval = false;
		} else {
			for (int i = 0; i < players.length; i++) {
				retval &= authenticatePlayer((Player)players[i], clientHandler, gameState);
			}
		}
		
		if (retval == true) {
			sendAuthenticationMessage(clientHandler, "Auth OK");
		} else {
			sendAuthenticationMessage(clientHandler, _errorMsg);
		}
		
		return retval;
	}

	private void sendAuthenticationMessage(ClientHandler clientHandler,
			String message) throws IOException {
		clientHandler.setDataMode(DataMode.STRING, DataType.IN); //set String data mode for this connection
		clientHandler.setDataMode(DataMode.STRING, DataType.OUT);
		sendString(clientHandler, message);	
	}

	private void updatePlayerServerData(Object[] players, ClientHandler clientHandler, BlobsGameState gameState) throws IOException {
		for (int i = 0; i < players.length; i++) {
			Player player = (Player)players[i];
			clientHandler.setDataMode(DataMode.OBJECT, DataType.IN);
			clientHandler.setDataMode(DataMode.OBJECT, DataType.OUT);
			PlayerServerData playerServerData = gameState.getPlayerServerData(player.getName());
			playerServerData.setGameClientHandler(clientHandler);
			playerServerData.setIsReadyToPlay(false);
			gameState.increasePlayerCount();
		}
	}

	private boolean authenticatePlayer(Player player, ClientHandler clientHandler, BlobsGameState gameState) throws IOException {
		boolean retval = false;
		if(player.getName() == null || player.getName().equals("")) {
			_errorMsg = "Auth Failed - Username empty";
			retval = false;		
		} else if (gameState.isHandleTaken(player.getName(), clientHandler.getHostAddress())) {
			_errorMsg = "Auth Failed - Handle in use";
			retval = false;
		} else {
			//this is the 1st connection from ChatThread
			PlayerServerData newPlayerServerData = (PlayerServerData) clientHandler.getClientData();
			newPlayerServerData.setHostAddress(clientHandler.getHostAddress());
			newPlayerServerData.setChatClientHandler(clientHandler);
			newPlayerServerData.addPlayer(player);
			gameState.addPlayerServerData(player.getName(), newPlayerServerData);
			retval = true;
		}
		return retval;
	}
}