package edu.columbia.threescompany.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;

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
	
	private static List<PlayerServerData> _players;
	
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
		
		_players = getPlayers();
		LocalGameState gameState = LocalGameState.getInitialGameState(playersFrom(_players));
		sendStateToAllPlayers(gameState);
		
		int activePlayer = -1;
		while (!gameState.gameOver()) {
			activePlayer = (activePlayer + 1) % _players.size();
			// TODO do this better -- Zach
			String playerId = _players.get(activePlayer).getClientId();
			sendMessage(playerId, new TurnChangeMessage(activePlayer));

			MoveStatePair pair = receiveMoveAndState(activePlayer);
			sendMoveToAllPlayersExcept(playerId, pair._move);

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
		List<PlayerServerData> players = getPlayers();
		for (PlayerServerData player : players) {
			sendMessage(player.getClientId(), msg);
		}
	}

	private static List<PlayerServerData> getPlayers() {
		return BlobsGameState.instance().getAllPlayers();
	}
	
	public static void sendMoveToAllPlayersExcept(String playerId, GameMove move) {
		ServerMessage msg = new ExecuteMoveMessage(move);
		List<PlayerServerData> players = getPlayers();
		for (PlayerServerData player : players) {
			String id = player.getClientId();
			if (!id.equals(playerId)) sendMessage(id, msg);
		}
	}
	
	public static MoveStatePair receiveMoveAndState(int playerId) {
		//TODO talk to Eugene -- we need to listen to the socket from
		// _remotePlayers.get(playerID) and receive a serialized
		// MoveStatePair
		return null;
	}
	
	public static void sendMessage(String playerID, ServerMessage msg) {
		BlobsGameState.instance().getPlayer(playerID);
		//TODO talk to Eugene -- we need to serialize msg and send it to
		// _remotePlayers.get(playerID) 's network socket
	}
}