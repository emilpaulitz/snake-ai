package snakeAI;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;

public class AStarAI {

	private int numberOfCols = 30;
	private int maxX = 29;
	private int maxY = 13;

	private Point head;
	private Point food;
	private Point curr;

	private LinkedList<Point> tail;

	private LinkedList<Point> plan = new LinkedList<Point>();
	private LinkedList<Point> viewed = new LinkedList<Point>();
	
	private int[][] dist = new int[maxX + 1][maxY + 1];

	public AStarAI() {
	}

	public LinkedList<Point> getPlan() {
		return plan;
	}

	public LinkedList<Point> getViewed() {
		return viewed;
	}

	public int dir(double[] input) {
		// [x + y * numberOfCols]
		// 30 x 14

		head = arrToPoint(input, input.length / 2, input.length / 2, 1).getFirst();
		tail = arrToPoint(input, 0, input.length / 2, -1);
		food = arrToPoint(input, 0, input.length / 2, 1).getFirst();

		// reset on death
		if (tail.isEmpty()) {
			resetDist();
		}

		// calculate a new plan
		if (plan.isEmpty()) {
			resetDist();
			curr = new Point(head);
			int min;
			int nextDir = 0;
			Point next = null;
			
			while (!curr.equals(food)) {

				// determine dist of neighbors and best neighbor
				min = Integer.MAX_VALUE;
				nextDir = -1;
				for (int dir = 0; dir < 4; dir++) {
					next = nextPoint(curr, dir);
					if (isWall(next)) {
						continue;
					}
					
					if (isOccupied(next)) {
						dist[next.x][next.y] = Integer.MAX_VALUE;
					} else if (dist[next.x][next.y] == 0 && !next.equals(food)) {
						dist[next.x][next.y] = Math.abs(next.x - food.x) + Math.abs(next.y - food.y);
					}
					
					if (dist[next.x][next.y] < min) {
						min = dist[next.x][next.y];
						nextDir = dir;
					}
				}
				
				// return if no option is feasible
				if (nextDir == -1) {
					System.err.println("All options are occupied at Point\n" + curr.toString());
					return 0;
				}
				next = nextPoint(curr, nextDir);
				System.out.println(next);

				// if this is last Point, perform backtracking
				if (!plan.isEmpty() && plan.getLast().equals(next)) {
					dist[curr.x][curr.y] += 2;
					curr = plan.getLast();
					continue;
				}

				// next Point is feasible: add it to the plan
				if (!curr.equals(head)) {
					plan.add(curr);
				}
				curr = next;
			}
			plan.add(food);
		}

		return pointsToDir(head, plan.poll());
	}

	public int dirStep(double[] input, int d) {
		// [x + y * numberOfCols]
		// 30 x 14

		// TODO implement viewed list
		head = arrToPoint(input, input.length / 2, input.length / 2, 1).getFirst();
		tail = arrToPoint(input, 0, input.length / 2, -1);
		food = arrToPoint(input, 0, input.length / 2, 1).getFirst();

		// reset on death
		if (tail.isEmpty()) {
			//resetDist();
		}

		// calculate a new plan
		if (plan.isEmpty() || d == -1) {
			if (plan.isEmpty()) {
				resetDist();
			}
			if (d != -1) {
				curr = new Point(head);
				viewed.clear();
			}
			
			int min;
			int nextDir = 0;
			Point next = null;

			while (!curr.equals(food)) {
				// determine dist of neighbors and best neighbor
				min = Integer.MAX_VALUE;
				nextDir = -1;
				for (int dir = 0; dir < 4; dir++) {
					next = nextPoint(curr, dir);
					if (isWall(next)) {
						continue;
					}
					if (isOccupied(next)) {
						dist[next.x][next.y] = Integer.MAX_VALUE;
					} else if (dist[next.x][next.y] == 0 && !next.equals(food)) {
						dist[next.x][next.y] = Math.abs(next.x - food.x) + Math.abs(next.y - food.y);
						if (!viewed.contains(next)) {
							viewed.add(next);
						}
					}
					if (dist[next.x][next.y] < min) {
						min = dist[next.x][next.y];
						nextDir = dir;
					}
				}
				
				System.out.println(nextDir);
				System.out.println(next);
				System.out.println(dist[next.x][next.y]);
				
				// return if no option is feasible
				if (nextDir == -1) {
					System.err.println("All options are occupied at Point\n" + curr.toString());
					return 0;
				}
				next = nextPoint(curr, nextDir);
				
				// extremely whacky
				dist[next.x][next.y] += 2; 
				
				// if this is last Point, perform backtracking
				if (!plan.isEmpty() && plan.getLast().equals(next)) {
					dist[curr.x][curr.y] += 2;
					curr = plan.getLast();
					continue;
				}

				// next Point is feasible: add current point to the plan
				if (!curr.equals(head)) {
					plan.add(curr);
				}

				curr = next;
				return -1;
			}
			
			if (!plan.getLast().equals(food)) {
				plan.add(food);
				return -1;
			}
		}

		return pointsToDir(head, plan.poll());
	}

	public int dist(Point p) {
		return Math.abs(p.x - food.x) + Math.abs(p.y - food.y);
	}

	public boolean isOpposite(int dir, int prevDir) {
		return Math.abs(dir - prevDir) == 2;
	}

	public LinkedList<Point> arrToPoint(double[] input, int start, int len, int target) {
		LinkedList<Point> result = new LinkedList<Point>();
		int y = 0;
		for (int i = 0; i < len; i++) {
			if (input[i + start] == target) {
				y = (int) (i / numberOfCols);
				result.add(new Point(i - (y * numberOfCols), y));
			}
		}
		return result;
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

	public boolean isOccupied(Point p) {
		return tail.contains(p) || p.x < 0 || p.x > maxX || p.y < 0 || p.y > maxY || (p.y == 9 && (p.x < 25 || p.x > 5));
	}
	public boolean isWall(Point p) {
		return p.x < 0 || p.x > maxX || p.y < 0 || p.y > maxY;
	}

	public int pointsToDir(Point start, Point end) {
		if (start.x == end.x) {
			if (start.y + 1 == end.y) {
				return 1;
			}
			if (start.y - 1 == end.y) {
				return 3;
			}
		}
		if (start.y == end.y) {
			if (start.x + 1 == end.x) {
				return 0;
			}
			if (start.x - 1 == end.x) {
				return 2;
			}
		}
		System.err.print("Points do not lie beneath each other!!\n");
		System.err.print(start.toString() + " \n");
		System.err.print(end.toString() + "\n");
		return -1;
	}
	
	public void resetDist() {
		for (int x = 0; x < dist.length; x++) {
			for (int y = 0; y < dist[y].length; y++) {
				dist[x][y] = 0; //Math.abs(x - food.x) + Math.abs(y - food.y);
			}
		}
	}
}
