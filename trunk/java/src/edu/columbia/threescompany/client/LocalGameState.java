package edu.columbia.threescompany.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.game.EventMove;
import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.game.PhysicalMove;
import edu.columbia.threescompany.game.Player;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.graphics.Gui;

public class LocalGameState implements Serializable {
	private static final long serialVersionUID = 8708609010775403554L;
	
	public void executeMove(GameMove move, Gui gui) {
		/* t is our time variable -- basically, we execute GRANULARITY tiny
		 * moves, sequentially. */
		double turnLength = move.getDuration() + GameParameters.ADDITIONAL_SIMULATION_LENGTH;
		for (int t = 0; t < turnLength; t++)
			executeMoveStep(move, gui, t);
		
		deactivateBlobs();
		growBlobs();
	}

	public void executeMove(GameMove move) {
		executeMove(move, null);
	}

	private void growBlobs() {
		for (GameObject obj : _gameObjects) obj.grow();
	}

	private void executeMoveStep(GameMove move, Gui gui, int t) {
		for (PhysicalMove granularMove : move.granularMovesAt(t))
			granularMove.execute();
		for (EventMove eventMove : move.eventMovesAt(t))
			eventMove.execute();
		
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

	private void deactivateBlobs() {
		for (GameObject obj : _gameObjects)
			if (obj instanceof Blob)
				((Blob) obj).activate(false);
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
	
	public void updateActivePlayer(String id) {
		for (Player player : _players) {
			if (id.equals(player.getName())) {
				_activePlayer = player;
				return;
			}
		}
		throw new RuntimeException("Can't set active player to " + id +
								   ", that player doesn't exist!");
	}

	public boolean gameOver() {
		// TODO do all blobs on the board belong to one player?
		return false;
	}

	private LocalGameState() {
		_gameObjects = new ArrayList<GameObject>();
		_players = new ArrayList<Player>();
	}
	
	public static LocalGameState getSpecifiedGameState(List<GameObject> objects) {
		LocalGameState state = new LocalGameState();
		for (GameObject obj : objects) state.addObject(obj);
		
		return state;
	}
	
	/* For spawning, hole/spot creation, and testing. */
	public void addObject(GameObject obj) {
		_gameObjects.add(obj);
		Player owner = obj.getOwner();
		if (owner != Player.NULL_PLAYER && !_players.contains(owner))
			_players.add(owner);
	}
	
	public static LocalGameState getInitialGameState(List<Player> players) {
		LocalGameState initialGameState =  new LocalGameState();
		initialGameState._activePlayer = players.get(0);
		initialGameState._players = players;
		initialGameState._gameObjects = new ArrayList<GameObject>();
		
		initialGameState.addObject(new PushBlob(1, 1, players.get(0)));
		initialGameState.addObject(new PushBlob(3, 3, players.get(1)));
		
		return initialGameState;
	}
	
	public LocalGameState predictOutcome(GUIGameMove guiMove) {
		LocalGameState clonedState = clone();
		clonedState.executeMove(new GameMove(guiMove));
		return clonedState;
	}
	
	protected LocalGameState clone() {
		LocalGameState state = new LocalGameState();
		state._players = _players;
		state._gameObjects = new ArrayList<GameObject>(_gameObjects.size());
		for (GameObject object : _gameObjects)
			state._gameObjects.add(object.clone());
		return state;
	}
	
	public String toString() {
		/* This is debug only */
		StringBuilder s = new StringBuilder("GameState:\n");
		for (GameObject obj : _gameObjects)
			s.append(obj.toString() + " at " + obj.getPosition().toString() + "\n");
		return s.toString();
	}
	
	private List<GameObject> _gameObjects;
	private List<Player> _players;
	private Player _activePlayer;
}
