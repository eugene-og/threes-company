package edu.columbia.threescompany.client;

import java.util.List;

import edu.columbia.threescompany.game.PhysicalMove;
import edu.columbia.threescompany.game.EventMove;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.gameobjects.Force;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.graphics.Gui;

public class LocalGameState {
	private List<GameObject> _gameObjects;
	private List<Player> _players;
	private List<Player> _localPlayers;
	private Player _activePlayer;
	
	public void executeMove(GameMove move, Gui gui) {
		/* t is our time variable -- basically, we execute GRANULARITY tiny
		 * moves, sequentially. */
		for (int t = 0; t < GameParameters.GRANULARITY_OF_PHYSICS; t++)
			executeMoveStep(move, gui, t);
	}

	private void executeMoveStep(GameMove move, Gui gui, int t) {
		for (EventMove instantEvent : move.instantMovesAt(t))
			// These will fire once
			instantEvent.execute();
		for (PhysicalMove granularEvent : move.granularMovesAt(t))
			// These will fire over and over
			granularEvent.execute();
		
		applyForces();
		checkCollisions();
		
		if (gui != null) {
			gui.drawState(this);
			try {
				/* TODO: Adjust this value so moves animate nicely. */
				Thread.sleep(20);
			} catch (InterruptedException exception) {}
		}
	}
	
	private void checkCollisions() {
		for (GameObject obj1 : _gameObjects)
		for (GameObject obj2 : _gameObjects)
			obj1.checkCollision(obj2);
	}

	private void applyForces() {
		for (GameObject obj1 : _gameObjects)
		for (GameObject obj2 : _gameObjects) {
			Force f = obj1.actOn(obj2);
			
			/* Newton's 3rd law: */
			obj1.applyForce(f.inverse());
			obj2.applyForce(f);
		}
	}

	public List<GameObject> getObjects() {
		return _gameObjects;
	}

	public List<Player> getPlayers() {
		return _players;
	}
	
	public int getAP() {
		return _activePlayer.getAbilityPoints();
	}
	
	public void updateActivePlayer(int id) {
		for (Player player : _players) {
			if (id == player.getId()) {
				_activePlayer = player;
				return;
			}
		}
		throw new RuntimeException("Can't set active player to " + id +
								   ", that player doesn't exist!");
	}

	public boolean isLocalPlayer(int somePlayer) {
		for (Player player : _localPlayers)
			if (player.getId() == somePlayer) return true;
		return false;
	}

}
