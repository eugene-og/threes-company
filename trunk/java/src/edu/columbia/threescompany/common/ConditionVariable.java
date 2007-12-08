package edu.columbia.threescompany.common;

public class ConditionVariable {
	private Boolean _done;
	
	public synchronized void setFalse() {
		_done = false;
	}
	
	public synchronized void setTrue() {
		_done = true;
		notify();
	}
	
	public synchronized void waitUntilTrue() {
		while (!_done) {
			try {
				wait();
			} catch (InterruptedException e) {
				// When I think about InterruptedException, I get a warm and fuzzy feeling
			}
		}
	}
}
