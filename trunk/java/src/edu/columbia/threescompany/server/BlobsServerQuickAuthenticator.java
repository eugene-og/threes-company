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
			sendString(clientHandler, "Auth Failed - Username empty");
			retval = false;
		} else if (gameState.isAlreadyAuthenticated(player.getName(), clientHandler.getHostAddress())) {
			retval = true;
		} else if (gameState.isHandleTaken(player.getName(), clientHandler.getHostAddress())) {
			sendString(clientHandler, "Auth Failed - Handle in use");
			retval = false;
		} else {
			sendString(clientHandler, "Auth OK");
			clientHandler.setDataMode(DataMode.STRING, DataType.IN); //set String data mode for this connection
			PlayerServerData newPlayer = (PlayerServerData) clientHandler.getClientData();
			newPlayer.setHandle(player.getName());
			newPlayer.setHostAddress(clientHandler.getHostAddress());
			newPlayer.setClient(clientHandler);
			newPlayer.setIsReadyToPlay(false);
			newPlayer.setHasTurn();
			
			Player clientPlayer = null;	// TODO
			newPlayer.setPlayer(clientPlayer);
			
			gameState.addPlayerServerData(newPlayer.getClientId(), newPlayer);
			retval = true;
		}
		return retval;
	}
}