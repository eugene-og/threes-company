package edu.columbia.threescompany.client;

import java.util.List;

import edu.columbia.threescompany.client.communication.ExecuteMoveMessage;
import edu.columbia.threescompany.client.communication.ServerConnection;
import edu.columbia.threescompany.client.communication.ServerMessage;
import edu.columbia.threescompany.client.communication.UpdateStateMessage;
import edu.columbia.threescompany.client.communication.TurnChangeMessage;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.graphics.Gui;
import edu.columbia.threescompany.graphics.PlayerInfoGui;

public class BlobsClient {
	private static LocalGameState _gameState;
	private static ServerConnection _serverConnection;
	private static ChatThread _chatThread;
	private static Gui _gui;
	
	public static void main(String[] args) throws Exception {
		doPlayerSetup();
		
		_chatThread = new ChatThread();
		_gui = Gui.getInstance(_chatThread);
		_chatThread.setGui(_gui);
		
		ServerMessage message;
		/* TODO Interface with Eugene's code in re "ready to play"/"game start"/etc. */
		while ((message = _serverConnection.receiveMessage()) != null)
			handleMessage(message);
		
		// TODO display a polite game over in GUI
	}

	private static void doPlayerSetup() {
		// TODO: We're locked into Hotseat for now
		List<Player> players = PlayerInfoGui.getPlayers(2);
		System.out.println("Success: " + players.get(0).getName());
	}
	
	private static void handleMessage(ServerMessage message) {
		if (message instanceof UpdateStateMessage) {
			_gameState = ((UpdateStateMessage) message).getGameState();
			_gui.drawState(_gameState);
		} else if (message instanceof TurnChangeMessage) {
			int activePlayer = ((TurnChangeMessage) message).whoseTurn();
			_gameState.updateActivePlayer(activePlayer);
			if (_gameState.isLocalPlayer(activePlayer)) {
				GameMove move = _gui.getMoveFor(activePlayer);
				_gameState.executeMove(move, _gui);
				_serverConnection.sendMove(move, _gameState);
			} else {
				// TODO Display "Someone else's turn" or something
			}
		} else if (message instanceof ExecuteMoveMessage) {
			_gameState.executeMove(((ExecuteMoveMessage) message).getMove(),
								   _gui);
		}
	}
}
