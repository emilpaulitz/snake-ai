package snakeAI;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;

public class HardCodeAI {

	private int numberOfCols = 30;
	private int maxX = 30;
	private int maxY = 14;

	private Point head;
	private Point food;

	private LinkedList<Point> tail;

	public HardCodeAI() {
	}

	public int dir(double[] input, int prevDir) {
		// [x + y * numberOfCols]
		// 30 x 14
		double[] fstHalf = new double[input.length / 2];
		double[] sndHalf = new double[input.length / 2];

		for (int i = 0; i < input.length / 2; i++) {
			fstHalf[i] = input[i];
			sndHalf[i] = input[fstHalf.length + i];
		}

		head = arrToPoint(sndHalf, 1).getFirst();
		tail = arrToPoint(fstHalf, -1);
		food = arrToPoint(fstHalf, 1).getFirst();
		
		if (head.getX() < food.getX()) {
			if (prevDir != 2 && wontDie(0)) {
				return 0;
			}
		}
		if (head.getX() > food.getX()) {
			if (prevDir != 0 && wontDie(2)) {
				return 2;
			}
		}
		if (head.getY() < food.getY()) {
			if (prevDir != 3 && wontDie(1)) {
				return 1;
			}
		}
		if (head.getY() > food.getY()) {
			if (prevDir != 1 && wontDie(3)) {
				return 3;
			}
		}
		
		if (head.getX() == food.getX() && prevDir != 2 && wontDie(0)) {
			return 0;
		}
		if (head.getX() == food.getX() && prevDir != 0 && wontDie(2)) {
			return 2;
		}
		if (head.getY() == food.getY() && prevDir != 3 && wontDie(1)) {
			return 1;
		}
		if (head.getY() == food.getY() && prevDir != 1 && wontDie(3)) {
			return 3;
		}
		
		int[] areaLeft = new int[4];
		for (int i = 0; i <= 3; i++) {
			if (wontDie(i) && !isOpposite(i, prevDir)) {
				areaLeft[i] = calcAreaLeft(i);
			}
		}
		System.out.println(Arrays.toString(areaLeft));
		return Static.argmax(areaLeft);
		
	}

	public boolean isOpposite(int dir, int prevDir) {
		switch (dir) {
		case 0:
			return prevDir == 2;
		case 1:
			return prevDir == 3;
		case 2:
			return prevDir == 0;
		case 3:
			return prevDir == 1;
		default:
			return false;
		}
	}

	public boolean wontDie(int dir) {
		switch (dir) {
		case 0:
			return !tail.contains(new Point(head.x + 1, head.y)) && head.x + 1 < maxX;
		case 1:
			return !tail.contains(new Point(head.x, head.y + 1)) && head.y + 1 < maxY;
		case 2:
			return !tail.contains(new Point(head.x - 1, head.y)) && head.x > 0;
		case 3:
			return !tail.contains(new Point(head.x, head.y - 1)) && head.y > 0;
		}
		return false;
	}

	public boolean wontDie(Point point, int dir) {
		switch (dir) {
		case 0:
			return !tail.contains(new Point(point.x + 1, point.y)) && point.x + 1 < maxX;
		case 1:
			return !tail.contains(new Point(point.x, point.y + 1)) && point.y + 1 < maxY;
		case 2:
			return !tail.contains(new Point(point.x - 1, point.y)) && point.x > 0;
		case 3:
			return !tail.contains(new Point(point.x, point.y - 1)) && point.y > 0;
		}
		return false;
	}

	public LinkedList<Point> arrToPoint(double[] inputHalf, int target) {
		LinkedList<Point> result = new LinkedList<Point>();
		int y = 0;
		for (int i = 0; i < inputHalf.length; i++) {
			if (inputHalf[i] == target) {
				y = (int) (i / numberOfCols);
				result.add(new Point(i - (y * numberOfCols), y));
			}
		}
		return result;
	}

	public int calcAreaLeft(int dir) {
		LinkedList<Point> acc = new LinkedList<Point>();
		LinkedList<Point> queue = new LinkedList<Point>();

		Point point = nextPoint(head, dir);
		acc.add(point);
		queue.add(point);

		while (!queue.isEmpty()) {
			point = queue.pop();
			for (int i = 0; i <= 3; i++) {
				if (wontDie(point, i) && !acc.contains(nextPoint(point, i)) && !point.equals(head)) {
					Point next = nextPoint(point, i);
					queue.add(next);
					acc.add(next);
				}
			}
		}

		return acc.size();
	}

	public static void movePoint(Point point, int dir) {
		switch (dir) {
		case 0:
			point.translate(1, 0);
			break;
		case 1:
			point.translate(0, 1);
			break;
		case 2:
			point.translate(-1, 0);
			break;
		case 3:
			point.translate(0, -1);
			break;
		default:
			break;
		}
	}

	public static Point nextPoint(Point point, int dir) {
		switch (dir) {
		case 0:
			return new Point(point.x + 1, point.y);
		case 1:
			return new Point(point.x, point.y + 1);
		case 2:
			return new Point(point.x - 1, point.y);
		case 3:
			return new Point(point.x, point.y - 1);
		default:
			return new Point();
		}
	}
}
