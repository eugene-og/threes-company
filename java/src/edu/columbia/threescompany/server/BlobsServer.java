package edu.columbia.threescompany.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;

import com.sun.corba.se.spi.activation.Server;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.client.communication.ExecuteMoveMessage;
import edu.columbia.threescompany.client.communication.MoveStatePair;
import edu.columbia.threescompany.client.communication.ServerMessage;
import edu.columbia.threescompany.client.communication.TurnChangeMessage;
import edu.columbia.threescompany.client.communication.UpdateStateMessage;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;

public class BlobsServer {
	public static String VERSION = "0.1";
	private static String _confFile = "conf" + File.separator + "BlobsServer.xml";
	
	// TODO hook into Eugene code
	private static List<PlayerServerData> _remotePlayers = null;
	
	public static void main(String args[])	{
		QuickServer blobsServer = new QuickServer();
		Object config[] = new Object[] { _confFile };
		if (blobsServer.initService(config) == true) {
			try {
				blobsServer.startServer();
				blobsServer.startQSAdminServer();
			} catch (AppException e) {
				System.err.println("Error in server : " + e);
				e.printStackTrace();
			}
		}
		
		LocalGameState gameState = new LocalGameState(playersFrom(_remotePlayers));
		sendStateToAllPlayers(gameState);
		
		int activePlayer = -1;
		while (!gameState.gameOver()) {
			activePlayer = (activePlayer + 1) % _remotePlayers.size();
			sendMessage(activePlayer, new TurnChangeMessage(activePlayer));

			MoveStatePair pair = receiveMoveAndState(activePlayer);
			sendMoveToAllPlayersExcept(activePlayer, pair._move);

			gameState = pair._state;
			sendStateToAllPlayers(gameState);
		}
	}
	
	private static List<Player> playersFrom(List<PlayerServerData> players) {
		List<Player> result = new ArrayList<Player>();
		for (PlayerServerData player : players)
			result.add(player.getPlayer());
		return result;
	}

	public static void sendStateToAllPlayers(LocalGameState gameState) {
		ServerMessage msg = new UpdateStateMessage(gameState);
		for (int i = 0; i < _remotePlayers.size(); i++)
			sendMessage(i, msg);
	}
	
	public static void sendMoveToAllPlayersExcept(int playerId, GameMove move) {
		ServerMessage msg = new ExecuteMoveMessage(move);
		for (int i = 0; i < _remotePlayers.size(); i++)
			if (i != playerId) sendMessage(i, msg);
	}
	
	public static MoveStatePair receiveMoveAndState(int playerId) {
		//TODO talk to Eugene -- we need to listen to the socket from
		// _remotePlayers.get(playerID) and receive a serialized
		// MoveStatePair
		return null;
	}
	
	public static void sendMessage(int playerID, ServerMessage msg) {
		//TODO talk to Eugene -- we need to serialize msg and send it to
		// _remotePlayers.get(playerID) 's network socket
	}
}