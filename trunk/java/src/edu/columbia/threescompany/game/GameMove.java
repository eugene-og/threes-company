package edu.columbia.threescompany.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameMove implements Serializable {
	private static final long serialVersionUID = -6368788904877021374L;

	public List<BlobMove> getBlobMoves() {
		// TODO this doesn't do anything useful yet
		return new ArrayList<BlobMove>();
	}
}