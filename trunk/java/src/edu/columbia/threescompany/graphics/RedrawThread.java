package edu.columbia.threescompany.graphics;

public class RedrawThread {
	private boolean _stop = false;
	private static final int FRAME_DELAY = 60;
	
	RedrawThread(final Board board) {
		new Thread(new Runnable() {
			public void run() {
				while (!_stop) {
					long start = System.currentTimeMillis();
					
					/* No more than 5 ms, you criminal! */
					board.repaint(5, 0, 0, board.getWidth(), board.getHeight());
					
					long sleepTime = FRAME_DELAY - (System.currentTimeMillis() - start);
						
					try {
						if (sleepTime > 0) Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						/* gobble */
					}
				}
			}
		}).start();
	}
}
