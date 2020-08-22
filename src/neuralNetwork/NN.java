package neuralNetwork;

import java.util.LinkedList;

public class NN {

	/**
	 * Array containing all the Neurons and their Synapses. Bias neuron at id 0.
	 */
	private Neuron[] neurons;

	/**
	 * Array containing all the Neurons (including their Synapses)
	 */
	public Neuron[] getNeurons() {
		return neurons;
	}

	public Neuron getNeuron(int i) {
		return neurons[i];
	}

	public void setNeurons(Neuron[] neurons) {
		this.neurons = neurons;
	}

	/**
	 * Number of neurons in the neural network (including bias neuron)
	 */
	public int getNeuronNumber() {
		return neurons.length;
	}

	/**
	 * number of input neurons
	 */
	private int input;

	/**
	 * number of hidden neurons
	 */
	private int hidden;

	/**
	 * number of hidden layers
	 */
	private int layers;

	/**
	 * number of output neurons
	 */
	private int output;

	/**
	 * number of input neurons
	 */
	public int getInput() {
		return input;
	}

	/**
	 * number of hidden neurons
	 */
	public int getHidden() {
		return hidden;
	}

	/**
	 * number of hidden layers
	 */
	public int getLayers() {
		return layers;
	}

	/**
	 * number of output neurons
	 */
	public int getOutput() {
		return output;
	}

	/**
	 * set number of input neurons
	 */
	public void setInput(int i) {
		this.input = i;
	}

	/**
	 * Constructor
	 * 
	 * @param input  Number of neurons in the input layer
	 * @param hidden Number of neurons in the hidden layers
	 * @param layers Number of hidden layers
	 * @param output Number of neurons in the output layer
	 * @throws Exception When hidden Neurons cannot be distributed equally on all
	 *                   the layers
	 */
	public NN(int input, int hidden, int layers, int output) {
		if (hidden % layers != 0) {
			System.err.println("hidden Neurons cannot be distributed equally on the layers");
		}

		this.input = input;
		this.hidden = hidden;
		this.layers = layers;
		this.output = output;
		this.neurons = new Neuron[input + hidden + output + 1];

		// initialize neurons
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new Neuron(i);
		}
		neurons[0].setActivation(1.0);

		for (int i = 0; i < neurons.length; i++) {

			LinkedList<Synapsis> out = new LinkedList<Synapsis>();
			int[] startEnd = calcSynapses(i);

			// create Synapses to every Neuron in the next layer
			for (int j = startEnd[0]; j <= startEnd[1]; j++) {
				out.add(new Synapsis(neurons[i], neurons[j], (Math.random() - 0.5) * 10));
			}

			neurons[i].setOut(out);
		}

		for (Neuron neuron : neurons) {
			for (Synapsis syn : neuron.getOut()) {
				syn.getEnd().addIn(syn);
			}
		}
	}

	/**
	 * Constructor
	 * 
	 * @param input  Number of neurons in the input layer
	 * @param hidden Number of neurons in the hidden layers
	 * @param layers Number of hidden layers
	 * @param output Number of neurons in the output layer
	 * @throws Exception When hidden Neurons cannot be distributed equally on all
	 *                   the layers
	 */
	public NN(int input, int hidden, int layers, int output, Neuron.FunType actFun) {
		if (hidden % layers != 0) {
			System.err.println("hidden Neurons cannot be distributed equally on the layers");
		}

		this.input = input;
		this.hidden = hidden;
		this.layers = layers;
		this.output = output;
		this.neurons = new Neuron[input + hidden + output + 1];

		// initialize neurons
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new Neuron(i, actFun);
		}
		neurons[0].setActivation(1.0);

		for (int i = 0; i < neurons.length; i++) {

			LinkedList<Synapsis> out = new LinkedList<Synapsis>();
			int[] startEnd = calcSynapses(i);

			// create Synapses to every Neuron in the next layer
			for (int j = startEnd[0]; j <= startEnd[1]; j++) {
				out.add(new Synapsis(neurons[i], neurons[j], (Math.random() - 0.5) * 10));
			}

			neurons[i].setOut(out);
		}

		for (Neuron neuron : neurons) {
			for (Synapsis syn : neuron.getOut()) {
				syn.getEnd().addIn(syn);
			}
		}
	}

	/**
	 * Constructor
	 * 
	 * @param input      Number of neurons in the input layer
	 * @param hidden     Number of neurons in the hidden layers
	 * @param layers     Number of hidden layers
	 * @param output     Number of neurons in the output layer
	 * @param synWeights Weights of the synapses, starting with an array containing
	 *                   the weights of the synapses from neuron 0 to all of its
	 *                   successors, then neuron 1 etc.
	 * @throws Exception When hidden Neurons cannot be distributed equally on all
	 *                   the layers
	 */
	public NN(int input, int hidden, int layers, int output, double[][] synWeights) {
		if (hidden % layers != 0) {
			System.err.println("hidden Neurons cannot be distributed equally on the layers");
		}

		this.input = input;
		this.hidden = hidden;
		this.layers = layers;
		this.output = output;
		this.neurons = new Neuron[input + hidden + output + 1];

		// initialize neurons
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new Neuron(i);
		}
		neurons[0].setActivation(1.0);

		for (int i = 0; i < neurons.length; i++) {
			LinkedList<Synapsis> out = new LinkedList<Synapsis>();
			int[] startEnd = calcSynapses(i);

			// create Synapses to every Neuron in the next layer
			for (int j = startEnd[0]; j <= startEnd[1]; j++) {
				out.add(new Synapsis(neurons[i], neurons[j], synWeights[i][j - startEnd[0]]));
			}

			neurons[i].setOut(out);
		}

		for (Neuron neuron : neurons) {
			for (Synapsis syn : neuron.getOut()) {
				syn.getEnd().addIn(syn);
			}
		}
	}

	/**
	 * Constructor
	 * 
	 * @param input      Number of neurons in the input layer
	 * @param hidden     Number of neurons in the hidden layers
	 * @param layers     Number of hidden layers
	 * @param output     Number of neurons in the output layer
	 * @param synWeights Weights of the synapses, starting with an array containing
	 *                   the weights of the synapses from neuron 0 to all of its
	 *                   successors, then neuron 1 etc.
	 * @throws Exception When hidden Neurons cannot be distributed equally on all
	 *                   the layers
	 */
	public NN(int input, int hidden, int layers, int output, double[][] synWeights, Neuron.FunType actFun) {
		if (hidden % layers != 0) {
			System.err.println("hidden Neurons cannot be distributed equally on the layers");
		}

		this.input = input;
		this.hidden = hidden;
		this.layers = layers;
		this.output = output;
		this.neurons = new Neuron[input + hidden + output + 1];

		// initialize neurons
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new Neuron(i, actFun);
		}
		neurons[0].setActivation(1.0);

		for (int i = 0; i < neurons.length; i++) {
			LinkedList<Synapsis> out = new LinkedList<Synapsis>();
			int[] startEnd = calcSynapses(i);

			// create Synapses to every Neuron in the next layer
			for (int j = startEnd[0]; j <= startEnd[1]; j++) {
				out.add(new Synapsis(neurons[i], neurons[j], synWeights[i][j - startEnd[0]]));
			}

			neurons[i].setOut(out);
		}

		for (Neuron neuron : neurons) {
			for (Synapsis syn : neuron.getOut()) {
				syn.getEnd().addIn(syn);
			}
		}
	}

	/**
	 * Constructs a NN from the given neuron - arrays
	 * 
	 * @throws Exception when wrong parameters are entered
	 */
	public NN(int input, int hidden, int layers, int output, Neuron[] neurons) throws Exception {
		this.input = input;
		this.hidden = hidden;
		this.layers = layers;
		this.output = output;
		this.neurons = neurons;
		if (neurons.length != 1 + input + hidden + output) {
			throw new Exception("length of neuron array incorrect");
		}
		if (hidden % layers != 0) {
			throw new Exception("hidden Neurons cannot be distributed equally on all the layers");
		}
	}

	/**
	 * Constructs a copy of the given NN: every object contained gets copied and
	 * bundled together
	 * 
	 * @param nn
	 */
	public NN(NN nn) {
		this.input = nn.getInput();
		this.hidden = nn.getHidden();
		this.layers = nn.getLayers();
		this.output = nn.getOutput();
		this.neurons = new Neuron[nn.getNeuronNumber()];
		for (int i = 0; i < nn.getNeuronNumber(); i++) {
			this.neurons[i] = new Neuron(i, nn.getNeuron(i).getActFun());
		}
		for (int i = 0; i < nn.getNeuronNumber(); i++) {
			for (Synapsis syn : nn.getNeuron(i).getOut()) {
				Synapsis newSyn = new Synapsis(this.neurons[syn.getStart().getId()],
						this.neurons[syn.getEnd().getId()], syn.getWeight());
				this.neurons[i].addOut(newSyn);
				if (syn.getStart().getId() != i) {
					System.out.println("halt stoppppp");
				}
				this.neurons[newSyn.getEnd().getId()].addIn(newSyn);
			}
		}
	}

	public boolean checkup() {
		
		boolean result = true;
		
		for (Neuron neur : this.getNeurons()) {
			for (Synapsis syn : neur.getOut()) {
				result = result && syn.getEnd().getIn().contains(syn) && syn.getStart().getOut().contains(syn);
			}
		}
		
		return result;
	}
	
	/**
	 * Determines if the networks have all the same parameters. Not only, if they
	 * collect the exact same objects.
	 * 
	 * @param nn
	 * @return
	 */
	public boolean equals(NN nn) {
		boolean result = false;
		result = getInput() == nn.getInput() && getOutput() == nn.getOutput() && getHidden() == nn.getHidden()
				&& getLayers() == nn.getLayers();
		if (getNeuronNumber() == nn.getNeuronNumber() && result) {
			for (int i = 0; i < neurons.length; i++) {
				result = result && neurons[i].equals(nn.neurons[i]);
			}
		} else {
			return false;
		}
		return result;
	}

	/**
	 * @return the double array that is the activation of the output neurons
	 */
	public double[] getResult() {
		double[] result = new double[getOutput()];
		int outputStart = hidden + input + 1;
		for (int i = outputStart; i < getNeuronNumber(); i++) {
			result[i - outputStart] = getNeurons()[i].getActivation();
		}
		return result;
	}

	/**
	 * @return the activation of the i-th output neuron (starts at 0)
	 */
	public double getResult(int i) {
		return neurons[input + hidden + 1 + i].getActivation();
	}

	public void stepTopoPrintLog(double[] input) {

		if (input.length != this.input) {
			System.err.println("Length of given input and number of input neurons differ!");
		}

		// for every neuron except the bias:
		for (Neuron neur : neurons) {

			if (!neur.isBias()) {
				if (isInput(neur)) {

					neur.setNetinput(input[neur.getId() - 1]);
					neur.setActivation(input[neur.getId() - 1]);

				} else {

					// First update its netinput
					neur.setNetinput(0);
					System.out.println("Neuron " + neur.getId() + " gets following netinput: \\\\$");
					for (Synapsis syn : neur.getIn()) {
						neur.addNetinput(syn.getStart().getActivation() * syn.getWeight());
						System.out.print((syn.getStart().getActivation() * syn.getWeight()) + " + ");
					}
					System.out.print("= " + neur.getNetinput());

					// Then its activation
					neur.updateActivation();
					System.out.println("$, leading to an activation of " + neur.getActivation() + "\\\\");
				}
			}
		}
	}

	/**
	 * Calculates one step using the activation function fAct. Implements toplogical
	 * order, which is the array index in increasing order.
	 * 
	 * @param input a double array of length input (!), represents the activation
	 *              the input neurons will have in the next step.
	 */
	public void stepTopo(double[] input) {

		if (input.length != this.input) {
			System.err.println("Length of given input and number of input neurons differ!");
		}

		// for every neuron except the bias:
		for (int i = 1; i < getNeuronNumber(); i++) {
			Neuron neur = this.getNeuron(i);

			if (isInput(neur)) {

				neur.setNetinput(input[neur.getId() - 1]);
				neur.setActivation(input[neur.getId() - 1]);

			} else {

				// First update its netinput
				neur.setNetinput(0);
				for (Synapsis syn : neur.getIn()) {
					neur.addNetinput(syn.getStart().getActivation() * syn.getWeight());
				}

				// Then its activation
				neur.updateActivation();
			}
		}
	}

	/**
	 * sets netinput = 0 and updates the activation
	 */
	public void reset() {
		for (int i = 1; i < getNeurons().length; i++) {
			getNeurons()[i].setNetinput(0);
			getNeurons()[i].updateActivation();
		}
	}

	/**
	 * calculates a list of Synapses to every neuron in the next layer (with random
	 * weights between -50 and 50)
	 * 
	 * @param i Id of the current neuron
	 * @return LinkedList of Synapses to every neuron in the next layer
	 */
	private int[] calcSynapses(int i) {
		int hiddenPerLayer = hidden / layers;

		// first and last Neuron you connect the Neuron to
		int start = 0;
		int end = 0;

		if (i == 0) { // you are at the bias neuron
			start = input + 1;
			end = getNeuronNumber() - 1;
		} else if (i <= input) { // you are at an input neuron

			// create Synapses to every Neuron in the next hidden layer
			start = input + 1;
			if (hidden > 0) {
				end = input + hiddenPerLayer;
			} else {
				end = getNeuronNumber() - 1;
			}

		} else if (i <= input + hidden) { // you are at a hidden neuron

			// you are in the n-th hidden layer
			int n = (int) Math.ceil((double) (i - input) / (double) hiddenPerLayer);

			if (n == layers) { // you are in the last hidden layer
				start = input + hidden + 1;
				end = getNeuronNumber() - 1;
			} else { // you are not in the last hidden layer
				start = n * hiddenPerLayer + input + 1;
				end = start + hiddenPerLayer - 1;
			}
		} else { // you are at an output neuron and dont create any more synapses
			end = start - 1;
		}

		return new int[] { start, end };
	}

	public boolean isInput(Neuron neuron) {
		return neuron.getId() <= input;
	}

	public boolean isHidden(Neuron neuron) {
		return neuron.getId() > input && neuron.getId() <= (input + hidden);
	}

	public boolean isOutput(Neuron neuron) {
		return neuron.getId() > (input + hidden);
	}

	public String print() {
		String result = "";

		for (Neuron neur : this.neurons) {
			result += "Neuron " + neur.getId() + " Activation: " + neur.getActivation() + "\n";
			for (Synapsis syn : neur.getOut()) {
				result += "\tSynapsis to Neuron " + syn.getEnd().getId() + " has weight "
						+ (double) Math.round(syn.getWeight() * 100d) / 100d + "\n";
			}
		}

		return result;
	}

	public int largestLayer(boolean includeInput) {
		int result = Math.max(getOutput(), (getHidden() / getLayers()));
		if (includeInput) {
			result = Math.max(result, getInput());
		}
		return result;
	}
}
