package edu.columbia.threescompany.client;

import java.io.IOException;
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
		
		_chatThread = new ChatThread(_players);
		_gui = Gui.getInstance(_chatThread);
		_chatThread.setGui(_gui);
		_chatThread.start();

		_serverConnection = new ServerConnection();	// FIXME use port
		_serverConnection.sendPlayers(_players);
		
		ServerMessage message;
//		while ((message = _serverConnection.receiveMessage()) != null)
//			handleMessage(message);
		
		gameOverDialog();
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
			if (_gameState.isLocalPlayer(activePlayer)) {
				yourMove(getIdForName(activePlayer));
			} else {
				notYourTurnDialog();
			}
		} else if (message instanceof ExecuteMoveMessage) {
			_gameState.executeMove(((ExecuteMoveMessage) message).getMove(),
								   _gui);
		}
	}

	private static int getIdForName(String activePlayer) {
		for (Player player : _players)
			if (player.getName().equals(activePlayer)) return player.getId();
		throw new RuntimeException("Bad player name!");
	}

	private static void updateState(ServerMessage message) {
		// TODO confirm we already have this state.
		_gameState = ((UpdateStateMessage) message).getGameState();
		_gui.drawState(_gameState);
	}

	private static void yourMove(int activePlayer) throws IOException {
		GameMove move = _gui.getMoveFor(activePlayer);
		_gameState.executeMove(move, _gui);
		// TODO this will create a simulation pause!
		_serverConnection.sendMove(move, _gameState);
	}

	private static void notYourTurnDialog() {
		JOptionPane.showMessageDialog(null, "Not your turn", 
											"Hold your horses!",
											JOptionPane.ERROR_MESSAGE );
	}
}
