package edu.columbia.threescompany.server;

import java.io.IOException;

import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.QuickAuthenticator;

import edu.columbia.threescompany.game.Player;

public class BlobsServerQuickAuthenticator extends QuickAuthenticator {
	public boolean askAuthorisation(ClientHandler clientHandler) throws IOException {
		BlobsGameState gameState = BlobsGameState.instance();
		Object[] players;
		
		try {
			players = (Object[]) askObjectInput(clientHandler, null);
		} catch (Exception e) {
			throw new RuntimeException("Error deserializing player list", e);
		}
		
		boolean retval = true;
		
		for (int i = 0; i < players.length; i++)
			retval &= authenticatePlayer((Player) players[i], clientHandler, gameState);
		
		return retval;
	}

	private boolean authenticatePlayer(Player player, ClientHandler clientHandler, BlobsGameState gameState) throws IOException {
		if(player.getName() == null) {
			sendString(clientHandler, "Auth Failed - Username empty");
			return false; 
		} else if (gameState.isHandleTaken(player.getName())) {
			sendString(clientHandler, "Auth Failed - Handle in use");
			return false;
		} else {
			sendString(clientHandler, "Auth OK");
			PlayerServerData newPlayer = (PlayerServerData) clientHandler.getClientData();
			newPlayer.setHandle(player.getName());
			newPlayer.setClient(clientHandler);
			newPlayer.setIsReadyToPlay(false);
			newPlayer.setHasTurn();
			
			Player clientPlayer = null;	// TODO
			newPlayer.setPlayer(clientPlayer);
			
			gameState.addPlayer(newPlayer.getClientId(), newPlayer);
			return true;
		}
	}
}