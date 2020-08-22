package neuralNetwork;

public class Backprop {

	public static double trainOncePrintLog(NN nn, TrainingSet ts, double learningRate) {

		double DeltaW, sum;
		double[] deltah = new double[nn.getNeuronNumber()];
		for (Lesson lesson : ts.getSet()) {

			// simulate outcome with given input
			nn.stepTopo(lesson.getInput());

			// calc deltas
			for (int j = nn.getNeuronNumber() - 1; j > nn.getInput(); j--) {
				Neuron neur_j = nn.getNeuron(j);
				if (nn.isOutput(neur_j)) {
					// delta_h = fAct' * (t_h - y_h)
					deltah[neur_j.getId()] = neur_j.fActDeriv(neur_j.getNetinput())
							* (lesson.getDesiredOutput()[neur_j.getId() - nn.getInput() - nn.getHidden() - 1]
									- neur_j.getOutput());
					System.out.println("Neuron " + j + " is an output neuron.\\\\ Desired output: "
							+ lesson.getDesiredOutput()[neur_j.getId() - nn.getInput() - nn.getHidden() - 1]);
					System.out.println(", current output: " + neur_j.getOutput());
					System.out.println("\\\\Derivation of activation function with current netinput: "
							+ neur_j.fActDeriv(neur_j.getNetinput()));
					System.out.println("\\\\$ \\Rightarrow \\delta_" + j + " = "
							+ neur_j.fActDeriv(neur_j.getNetinput()) + " \\cdot ("
							+ lesson.getDesiredOutput()[neur_j.getId() - nn.getInput() - nn.getHidden() - 1] + " - "
							+ neur_j.getOutput() + ") = $" + deltah[neur_j.getId()] + "\\\\");
				} else {
					// delta_h = fAct' * sum(über alle Nachfolger l) {delta_l * w_{hl}} ;
					sum = 0;
					String ssum = "";
					for (Synapsis synh : neur_j.getOut()) {
						sum += (deltah[synh.getEnd().getId()] * synh.getWeight());
						ssum += deltah[synh.getEnd().getId()] + " \\cdot " + synh.getWeight();
					}
					deltah[neur_j.getId()] = neur_j.fActDeriv(neur_j.getNetinput()) * sum;
					System.out.println("Neuron " + j + " is a hidden neuron.\\\\");
					System.out.println("From derivation of activation function with current netinput ("
							+ neur_j.fActDeriv(neur_j.getNetinput()) + ")");
					System.out.println("and the sum: " + ssum + " = " + sum);
					System.out.println(
							", $\\delta_" + j + "$ is calculated as their product: " + deltah[neur_j.getId()] + "\\\\");
				}
			}

			for (Neuron preNeuron : nn.getNeurons()) {
				// System.out.println("Current Neuron i: " + preNeuron.getId());
				for (Synapsis syn : preNeuron.getOut()) {
					Neuron sucNeuron = syn.getEnd();
					// System.out.println("\tCurrent Neuron j: " + sucNeuron.getId());
					// System.out.println("\tdelta already calculated: " +
					// deltah[sucNeuron.getId()]);

					// calculate Delta w as (rate)(output of preceding neuron)(delta h) and apply
					DeltaW = learningRate * preNeuron.getOutput() * deltah[sucNeuron.getId()];
					// System.out.println("=> Delta W = " + learningRate + " * " +
					// preNeuron.getOutput() + " * "
					// + deltah[sucNeuron.getId()] + " = " + DeltaW);
					// System.out.println("old syn weight: " + syn.getWeight());
					syn.addWeight(DeltaW);
					// System.out.println("=> new syn weight: " + syn.getWeight());
					int i = preNeuron.getId();
					int j = sucNeuron.getId();
					System.out.println("$\\Delta w_{" + j + i + "} = \\eta \\cdot o_" + i + " \\delta_" + j + " = "
							+ learningRate + " \\cdot " + preNeuron.getOutput() + " \\cdot " + deltah[sucNeuron.getId()]
							+ " = " + DeltaW);
					System.out.println("\\Rightarrow \\text{ new weight: }" + syn.getWeight() + "$\\\\");
				}
			}
		}

		return calcError(ts, nn);
	}
	
	public static double train(NN nn, TrainingSet ts, double learningRate, int epochs) {
		for (int i = 0; i < epochs; i++) {
			trainOnce(nn, ts, learningRate);
		}
		return calcError(ts, nn);
	}

	public static double trainOnce(NN nn, TrainingSet ts, double learningRate) {

		double DeltaW, sum;
		double[] deltah = new double[nn.getNeuronNumber()];

		for (Lesson lesson : ts.getSet()) {

			// simulate outcome with given input
			nn.stepTopo(lesson.getInput());

			// calc deltas
			for (int j = nn.getNeuronNumber() - 1; j >= 0; j--) {
				Neuron neur_j = nn.getNeuron(j);
				if (nn.isOutput(neur_j)) {
					// delta_h = fAct' * (t_h - y_h)
					deltah[neur_j.getId()] = neur_j.fActDeriv(neur_j.getNetinput())
							* (lesson.getDesiredOutput()[neur_j.getId() - nn.getInput() - nn.getHidden() - 1]
									- neur_j.getOutput());
				} else {
					// delta_h = fAct' * sum(über alle Nachfolger l) {delta_l * w_{hl}} ;
					sum = 0;
					for (Synapsis synh : neur_j.getOut()) {
						sum += (deltah[synh.getEnd().getId()] * synh.getWeight());
					}
					deltah[neur_j.getId()] = neur_j.fActDeriv(neur_j.getNetinput()) * sum;
				}
			}

			// change weights for every synapse
			for (Neuron preNeuron : nn.getNeurons()) {
				for (Synapsis syn : preNeuron.getOut()) {
					Neuron sucNeuron = syn.getEnd();

					// Delta w = (rate)(output of preceding neuron)(delta h)
					DeltaW = learningRate * preNeuron.getOutput() * deltah[sucNeuron.getId()];
					syn.addWeight(DeltaW);
				}
			}
		}

		return calcError(ts, nn);
	}

	public static double calcError(TrainingSet ts, NN nn) {
		double err = 0.0;

		for (Lesson lesson : ts.getSet()) {
			nn.stepTopo(lesson.getInput());
			err += calcError(lesson, nn);
		}

		return err;
	}

	public static double calcError(Lesson lesson, NN nn) {
		double sum = 0;
		for (int j = 0; j < lesson.getDesiredOutput().length; j++) {
			sum += Math.pow(lesson.getDesiredOutput()[j] - nn.getResult(j), 2);
		}
		return 0.5 * sum;
	}
}