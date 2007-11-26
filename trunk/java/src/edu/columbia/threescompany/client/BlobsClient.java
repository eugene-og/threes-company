package edu.columbia.threescompany.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;
import javax.swing.JOptionPane;

import edu.columbia.threescompany.client.communication.ExecuteMoveMessage;
import edu.columbia.threescompany.client.communication.ServerConnection;
import edu.columbia.threescompany.client.communication.ServerMessage;
import edu.columbia.threescompany.client.communication.UpdateStateMessage;
import edu.columbia.threescompany.client.communication.TurnChangeMessage;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.graphics.Gui;
import edu.columbia.threescompany.graphics.PlayerInfoGui;
import edu.columbia.threescompany.graphics.PreGameGui;

public class BlobsClient {
	private static LocalGameState _gameState;
	private static ServerConnection _serverConnection;
	private static ChatThread _chatThread;
	private static Gui _gui;
	private static List<Player> _players;
	
	public static void main(String[] args) throws Exception {
		doPlayerSetup();
		
		Boolean connected = false;
		while (!connected) {
			try {
				_chatThread = new ChatThread(_players);
				connected = true;
			} catch (ConnectException e) {
				JOptionPane.showMessageDialog(null, "Blobs could not connect to the server. You need a server running " +
				                              "even for a hotseat game.");
				doPlayerSetup();
			}
		}
		_gui = Gui.getInstance(_chatThread);
		_chatThread.setGui(_gui);
		_chatThread.start();

		connectToServer(args);
		
		ServerMessage message;
		while ((message = _serverConnection.receiveMessage()) != null)
			handleMessage(message);
		
		gameOverDialog();
	}

	private static void connectToServer(String[] args) throws UnknownHostException, IOException {
		if (args.length == 2)
			_serverConnection = connectFromHostAndPort(args[0], args[1]);
		else if (args.length == 1)
			_serverConnection = connectFromHost(args[0]);
		else
			_serverConnection = new ServerConnection();

		_serverConnection.sendPlayers(_players);
	}

	private static ServerConnection connectFromHost(String host)
	throws IOException {
		return new ServerConnection(host);
	}

	private static ServerConnection connectFromHostAndPort(String host, String port)
	throws IOException {
		return new ServerConnection(host, Integer.valueOf(port));
	}

	private static void gameOverDialog() {
		JOptionPane.showMessageDialog(null, "You didn't lose, you just weren't the winner", 
											"Game over",
											JOptionPane.INFORMATION_MESSAGE );
	}

	private static void doPlayerSetup() {
		GameType gameType = PreGameGui.getGameType();
		
		if (gameType == GameType.HOTSEAT) {
			_players = PlayerInfoGui.getPlayers(2);
		} else if (gameType == GameType.NETWORK) {
			_players = PlayerInfoGui.getPlayers(1);
		} else {
			throw new RuntimeException("Unknown game type!");
		}	
	}
	
	private static void handleMessage(ServerMessage message) throws IOException {
		if (message instanceof UpdateStateMessage) {
			updateState(message);
		} else if (message instanceof TurnChangeMessage) {
			String activePlayer = ((TurnChangeMessage) message).whoseTurn();
			_gameState.updateActivePlayer(activePlayer);
			if (isLocalPlayer(activePlayer)) {
				yourMove(getIdForName(activePlayer));
			} else {
				notYourTurnDialog();
			}
		} else if (message instanceof ExecuteMoveMessage) {
			_gameState.executeMove(((ExecuteMoveMessage) message).getMove(),
								   _gui);
		}
	}

	private static boolean isLocalPlayer(String activePlayer) {
		for (Player player : _players)
		if (activePlayer.equals(player.getName())) return true;
		return false;
	}

	private static int getIdForName(String activePlayer) {
		for (Player player : _players)
			if (player.getName().equals(activePlayer)) return player.getId();
		throw new RuntimeException("Bad player name!");
	}

	private static void updateState(ServerMessage message) {
		// TODO confirm we already have this state.
		_gameState = ((UpdateStateMessage) message).getGameState();
		System.err.println("Received new state: " + _gameState);
		_gui.drawState(_gameState);
	}

	private static void yourMove(int activePlayer) throws IOException {
		final GameMove move = new GameMove(_gui.getMoveFor(activePlayer));
		new Thread(new Runnable() {
			public void run() {
				try {
					_serverConnection.sendMove(move, _gameState);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
		_gameState.executeMove(move, _gui);
	}

	private static void notYourTurnDialog() {
		JOptionPane.showMessageDialog(null, "Not your turn", 
											"Hold your horses!",
											JOptionPane.ERROR_MESSAGE );
	}
}