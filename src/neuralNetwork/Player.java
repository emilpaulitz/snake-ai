package neuralNetwork;

public class Player implements Comparable<Object>{
	private String name;

	private int highScore;

	private NN brain;

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

	public void addScore(int score) {
		this.highScore = Math.max(this.highScore, score);
	}

	public String getName() {
		return name;
	}

	public NN getBrain() {
		return brain;
	}

	public Player(String name, NN brain) {
		this.name = name;
		this.highScore = 0;
		this.brain = brain;
	}

	/**
	 * returns true if and only if the names of the players are the same string
	 * 
	 * @param player
	 *            to be tested for equality
	 * @return if the players are the same
	 */
	public boolean equals(Player player) {
		return player.name.equals(this.name);
	}

	/**
	 * returns a perfect copy of the player. Every object contained is copied.
	 */
	public Player clone() {
		Player player = new Player(getName(), new NN(this.getBrain()));
		player.setHighScore(getHighScore());
		return player;
	}

	@Override
	public String toString() {
		return this.name + " (Highscore: " + this.highScore + ")";
	}

	@Override
	public int compareTo(Object o) {
		return Integer.compare(this.getHighScore(), ((Player) o).getHighScore());
	}
}
