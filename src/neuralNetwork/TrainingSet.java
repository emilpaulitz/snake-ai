package neuralNetwork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class TrainingSet {

	private LinkedList<Lesson> set = new LinkedList<Lesson>();

	public LinkedList<Lesson> getSet() {
		return set;
	}

	public Lesson getLesson(int i) {
		return set.get(i);
	}

	public void setSet(LinkedList<Lesson> set) {
		this.set = set;
	}

	public void addLesson(Lesson lesson) {
		set.add(lesson);
	}

	/**
	 * Read lessons given in two separate txt Files, different lessons in different
	 * lines and different values separated by a space
	 * 
	 * @param input path to the input file
	 * @param output path to the desired output file
	 * @throws IOException
	 */
	public void readLessons(String input, String output) throws IOException {
		BufferedReader readerIn = new BufferedReader(new FileReader(input));
		BufferedReader readerOut = new BufferedReader(new FileReader(output));
		
		String lineIn = readerIn.readLine();
		String lineOut = readerOut.readLine();
		
		String[] inputVals;
		String[] outputVals;
		
		while (lineIn != null && lineOut != null) {			
			inputVals = lineIn.replace('\t', ' ').split(" ");
			outputVals = lineOut.replace('\t', ' ').split(" ");
			
			double[] in = new double[inputVals.length];
			double[] out = new double[outputVals.length];

			for (int i = 0; i < inputVals.length; i++) {
				in[i] = Double.parseDouble(inputVals[i]);
			}
			for (int i = 0; i < outputVals.length; i++) {
				out[i] = Double.parseDouble(outputVals[i]);
			}
			
			this.addLesson(new Lesson(in, out));
			
			lineIn = readerIn.readLine();
			lineOut = readerOut.readLine();
		}
		
		readerIn.close();
		readerOut.close();
	}
}