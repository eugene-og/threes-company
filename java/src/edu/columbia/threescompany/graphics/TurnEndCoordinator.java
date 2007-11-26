package edu.columbia.threescompany.graphics;

public class TurnEndCoordinator {
	private Boolean _done;
	
	synchronized void turnStart() {
		_done = false;
	}
	
	synchronized void turnDone() {
		_done = true;
		notify();
	}
	
	synchronized void waitUntilTurnDone() {
		while (!_done) {
			try {
				wait();
			} catch (InterruptedException e) {
				// When I think about InterruptedException, I get a warm and fuzzy feeling
			}
		}
	}
}
