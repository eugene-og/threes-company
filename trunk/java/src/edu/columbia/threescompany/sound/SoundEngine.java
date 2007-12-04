package edu.columbia.threescompany.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class SoundEngine {
	private static Player player; 
	private static String PATH = "sound" + File.separator;
	
	public static String EXPLODE 	= PATH + "explosion.mp3";
	public static String BUBBLE 	= PATH + "bubble.mp3";
	public static String BOMB		= PATH + "bomb.mp3";
	public static String GAMEOVER	= PATH + "gameover.mp3";
	public static String LASER		= PATH + "laser.mp3";
	public static String RIFLE		= PATH + "rifle.mp3";

    public static void close() { if (player != null) player.close(); }

    // play the MP3 file to the sound card
    public synchronized static void play(String filename) {
        try {
            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { player.play(); }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();
    }
}
