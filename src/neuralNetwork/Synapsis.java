package neuralNetwork;

public class Synapsis {
	
	/**
	 * weight of the synapsis
	 */
	private double weight;
	
	/**
	 * Neuron this synapsis starts at
	 */
	private Neuron start;
	
	/**
	 * Neuron this synapsis influences
	 */
	private Neuron end;
	
	public Synapsis(Neuron start, Neuron end, double initWeight) {
		this.start = start;
		this.end = end;
		this.weight = initWeight;
	}

	public double getWeight() {
		return weight;
	}
	
	public void addWeight(double deltaW) {
		this.weight = this.weight + deltaW;
	}

	public Neuron getStart() {
		return start;
	}

	public Neuron getEnd() {
		return end;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean equals(Synapsis syn) {
		boolean result = getStart() == syn.getStart() && getEnd() == syn.getEnd() && getWeight() == syn.getWeight();
		return result;
	}
	
	@Override
	public Synapsis clone() {
		return new Synapsis(this.start, this.end, this.weight);
	}
}
