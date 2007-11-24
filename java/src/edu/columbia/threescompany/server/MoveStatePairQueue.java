package edu.columbia.threescompany.server;

import java.util.LinkedList;
import java.util.Queue;

import edu.columbia.threescompany.client.communication.MoveStatePair;

public class MoveStatePairQueue {
	private static MoveStatePairQueue _instance;
	public static MoveStatePairQueue instance() {
		if (_instance == null) _instance = new MoveStatePairQueue();
		return _instance;
	}
	
	private MoveStatePairQueue() {
		_objectQ = new LinkedList<MoveStatePair>();
	}
	
	private Queue<MoveStatePair> _objectQ;
	
	public boolean hasPair() {
		return !_objectQ.isEmpty();
	}
	
	public synchronized MoveStatePair blockForNextPair() {
		while (!hasPair()) {
			try {
				wait();
			} catch (InterruptedException e) { /* do nothing */ }
		}
		return _objectQ.poll();
	}
	
	public void add(MoveStatePair pair) {
		_objectQ.add(pair);
		notify();
	}
}
