package edu.columbia.threescompany.graphics;

import java.util.List;

import edu.columbia.threescompany.gameobjects.GameObject;

public class Gui {
	static SingleGui _singleGui = new SingleGui();
	
	public void drawState(List<GameObject> gameState) {
		_singleGui.drawState(gameState);
	}
}
