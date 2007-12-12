package edu.columbia.threescompany.graphics;

public class RedrawThread {
	private boolean _stop = false;
	
	RedrawThread(final Board board) {
		new Thread(new Runnable() {
			public void run() {
				while (!_stop) {
					board.repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						/* gobble */
					}
				}
			}
		}).start();
	}
}
