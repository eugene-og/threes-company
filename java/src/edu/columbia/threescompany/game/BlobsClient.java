package edu.columbia.threescompany.game;

import java.util.LinkedList;
import java.util.List;

import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.graphics.Gui;

public class BlobsClient {
	private static Gui _gui;
	private static ServerConnection _connection;
	public static List<GameObject> _blobs = new LinkedList<GameObject>();
	
	public static void runGame() {
		Player me = _gui.getPlayer();
		_connection.sendPlayer(me);
		_connection.waitForGame();
		
		_blobs = _connection.receiveState();
		
		while (!_connection.gameOver()) {
			_gui.drawState(_blobs);
			
			GameMove move = _gui.getMove();
			_connection.sendMove(move);
		}		
	}
	
	public static void main(String[] args) {
		_gui = new Gui();
		runGame();
	}
}