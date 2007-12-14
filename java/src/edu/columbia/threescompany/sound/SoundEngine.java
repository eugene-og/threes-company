package edu.columbia.threescompany.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import edu.columbia.threescompany.client.Settings;
import edu.columbia.threescompany.common.ConditionVariable;

public class SoundEngine extends Thread {
	private static String PATH = "sound" + File.separator;
	
	public static String EXPLODE 	= PATH + "explosion.wav";
	public static String BUBBLE 	= PATH + "bubble.wav";
	public static String BOMB		= PATH + "bomb.wav";
	public static String GAMEOVER	= PATH + "gameover.wav";
	public static String LASER		= PATH + "laser.wav";
	public static String RIFLE		= PATH + "rifle.wav";
	
	private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

	private String filename;

	private ConditionVariable hasFileToPlay;
	
	
	public SoundEngine() {
		hasFileToPlay = new ConditionVariable();
		hasFileToPlay.setFalse();
	}

	enum Position {
		LEFT, RIGHT, NORMAL
	};

	public void run() {
		run(Position.NORMAL);
	}
	
	public void play(String filename) {
		if (!Settings.getInstance().soundOn) return;
		this.filename = filename;
		hasFileToPlay.setTrue();
	}
	
	public void run(Position curPosition) {
		while(true) {
			hasFileToPlay.waitUntilTrue();
			File soundFile = new File(filename);
			if (!soundFile.exists()) {
				System.err.println("Wave file not found: " + filename);
			}
	
			AudioInputStream audioInputStream = null;
			try {
				audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			} catch (UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	
			AudioFormat format = audioInputStream.getFormat();
			SourceDataLine auline = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
	
			try {
				auline = (SourceDataLine) AudioSystem.getLine(info);
				auline.open(format);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			if (auline.isControlSupported(FloatControl.Type.PAN)) {
				FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
				if (curPosition == Position.RIGHT)
					pan.setValue(1.0f);
				else if (curPosition == Position.LEFT)
					pan.setValue(-1.0f);
			}
	
			auline.start();
			int nBytesRead = 0;
			byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
	
			try {
				while (nBytesRead != -1) {
					nBytesRead = audioInputStream.read(abData, 0, abData.length);
					if (nBytesRead >= 0)
						auline.write(abData, 0, nBytesRead);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				auline.drain();
				auline.close();
			}
			hasFileToPlay.setFalse();
		}
	}
}
