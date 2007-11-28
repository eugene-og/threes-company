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
import edu.columbia.threescompany.gameobjects.DeathRayBlob;
import edu.columbia.threescompany.gameobjects.ExplodingBlob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.gameobjects.PullBlob;
import edu.columbia.threescompany.gameobjects.PushBlob;
import edu.columbia.threescompany.gameobjects.SlipperyBlob;
import edu.columbia.threescompany.graphics.Gui;

public class LocalGameState implements Serializable {
	private static final long serialVersionUID = 8708609010775403554L;
	
	public void executeMove(GameMove move, Gui gui) {
		/* t is our time variable -- basically, we execute GRANULARITY tiny
		 * moves, sequentially. */
		double turnLength = move.getDuration();
		if (move.hasActivations())
			turnLength += GameParameters.ADDITIONAL_SIMULATION_LENGTH;
		
		for (int t = 0; t < turnLength; t++)
			executeMoveStep(move, gui, t, (int) turnLength);
		
		deactivateBlobs();
		growBlobs();
		checkCollisions();
		
		System.out.println("Game objects after executing move:");
		for (GameObject item : _gameObjects) {
			System.out.println(item);			
		}
	}

	public void executeMove(GameMove move) {
		executeMove(move, null);
	}

	private void growBlobs() {
		for (GameObject obj : _gameObjects) obj.grow();
	}

	private void executeMoveStep(GameMove move, Gui gui, int t, int tmax) {
		for (PhysicalMove granularMove : move.granularMovesAt(t))
			granularMove.execute(this);
		for (EventMove eventMove : move.eventMovesAt(t))
			eventMove.execute(this);
		
		applyForces();
		checkCollisions();
		
		if (gui != null) {
			gui.drawState(this);
			try {
				/* TODO: Adjust this value so moves animate nicely. */
				Thread.sleep(sleepTimeForFrame(t, tmax));
			} catch (InterruptedException exception) {}
		}
	}
	
	private long sleepTimeForFrame(int t, int tmax) {
		int averageWait = GameParameters.AVERAGE_MS_FRAME_GAP;
		return (long) (averageWait * (1.5 - (t / tmax)));
	}

	private void checkCollisions() {
		List<GameObject> killList = new ArrayList<GameObject>();
		for (GameObject obj1 : _gameObjects)
			for (GameObject obj2 : _gameObjects)
				if (!obj1.isDead() && !obj2.isDead())
					if (obj1.checkCollision(obj2))
						killList.add(obj1);
		
		for (GameObject obj : killList) {
			System.out.println("KILLING " + obj);
			obj.die();
		}
	}
	
	private void applyForces() {
		for (GameObject obj1 : _gameObjects)
		for (GameObject obj2 : _gameObjects) {
			if (obj1.isDead() || obj2.isDead()) continue;
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
		for (Player player : _players)
			if (hasAnyBlobsLeft(player)) return false;
		
		return true;
	}

	private boolean hasAnyBlobsLeft(Player player) {
		for (GameObject obj : _gameObjects)
			if (obj instanceof Blob && !obj.isDead() && obj.getOwner().equals(player))
				return true;
		
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
		
		initialGameState.addObject(new PushBlob(1, 4, players.get(0)));
		initialGameState.addObject(new PushBlob(1, 8, players.get(0)));
		initialGameState.addObject(new PullBlob(1, 12, players.get(0)));
		initialGameState.addObject(new PullBlob(1, 16, players.get(0)));
		initialGameState.addObject(new DeathRayBlob(4, 6, players.get(0)));
		initialGameState.addObject(new SlipperyBlob(4, 10, players.get(0)));
		initialGameState.addObject(new ExplodingBlob(4, 14, players.get(0)));
		
		initialGameState.addObject(new PushBlob(19, 4, players.get(1)));
		initialGameState.addObject(new PushBlob(19, 8, players.get(1)));
		initialGameState.addObject(new PullBlob(19, 12, players.get(1)));
		initialGameState.addObject(new PullBlob(19, 16, players.get(1)));
		initialGameState.addObject(new DeathRayBlob(16, 6, players.get(1)));
		initialGameState.addObject(new SlipperyBlob(16, 10, players.get(1)));
		initialGameState.addObject(new ExplodingBlob(16, 14, players.get(1)));
		
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
			s.append(obj.toString() + " at " + obj.getPosition().toString() + 
					" owned by " + obj.getOwner() + "\n");
		return s.toString();
	}
	
	private List<GameObject> _gameObjects;
	private List<Player> _players;
	private Player _activePlayer;
}
