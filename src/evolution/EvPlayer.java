package evolution;

import neuralNetwork.NN;
import neuralNetwork.Player;

public class EvPlayer extends Player implements Comparable<Object> {

	private int numChilds = 0;

	public int getNumChilds() {
		return numChilds;
	}

	public void incNumChilds() {
		this.numChilds++;
	}

	public EvPlayer(String name, NN brain) {
		super(name, brain);
	}

	public EvPlayer(String name, NN brain, int numChilds) {
		super(name, brain);
		this.numChilds = numChilds;
	}

	/**
	 * returns a perfect copy of the player. Every object contained is copied.
	 */
	@Override
	public EvPlayer clone() {
		EvPlayer player = new EvPlayer(getName(), new NN(this.getBrain()), this.getNumChilds());
		player.setHighScore(getHighScore());
		return player;
	}
}
