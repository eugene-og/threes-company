package edu.columbia.threescompany.client;

import java.util.List;

import edu.columbia.threescompany.client.communication.ServerConnection;
import edu.columbia.threescompany.client.communication.UpdateStateMessage;
import edu.columbia.threescompany.client.communication.TurnChangeMessage;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.graphics.Gui;
import edu.columbia.threescompany.graphics.PlayerInfoGui;

public class BlobsClient {
	private static LocalGameState _gameState;
	private static ServerConnection _serverConnection;
	
	public static void main(String[] args) throws Exception {
		doPlayerSetup();

		Gui gui = Gui.getInstance();
		ServerMessage message;
		
		/* TODO Interface with Eugene's code in re "ready to play"/"game start"/etc. */
		while ((message = _serverConnection.receiveMessage()) != null)
			handleMessage(gui, message);
		
		// TODO display a polite game over in GUI
	}

	private static void doPlayerSetup() {
		// TODO: We're locked into Hotseat for now
		List<Player> players = PlayerInfoGui.getPlayers(2);
		System.out.println("Success: " + players.get(0).getName());
	}
	
	private static void handleMessage(Gui gui, ServerMessage message) {
		if (message instanceof UpdateStateMessage) {
			_gameState = ((UpdateStateMessage) message).getGameState();
			gui.drawState(_gameState);
		} else if (message instanceof TurnChangeMessage) {
			int activePlayer = ((TurnChangeMessage) message).whoseTurn();
			if (_gameState.isLocalPlayer(activePlayer)) {
				GameMove move = gui.getMoveFor(activePlayer);
				_serverConnection.sendMove(move);
			} else {
				// TODO Display "Someone else's turn" or something
			}
		} else {
			// TODO more message types
		}
	}
}
