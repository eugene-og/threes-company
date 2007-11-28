package edu.columbia.threescompany.graphics;

import edu.columbia.threescompany.gameobjects.Blob;

/**
 * This is a way for Gui and Board to share graphical state.
 */
public class GraphicalGameState {

	private static final long serialVersionUID = 1782537526902961834L;
	private Blob _selectedBlob;

	public GraphicalGameState() {
		_selectedBlob = null;
	}

	public Blob getSelectedBlob() {
		return _selectedBlob;
	}

	public void setSelectedBlob(Blob blob) {
		// TODO verify that this is a blob in the game state
		_selectedBlob = blob;
	}
	
}
