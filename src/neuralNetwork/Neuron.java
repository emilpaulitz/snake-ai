package neuralNetwork;

import java.util.LinkedList;

public class Neuron {
	/**
	 * Identification Number of Neuron, starts at 0
	 */
	private int id;

	/**
	 * Activation Status of the Neuron between -1 and 1
	 */
	private double activation;

	/**
	 * Accumulates during the calculation of one step
	 */
	private double netinput;

	/**
	 * List of Synapses this influences
	 */
	private LinkedList<Synapsis> out;

	/**
	 * List of Synapses influencing this Neuron
	 */
	private LinkedList<Synapsis> in;

	public enum FunType {
		LINEAR, RELU, TANH, SIGMA
	}

	private FunType actFun = FunType.TANH;

	/**
	 * The activation function used. IF CHANGED: CHANGE DERIVATION ASWELL
	 * 
	 * @param netinput  netinput of the neuron
	 * @param threshold threshold of the neuron
	 * @return resulting activation between -1 and 1
	 */
	public double fAct(double netinput) {
		switch (this.actFun) {
		case RELU:
			return Math.max(netinput, 0);
		case TANH:
			return Math.tanh(netinput);
		case SIGMA:
			return (1 / (1 + Math.exp(-netinput)));
		default:
			return netinput;
		}
	}

	/**
	 * the derivation of activation funciton used.
	 * 
	 * @param netinput  netinput of the neuron
	 * @param threshold threshold of the neuron
	 * @return resulting activation between -1 and 1
	 */
	public double fActDeriv(double netinput) {
		switch (this.actFun) {
		case RELU:
			if (netinput >= 0) {
				return 1.0;
			}
			return 0.0;
		case TANH:
			return (1 - (Math.pow(Math.tanh(netinput), 2)));
		case SIGMA:
			return fAct(netinput) * (1 - fAct(netinput));
		default:
			return 1.0;
		}
	}

	public Neuron(int id, LinkedList<Synapsis> out) {
		this.id = id;
		if (id == 0) {
			netinput = 1;
			activation = 1;
		} else {
			netinput = 0;
			activation = fAct(netinput);
		}
		this.out = out;
		this.in = new LinkedList<Synapsis>();
	}

	public Neuron(int id) {
		this.id = id;
		if (id == 0) {
			netinput = 1;
			activation = 1;
		} else {
			netinput = 0;
			activation = fAct(netinput);
		}
		this.out = new LinkedList<Synapsis>();
		this.in = new LinkedList<Synapsis>();
	}

	public Neuron(int id, LinkedList<Synapsis> out, FunType actFun) {
		this.id = id;
		this.actFun = actFun;
		if (id == 0) {
			netinput = 1;
			activation = 1;
		} else {
			netinput = 0;
			activation = fAct(netinput);
		}
		this.out = out;
		this.in = new LinkedList<Synapsis>();
	}

	public Neuron(int id, FunType actFun) {
		this.id = id;
		this.actFun = actFun;
		if (id == 0) {
			netinput = 1;
			activation = 1;
		} else {
			netinput = 0;
			activation = fAct(netinput);
		}
		this.out = new LinkedList<Synapsis>();
		this.in = new LinkedList<Synapsis>();
	}

	public double getActivation() {
		return activation;
	}

	public void setActivation(double activation) {
		this.activation = activation;
	}

	public void updateActivation() {
		this.activation = fAct(this.netinput);
	}

	public LinkedList<Synapsis> getOut() {
		return out;
	}

	/**
	 * sets synapses this neuron influences. WARNING: i.id < o for all o in output
	 * of i!!
	 * 
	 * @param out new output list of this neuron
	 */
	public void setOut(LinkedList<Synapsis> out) {
		this.out = out;
	}
	
	public void addOut(Synapsis syn) {
		this.out.add(syn);
	}

	public LinkedList<Synapsis> getIn() {
		return in;
	}

	public void setIn(LinkedList<Synapsis> in) {
		this.in = in;
	}

	public void addIn(Synapsis syn) {
		this.in.add(syn);
	}

	/**
	 * Identification Number of Neuron, starts at 0
	 */
	public int getId() {
		return id;
	}

	public FunType getActFun() {
		return actFun;
	}

	public void setActFun(FunType actFun) {
		this.actFun = actFun;
	}

	public double getNetinput() {
		return netinput;
	}

	public void setNetinput(double netinput) {
		this.netinput = netinput;
	}

	public void addNetinput(double netinput) {
		this.netinput += netinput;
	}

	/**
	 * returns the neurons output based on its activation. Atm this uses the
	 * identity function
	 * 
	 * @return output of the neuron
	 */
	public double getOutput() {
		return this.getActivation();
	}

	public boolean isBias() {
		return id == 0;
	}

	/**
	 * returns true if and only if all of the parameters of the neurons are equal
	 * 
	 * @param neuron
	 * @return
	 */
	public boolean equals(Neuron neuron) {
		boolean result = getOut().equals(neuron.getOut()) && getId() == neuron.getId()
				&& getActivation() == neuron.getActivation() && getNetinput() == neuron.getNetinput();
		return result;
	}

	public Neuron clone() {
		LinkedList<Synapsis> outClone = new LinkedList<Synapsis>();
		for (Synapsis synapsis : this.getOut()) {
			outClone.add(synapsis.clone());
		}
		Neuron clone = new Neuron(this.id, outClone);
		clone.setActivation(getActivation());
		clone.setNetinput(getNetinput());
		return clone;
	}
	
	@Override
	public String toString() {
		return "hi";
	}
}
