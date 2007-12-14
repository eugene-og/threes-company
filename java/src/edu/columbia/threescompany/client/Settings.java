package edu.columbia.threescompany.client;


public class Settings {
	
	public boolean soundOn = true;
	
	private static Settings _instance;
	
	public static Settings getInstance() {
		if (_instance == null) _instance = new Settings();
		return _instance;
	}
	
	private Settings() {}

}
