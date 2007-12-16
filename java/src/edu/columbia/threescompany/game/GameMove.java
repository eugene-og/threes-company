package edu.columbia.threescompany.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.EventMove.MOVE_TYPE;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.DeathRayBlob;
import edu.columbia.threescompany.gameobjects.ForceBlob;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.gameobjects.GameParameters;

public class GameMove implements Serializable {
	private static final long serialVersionUID = -6368788904877021374L;

	public GameMove(GUIGameMove move) {
		Map<Blob, Coordinate> finalPositions = move.getFinalPositions();
		
		_moves = new HashMap<Blob, PhysicalMove>();
		for (GameObject blob : finalPositions.keySet())
			addPhysicalMove(finalPositions.get(blob), (Blob) blob);
		
		_events = new HashMap<Blob, EventMove>();
		Map<Blob, MOVE_TYPE> activations = move.getBlobsToActivate();
		for (GameObject blob : activations.keySet())
			addMoveTrigger((Blob) blob, activations.get(blob));
	}

	private void addMoveTrigger(Blob blob, MOVE_TYPE type) {
		/* FIXME this is a useless function, but it's serving as a quick
		 * verifier that we don't try to fire unimplemented move types. it can
		 * be removed when bugs are closed. */ 
		
		if (type != MOVE_TYPE.ACTIVATE && type != MOVE_TYPE.SPAWN)
			throw new RuntimeException("Unimplemented move type " + type + "!");
		
		addEventMove(blob, type);
	}

	private void addPhysicalMove(Coordinate pos, Blob blob) {
		PhysicalMove move = new PhysicalMove(pos, blob);
		_moves.put(blob, move);
		int duration = move.getDuration();
	
		if (duration > _duration) _duration = duration;
	}

	public void addEventMove(Blob blob, MOVE_TYPE moveType) {
		int activationTime = 0;
		
		/* If a blob is moving physically, its activation time doesn't
		 * occur until after its move is over. */
		if (_moves.containsKey(blob))
			activationTime = _moves.get(blob).getDuration() + 1;
		
		if (blob instanceof DeathRayBlob && moveType == MOVE_TYPE.DEACTIVATE)
			activationTime += GameParameters.DEATH_RAY_DURATION;
		
		_events.put(blob, new EventMove(blob, activationTime, moveType));
		
		if (blob instanceof DeathRayBlob && moveType == MOVE_TYPE.ACTIVATE)
			/* make a space for the deactivation */
			activationTime += GameParameters.DEATH_RAY_DURATION;
		
		if (activationTime + 1 > _duration) _duration = activationTime + 1;
	}

	public List<PhysicalMove> granularMovesAt(int i) {
		// FIXME uh, this is a lot of processing. use an approp. data struc instead.
		List<PhysicalMove> result = new ArrayList<PhysicalMove>();
		for (PhysicalMove move : _moves.values())
			if (i < move.getDuration()) result.add(move);
		
		return result;
	}
	
	public List<EventMove> eventMovesAt(int i) {
		// FIXME same as above FIXME
		List<EventMove> result = new ArrayList<EventMove>();
		for (EventMove move : _events.values())
			if (i == move.getActivationTime()) result.add(move);
		
		return result;
	}

	public int getDuration() {
		System.err.println("Reporting move duration of " + _duration);
		return _duration;
	}

	public boolean hasActivations() {
		for (Blob blob : _events.keySet())
			if (_events.get(blob).getMoveType() == EventMove.MOVE_TYPE.ACTIVATE &&
					(blob instanceof ForceBlob) && !blob.isDead())
				return true;
		return false;
	}
	
	public boolean hasMoves() {
		for (Blob blob : _moves.keySet())
			if (!blob.isDead()) return true;
		return false;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder("GameMove: ");
		for (GameObject b : _moves.keySet()) {
			s.append(b.toString());
			s.append(" move: ");
			s.append(_moves.get(b).toString());
			s.append("\n");
		}
		for (GameObject b : _events.keySet()) {
			s.append(b.toString());
			s.append(" event: ");
			s.append(_events.get(b).toString());
			s.append("\n");
		}
		return s.toString();
	}
	
	private Map<Blob, PhysicalMove> _moves;
	private Map<Blob, EventMove> _events;
	private int _duration = 0;
}