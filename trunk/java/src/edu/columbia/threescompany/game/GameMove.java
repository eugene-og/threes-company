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
import edu.columbia.threescompany.gameobjects.ForceBlob;
import edu.columbia.threescompany.gameobjects.GameObject;

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
		if (type == MOVE_TYPE.ACTIVATE)
			addActivationTrigger(blob);
		else if (type == MOVE_TYPE.SPAWN)
			addSpawnTrigger(blob);
		else
			throw new RuntimeException("Unknown move type " + type + "!");
	}

	private void addPhysicalMove(Coordinate pos, Blob blob) {
		PhysicalMove move = new PhysicalMove(pos, blob);
		_moves.put(blob, move);
		int duration = move.getDuration();
	
		if (duration > _duration) _duration = duration;
	}

	private void addSpawnTrigger(Blob blob) {
		addEventMove(blob, EventMove.MOVE_TYPE.SPAWN);
	}
	
	private void addActivationTrigger(Blob blob) {
		addEventMove(blob, EventMove.MOVE_TYPE.ACTIVATE);
	}

	private void addEventMove(Blob blob, EventMove.MOVE_TYPE moveType) {
		int activationTime = 0;
		
		/* If a blob is moving physically, its activation time doesn't
		 * occur until after its move is over. */
		if (_moves.containsKey(blob))
			activationTime = _moves.get(blob).getDuration() + 1;
		
		_events.put(blob, new EventMove(blob, activationTime, moveType));
		if (activationTime + 1 > _duration) _duration = activationTime + 1;
	}

	public List<PhysicalMove> granularMovesAt(int i) {
		// FIXME uh, this is a lot of processing. use an approp. data struc instead.
		List<PhysicalMove> result = new ArrayList<PhysicalMove>();
		for (PhysicalMove move : _moves.values())
			if (i <= move.getDuration()) result.add(move);
		
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

	public boolean hasActivations() {
		for (Blob blob : _events.keySet())
			if (_events.get(blob).getMoveType() == EventMove.MOVE_TYPE.ACTIVATE &&
				(blob instanceof ForceBlob))
				return true;
		return false;
	}
}