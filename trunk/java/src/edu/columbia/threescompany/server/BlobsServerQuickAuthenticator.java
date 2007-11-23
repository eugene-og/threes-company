package edu.columbia.threescompany.server;

import java.io.IOException;

import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.DataMode;
import org.quickserver.net.server.DataType;
import org.quickserver.net.server.QuickAuthenticator;

import edu.columbia.threescompany.game.Player;

public class BlobsServerQuickAuthenticator extends QuickAuthenticator {
	public boolean askAuthorisation(ClientHandler clientHandler) throws IOException {
		BlobsGameState gameState = BlobsGameState.instance();
		Object[] players;
		clientHandler.setDataMode(DataMode.OBJECT, DataType.IN);
		try {
			players = (Object[]) askObjectInput(clientHandler, null);
		} catch (Exception e) {
			throw new RuntimeException("Error deserializing player list", e);
		}
		
		boolean retval = true;
		
		for (int i = 0; i < players.length; i++) {
			retval &= authenticatePlayer((Player) players[i], clientHandler, gameState);
		}
		
		return retval;
	}

	private boolean authenticatePlayer(Player player, ClientHandler clientHandler, BlobsGameState gameState) throws IOException {
		boolean retval = false;
		if(player.getName() == null) {
			clientHandler.setDataMode(DataMode.STRING, DataType.IN); //set String data mode for this connection
			clientHandler.setDataMode(DataMode.STRING, DataType.OUT);
			sendString(clientHandler, "Auth Failed - Username empty");
			retval = false;
		} else if (gameState.isAlreadyAuthenticated(player.getName(), clientHandler.getHostAddress())) {
			//this is the 2nd connection for the main game loop
			clientHandler.setDataMode(DataMode.OBJECT, DataType.IN);
			clientHandler.setDataMode(DataMode.OBJECT, DataType.OUT);
			PlayerServerData playerServerData = gameState.getPlayerServerData(player.getName());
			playerServerData.setGameClientHandler(clientHandler);
			playerServerData.setIsReadyToPlay(false);
			playerServerData.setHasTurn();
			
			Player clientPlayer = null;	// TODO
			playerServerData.setPlayer(clientPlayer);
			
			retval = true;
		} else if (gameState.isHandleTaken(player.getName(), clientHandler.getHostAddress())) {
			clientHandler.setDataMode(DataMode.STRING, DataType.IN); //set String data mode for this connection
			clientHandler.setDataMode(DataMode.STRING, DataType.OUT);
			sendString(clientHandler, "Auth Failed - Handle in use");
			retval = false;
		} else {
			//this is the 1st connection from ChatThread
			clientHandler.setDataMode(DataMode.STRING, DataType.IN); //set String data mode for this connection
			clientHandler.setDataMode(DataMode.STRING, DataType.OUT);
			sendString(clientHandler, "Auth OK");
			PlayerServerData newPlayerServerData = (PlayerServerData) clientHandler.getClientData();
			newPlayerServerData.setHandle(player.getName());
			newPlayerServerData.setHostAddress(clientHandler.getHostAddress());
			newPlayerServerData.setChatClientHandler(clientHandler);
			gameState.addPlayerServerData(newPlayerServerData.getClientId(), newPlayerServerData);
			retval = true;
		}
		return retval;
	}
}