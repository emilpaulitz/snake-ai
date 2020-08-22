package snakeAI;

import java.awt.Point;
import java.util.Arrays;

public interface Static {

	/**
	 * determines the argmax in the given array
	 */
	public static int argmax(double[] arr) {
		double max = arr[0];
		int argmax = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
				argmax = i;
			}
		}
		return argmax;
	}

	/**
	 * creates a copy of the matrix, crops it from the right-upper corner to the
	 * given point and returns it
	 * 
	 * @param arr
	 * @param p
	 * @return
	 */
	public static <T> T[][] crop(T[][] arr, Point p) {
		T[][] newArr = arr.clone();

		for (int i = 0; i < arr.length; i++) {
			newArr[i] = Arrays.copyOfRange(arr[i], 0, (int) p.getY() + 1);
		}

		return Arrays.copyOfRange(newArr, (int) p.getX(), newArr.length);
	}

	/**
	 * prints given array as a TeX table. Elements are converted to String via the
	 * ToString method of <T>. Set seq to "" if you only want the array printed
	 * without any row or col names.
	 */
	public static <T> String printAsTexTable(T[][] arr, String seq) {

		boolean printNames = !seq.equals("");

		String result = "\\begin{tabular}{|";
		int tableLength = arr.length;
		if (printNames)
			tableLength++;
		for (int i = 0; i < tableLength; i++) {
			result += "c|";
		}
		result += "}" + "\n" + "\\hline" + "\n";

		if (printNames) {
			result += "&";
			for (int i = 0; i < arr.length; i++) {
				result += seq.charAt(i);
				if (i + 1 < arr.length) {
					result += "&";
				}
			}
		}
		result += "\\" + "\\" + "\n" + "\\hline" + "\n";

		for (int i = 0; i < arr.length; i++) {
			if (printNames)
				result += seq.charAt(i) + "&";
			for (int j = 0; j < arr[i].length; j++) {
				if (arr[i][j] != null) {
					result += arr[i][j].toString();
				}

				if (j + 1 < arr[i].length) {
					result += "&";
				}
			}
			result += "\\" + "\\" + "\n" + "\\hline" + "\n";

		}
		result += "\\end{tabular}";
		return result;
	}

	public static String printAsTexTable(int[][] arr, String seq) {

		boolean printNames = !seq.equals("");

		String result = "\\begin{tabular}{|";
		int tableLength = arr.length;
		if (printNames)
			tableLength++;
		for (int i = 0; i < tableLength; i++) {
			result += "c|";
		}
		result += "}" + "\n" + "\\hline" + "\n";

		if (printNames) {
			result += "&";
			for (int i = 0; i < arr.length; i++) {
				result += seq.charAt(i);
				if (i + 1 < arr.length) {
					result += "&";
				}
			}
		}
		result += "\\" + "\\" + "\n" + "\\hline" + "\n";

		for (int i = 0; i < arr.length; i++) {
			if (printNames)
				result += seq.charAt(i) + "&";
			for (int j = 0; j < arr[i].length; j++) {
				result += arr[i][j];

				if (j + 1 < arr[i].length) {
					result += "&";
				}
			}
			result += "\\" + "\\" + "\n" + "\\hline" + "\n";

		}
		result += "\\end{tabular}";
		return result;
	}
	
	/**
	 * usage: 
	 * if (chance(0.6)){
	 * 	code
	 * }
	 */
	public static boolean chance (double prob) {
		return prob > Math.random();
	}
	
	public static double log2(double pot) {
		return (Math.log(pot) / Math.log(2));
	}

	public static int argmax(int[] arr) {
		int max = arr[0];
		int argmax = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
				argmax = i;
			}
		}
		return argmax;		
	}
}
