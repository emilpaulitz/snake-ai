package evolution;

import java.util.Arrays;

import neuralNetwork.NN;
import neuralNetwork.Neuron;
import neuralNetwork.Synapsis;

public class PlayerBase {

	public int active = 0;

	public int gen;

	private int numPlayers;

	private EvPlayer[] players;

	public int getNumPlayers() {
		return numPlayers;
	}

	public EvPlayer[] getPlayers() {
		return players;
	}

	public EvPlayer getActive() {
		return players[active];
	}

	public PlayerBase(int numPlayers, int input, int hidden, int layers, int output) throws Exception {
		this.numPlayers = numPlayers;
		this.players = new EvPlayer[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			players[i] = new EvPlayer("Bot" + i, new NN(input, hidden, layers, output));
		}
	}

	public void lastJudgement() {

		// Sort according to highscore
		Arrays.sort(players);

		// Reproduce best
		for (int i = players.length - 1; i >= players.length / 2; i--) {
			try {
				players[i - players.length / 2] = mutation(players[i]);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * mutates NN via synapsis weights. Also remarks it in the name by adding a "."
	 * + number of childs the first bot has. Sets highScore to 0!
	 * 
	 * @param player Player to be cloned and mutated
	 * @return "child"
	 * @throws Exception
	 */
	public static EvPlayer mutation(EvPlayer player) throws Exception {

		// TODO refine name change -> create number of mutations variable in this class!
		// First determine Name:
		String newName = player.getName();

		// check if player is already a child
		if (player.getName().contains(".")) {

			// then find the location of its dot
			int dot = 0;
			for (int i = 0; i < player.getName().length(); i++) {
				if (player.getName().charAt(i) == (".".charAt(0))) {
					dot = i;
				}
			}

			// and change its child number
			newName = player.getName().substring(0, dot + 1) + player.getNumChilds();
		} else { // create first child. Congratulations!
			newName = player.getName() + "." + player.getNumChilds();
		}

		// Second mutate the brain: Create a new Brain with the same properties as
		// parent but with mutated changed neurons. Caution when implementing changing
		// hidden layers: think of changing the hidden-parameter!
		NN nn = new NN(player.getBrain());
		mutateNeurons(nn.getNeurons());

		// Lastly increase the number of children this player has and return it
		player.incNumChilds();
		return new EvPlayer(newName, nn, player.getNumChilds());
	}

	/**
	 * Mutates a Neuron array by calling mutateWeight on the weight of every
	 * Synapsis of every Neuron in the given array. Does (!) alter the original.
	 * 
	 * @param original the Neuron array template of with a mutated copy should be
	 *                 created
	 * @return a mutation of the given array
	 */
	public static void mutateNeurons(Neuron[] original) {
		for (int i = 0; i < original.length; i++) {
			for (Synapsis syn : original[i].getOut()) {
				syn.setWeight(mutateWeight(syn.getWeight()));
			}
		}
	}

	public static double mutateWeight(double weight) {
		double rand = Math.random();
		if (rand < 0.3) {
			return weight * (1 + rand);
		} else if (rand > 0.7) {
			return weight * rand;
		}
		return weight;
	}

	public EvPlayer next() {
		if (active + 1 == numPlayers) {
			active = 0;
			gen++;
			lastJudgement();
		} else {
			active++;
		}

		// reset the next player first to avoid influence from past runs
		players[active].getBrain().reset();
		return players[active];
	}
}
