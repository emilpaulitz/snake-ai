package neuralNetwork;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JFrame;
import evolution.EvPlayer;
import evolution.PlayerBase;
import neuralNetwork.Neuron.FunType;
import snakeAI.Static;

public class Testing {

	public static void main(String[] args) throws Exception {
		TrainingSet ts = new TrainingSet();
		ts.readLessons("C:\\Users\\emilp\\Documents\\Uni\\Neural Networks\\ass5\\MultiLayerANN\\evenParity_input.txt",
				"C:\\Users\\emilp\\Documents\\Uni\\Neural Networks\\ass5\\MultiLayerANN\\evenParity_target.txt");

		int correctPredictions = 0;
		// for (int i = 0; i < results.length; i++) {

		double[][] weights = new double[][] { { -0.56060501, 0.95724757, 0.6324495 }, { 0.08680988, -0.44326123 },
				{ -0.15096482, 0.68955226 }, { -0.99056229, -0.75686176 }, { 0.34149817, 0.65170551 },
				{ -0.72658682, 0.15018666 }, { 0.78264391, -0.58159576 }, { -0.62934356, -0.78324622 }, { 0.6233663 },
				{ -0.65611797 } };
		NN task = new NN(ts.getLesson(0).getInput().length, 5, 1, ts.getLesson(0).getDesiredOutput().length,
				FunType.SIGMA);

		JFrame nFrame = new JFrame("Encoder");
		DrawNN nn = new DrawNN(new Player("ENCODER", task), true);
		Canvas canvas = openNN(nFrame, nn);
		canvas.repaint();

		Backprop.train(task, ts, 0.1, 1000);

		correctPredictions = 0;
		for (int j = 0; j < ts.getSet().size(); j++) {

			task.stepTopo(ts.getLesson(j).getInput());/*
														 * int prediction = Static.argmax(task.getResult()); if
														 * (ts.getLesson(j).getDesiredOutput()[prediction] == 1) {
														 * correctPredictions++; }
														 */

			if (Math.abs(task.getResult()[0] - ts.getLesson(j).getDesiredOutput()[0]) < 0.5) {
				correctPredictions++;
			}
		}
		// results[i] = correctPredictions;
		// }
		// Arrays.sort(results);

		// System.out.println(results[0]);
		System.out.println(task.getNeuron(8).getOut().get(0).getWeight());
		System.out.println(correctPredictions + " / " + ts.getSet().size());

		canvas.repaint();
		/*
		 * 
		 * int inputNeurs = 13; NN task = new NN(inputNeurs, 2, 1, inputNeurs,
		 * FunType.SIGMA); JFrame nFrame = new JFrame("Encoder"); DrawNN nn = new
		 * DrawNN(new Player("ENCODER", task), true); Canvas canvas = openNN(nFrame,
		 * nn);
		 * 
		 * for (int i = 0; i < inputNeurs; i++) { double[] input = new
		 * double[inputNeurs]; input[i] = 1;
		 * 
		 * ts.addLesson(new Lesson(input, input)); } canvas.repaint();
		 * 
		 * System.out.println("Error before learning: " + Backprop.calcError(ts, task));
		 * double err = 1.0; // double[] errs = new double[2500]; int iter = 0; int
		 * restarts = 0; while (err > 0.5) { err = Backprop.trainOnce(task, ts, 0.2); //
		 * if (iter < 2500) { // errs[iter] = err; // } iter++; if (iter >= 1000000) {
		 * 
		 * //if (err < 1.1) { break; }
		 * 
		 * task = new NN(inputNeurs, 5, 1, inputNeurs, FunType.SIGMA); iter = 0;
		 * restarts++; // System.out.println(Arrays.toString(errs)); // errs = new
		 * double[25000]; System.out.println("restarting with error " + err + "..."); }
		 * } System.out.println("Error after restarting " + restarts +
		 * " times and learning " + iter + " iterations: " + Backprop.calcError(ts,
		 * task));
		 * 
		 * canvas.repaint(); while (true) { for (int i = 0; i < inputNeurs; i++) {
		 * Thread.sleep(1500); double[] input = new double[inputNeurs]; input[i] = 1;
		 * nn.step(canvas, input); canvas.repaint(); } } /* NN and = new NN(2, 2, 1, 1,
		 * Neuron.FunType.TANH); JFrame newFrame = new JFrame("Neural Network for AND");
		 * DrawNN dNN = new DrawNN(new Player("AND", and), true); Canvas newCanvas =
		 * openNN(newFrame, dNN); ts.setSet(new LinkedList<Lesson>()); ts.addLesson(new
		 * Lesson(new double[] { 1, 1 }, new double[] { 1 })); ts.addLesson(new
		 * Lesson(new double[] { 0.5, 0.5 }, new double[] { 0.5 })); ts.addLesson(new
		 * Lesson(new double[] { -1, -1 }, new double[] { -1 })); ts.addLesson(new
		 * Lesson(new double[] { 0, 0 }, new double[] { 0 }));
		 * 
		 * System.out.println(Backprop.calcError(ts, and)); for (int i = 0; i < 100000;
		 * i++) { Backprop.trainOnce(and, ts, 0.5); }
		 * System.out.println(Backprop.calcError(ts, and)); printResults(ts, and);
		 * 
		 * task.stepTopo(new double[] { 1, 0 }); newCanvas.repaint();
		 */
	}

	public static Canvas openNN(JFrame frame, DrawNN nn) {
		Canvas canvas = nn;
		canvas.setSize(canvas.getWidth(), canvas.getHeight());
		canvas.setBackground(Color.black);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
		return canvas;
	}

	public static void printWeights(NN nn) {
		for (int i = 0; i < nn.getNeuronNumber(); i++) {
			for (Synapsis syn : nn.getNeuron(i).getOut()) {
				System.out.print(syn.getWeight() + " ");
			}
			System.out.println();
		}
	}

	public static void printResults(TrainingSet ts, NN nn) {
		for (Lesson lesson : ts.getSet()) {
			nn.stepTopo(lesson.getInput());
			System.out.println(Arrays.toString(lesson.getInput()) + " returns: " + Arrays.toString(nn.getResult()));
		}
	}
}
