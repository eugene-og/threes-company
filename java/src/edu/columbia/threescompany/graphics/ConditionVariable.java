package edu.columbia.threescompany.graphics;

public class ConditionVariable {
	private Boolean _done;
	
	synchronized void setFalse() {
		_done = false;
	}
	
	synchronized void setTrue() {
		_done = true;
		notify();
	}
	
	synchronized void waitUntilTrue() {
		while (!_done) {
			try {
				wait();
			} catch (InterruptedException e) {
				// When I think about InterruptedException, I get a warm and fuzzy feeling
			}
		}
	}
}
