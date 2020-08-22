package neuralNetwork;

public class Lesson {

	private double[] input;

	private double[] desiredOutput;

	public double[] getInput() {
		return input;
	}

	public void setInput(double[] input) {
		this.input = input;
	}

	public double[] getDesiredOutput() {
		return desiredOutput;
	}

	public void setDesiredOutput(double[] desiredOutput) {
		this.desiredOutput = desiredOutput;
	}

	public Lesson(double[] input, double[] output) {
		this.input = input;
		this.desiredOutput = output;
	}
	
	public Lesson() {
	}
}

