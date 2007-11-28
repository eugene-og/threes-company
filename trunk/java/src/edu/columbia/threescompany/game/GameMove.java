package edu.columbia.threescompany.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.columbia.threescompany.client.LocalGameState;
import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.game.graphics.GUIGameMove;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.GameObject;

public class GameMove implements Serializable {
	/* README FIRST
	 * 
	 * Hi Dan and John:
	 * 
	 * For the sake of simplifying the link between our two classes, please
	 * don't use GameMove -- I'm changing it to handle all the simulation stuff.
	 * GUIGameMove is now how our classes talk, and I've defined it roughly
	 * (feel free to change the implementation). I figured I'd minimize the
	 * amount of data we use to converse.
	 */
	
	private static final long serialVersionUID = -6368788904877021374L;

	public GameMove(GUIGameMove move) {
		Map<Blob, Coordinate> finalPositions = move.getFinalPositions();
		
		_moves = new HashMap<Blob, PhysicalMove>();
		for (GameObject blob : finalPositions.keySet())
			addPhysicalMove(finalPositions.get(blob), (Blob) blob);
		
		_activations = new HashMap<Blob, EventMove>();
		for (GameObject blob : move.getBlobsToActivate())
			addActivationTrigger((Blob) blob);
	}

	private void addPhysicalMove(Coordinate pos, Blob blob) {
		PhysicalMove move = new PhysicalMove(pos, blob);
		_moves.put(blob, move);
		int duration = move.getDuration();
		
		if (duration > _duration) _duration = duration;
	}

	private void addActivationTrigger(Blob blob) {
		int activationTime = 0;
		
		/* If a blob is moving physically, its activation time doesn't
		 * occur until after its move is over. */
		if (_moves.containsKey(blob))
			activationTime = _moves.get(blob).getDuration() + 1;
		
		_activations.put(blob, new EventMove(blob, activationTime));
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
		for (EventMove move : _activations.values())
			if (i == move.getActivationTime()) result.add(move);
		
		return result;
	}

	public int getDuration() {
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
		for (GameObject b : _activations.keySet()) {
			s.append(b.toString());
			s.append(" event: ");
			s.append(_activations.get(b).toString());
			s.append("\n");
		}
		return s.toString();
	}
	
	private Map<Blob, PhysicalMove> _moves;
	private Map<Blob, EventMove> _activations;
	private int _duration = 0;
}