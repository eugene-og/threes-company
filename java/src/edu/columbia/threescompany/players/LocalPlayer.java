package edu.columbia.threescompany.players;

import java.util.List;

import edu.columbia.threescompany.game.GameMove;
import edu.columbia.threescompany.gameobjects.GameObject;
import edu.columbia.threescompany.graphics.SingleGui;

public class LocalPlayer extends Player {
	private SingleGui _gui;
	
	public LocalPlayer(String name) {
		super(name);
	}

	@Override
	public GameMove getMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processMoves(GameMove move) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateState(List<GameObject> boardState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableInput() {
		// TODO Auto-generated method stub
	}

	@Override
	public void enableInput() {
		// TODO Auto-generated method stub
	}

}
