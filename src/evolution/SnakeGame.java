package evolution;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.JPanel;

import snakeAI.Pause;
import snakeAI.Static;

@SuppressWarnings("serial")
public class SnakeGame extends JPanel {

	private int width = 1600, height = 850, tileSize = 50, scoreHeight = 50, score = 0, steps = 0;

	// direction of coordinates: x: left to right, y: top to bottom
	private int boardSizeX = width / tileSize - 2, boardSizeY = height / tileSize - 3, initSize = 1;
	// => Board size: 30 x 14 (considering scoreboard and walls)

	private Point foodLoc = new Point(18, 12), snakeHead = new Point(19, 10);

	//private Point foodLoc = new Point(1, 2), snakeHead = new Point(30, 15);
	
	private LinkedList<Point> snakeBody = new LinkedList<Point>();

	/**
	 * direction of the snake: 0 = east; 1 = south; 2 = west; 3 = north
	 */
	private int snakeDir;

	private boolean AIplaying;

	private EvPlayer player;

	private PlayerBase base;

	private boolean playerChanged = false;

	/**
	 * Array of the players with highest scores achieved. Sorted: highest score is
	 * in highScores[0]
	 */
	private EvPlayer[] highScores = new EvPlayer[] { new EvPlayer("Leer", null), new EvPlayer("Leer", null),
			new EvPlayer("Leer", null) };

	private Pause pause;

	public SnakeGame(PlayerBase base, Pause pause) {
		this.base = base;
		this.player = base.getActive();
		this.AIplaying = true;
		this.pause = pause;
	}

	public SnakeGame(String name, Pause pause) {
		this.player = new EvPlayer(name, null);
		this.pause = pause;
		this.AIplaying = false;
	}

	public Pause getPause() {
		return pause;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTileSize() {
		return tileSize;
	}

	/**
	 * board: [1, boardSize (30)]
	 * 
	 * @return
	 */
	public int getBoardSizeX() {
		return boardSizeX;
	}

	/**
	 * board: [2, boardSize + 1 (15)]
	 * 
	 * @return
	 */
	public int getBoardSizeY() {
		return boardSizeY;
	}

	public int getScoreHeight() {
		return scoreHeight;
	}

	public static Point dirToPoint(int dir) {
		return new Point(-(dir - 1) % 2, -(dir - 2) % 2);
	}

	public int getScore() {
		return score;
	}

	public void incScore() {
		score++;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSnakeDir() {
		return snakeDir;
	}

	public void setSnakeDir(int snakeDir) {
		this.snakeDir = snakeDir;
	}

	public Point getSnakeHead() {
		return snakeHead;
	}

	public void setSnakeHead(Point snakeHead) {
		this.snakeHead = snakeHead;
	}

	public void translateSnakeHead(Point translation) {
		this.snakeHead.translate(translation.x, translation.y);
	}

	public LinkedList<Point> getSnakeBody() {
		return snakeBody;
	}

	public void setSnakeBody(LinkedList<Point> snakeBody) {
		this.snakeBody = snakeBody;
	}

	public Point getFoodLoc() {
		return foodLoc;
	}

	public void setFoodLoc(Point foodLoc) {
		this.foodLoc = foodLoc;
	}

	/**
	 * changes last segment to first and puts it at the given Point
	 * 
	 * @param newLoc the last postion of the head
	 */
	public void translateSnakeBody(Point newLoc) {
		if (snakeBody.size() == score + initSize) {
			this.snakeBody.removeLast();
		}
		this.snakeBody.addFirst(newLoc);
	}

	public boolean isPlayerChanged() {
		return playerChanged;
	}

	public boolean isAIplaying() {
		return AIplaying;
	}

	public void setAIplaying(boolean aIplaying) {
		AIplaying = aIplaying;
	}

	public EvPlayer getPlayer() {
		return player;
	}

	public void setPlayer(EvPlayer player) {
		this.player = player;
	}

	public EvPlayer[] getHighScores() {
		return highScores;
	}

	/**
	 * add the given Player to the leaderboard if its score is high enough and it is
	 * not already on there
	 * 
	 * @param player the player to add if matching the conditions
	 */
	public void addHighScore(EvPlayer player) {

		Arrays.sort(getHighScores());

		// Tests if the player is already on the leaderboard
		for (int i = 0; i < getHighScores().length; i++) {
			if (getHighScores()[i].equals(player)) {
				getHighScores()[i].addScore(player.getHighScore());
				Arrays.sort(getHighScores());
				return;
			}
		}

		if (player.getHighScore() > getHighScores()[0].getHighScore()) {
			highScores[0] = player;
		}

		Arrays.sort(getHighScores());
	}

	public int getSteps() {
		return steps;
	}

	/**
	 * returns the game state as an array twice as long as there are tiles. The
	 * board is read row by row: Any point (x,y) is stored in array[x + y *
	 * numberOfCols]. In the first half: -1 means snake body 1 means food 0 means
	 * nothing
	 * 
	 * In the second half: 1 means there is the head 0 means everything else
	 * 
	 * @return int[] as described above
	 */
	public double[] gameState() {
		double[] result = new double[2 * getBoardSizeX() * getBoardSizeY()];

		// inscribe body
		for (Point pointer : getSnakeBody()) {
			result[(int) (pointer.getX() - 1 + (pointer.getY() - 2) * getBoardSizeX())] = -1;
		}

		// inscribe food
		result[(int) (getFoodLoc().getX() - 1 + (getFoodLoc().getY() - 2) * getBoardSizeX())] = 1;

		// inscribe head
		result[(result.length / 2)
				+ (int) (getSnakeHead().getX() - 1 + (getSnakeHead().getY() - 2) * getBoardSizeX())] = 1;

		return result;
	}
	
	/**
	 * returns the game state as a double array of len 16. First 8 cells reflect (manhattan metric) dist
	 * to an obstacle, last 8 cells are 0 or 1 depending if target is in that direction. First of the 8
	 * means north, then clockwise in 45° steps.
	 * 
	 * @return int[] as described above
	 */
	public double[] gameStateDist() {
		double[] result = new double[16];

		// inscribe obstacles
		// arr of distances to walls
		int[] min = new int[8];
		min[0] = (int) getSnakeHead().getY() - 1;
		min[2] = (int) (getBoardSizeX() - getSnakeHead().getX() + 1);
		min[4] = (int) (getBoardSizeY() + 2 - getSnakeHead().getY());
		min[6] = (int) getSnakeHead().getX();
		min[1] = Math.min(min[0], min[2]);
		min[3] = Math.min(min[4], min[2]);
		min[5] = Math.min(min[4], min[6]);
		min[7] = Math.min(min[0], min[6]);
		
		int headX = (int) getSnakeHead().getX();
		int headY = (int) getSnakeHead().getY();
		
		// check if snake body is closer
		for (Point p : getSnakeBody()) {
			int x = (int) p.getX();
			int y = (int) p.getY();
			
			// north-south
			if (x == headX) {
				if (y > headY) { // south
					if (y - headY < min[4]) {
						min[4] = y - headY;
					}
				} else { // north 
					if (headY - y < min[0]) {
						min[0] = headY - y;
					}
				}
			}
			
			// east-west
			else if (y == headY) {
				if (x > headX) { // east
					if (x - headX < min[2]) {
						min[2] = x - headX;
					}
				} else { // west 
					if (headX - x < min[6]) {
						min[6] = headX - x;
					}
				}
			}
			
			// NW-SE
			else if (y - headY == x - headX) {
				if (x > headX) { // SE
					if (x - headX < min[3]) {
						min[3] = x - headX;
					}
				} else { // NW 
					if (headX - x < min[7]) {
						min[7 ] = headX - x;
					}
				}
			}
			
			// NE-SW
			else if (x - headX == headY - y) {
				
			}
		}
		
		// inscribe food
		
		// inscribe body
		for (Point pointer : getSnakeBody()) {
			result[(int) (pointer.getX() - 1 + (pointer.getY() - 2) * getBoardSizeX())] = -1;
		}

		// inscribe food
		result[(int) (getFoodLoc().getX() - 1 + (getFoodLoc().getY() - 2) * getBoardSizeX())] = 1;

		// inscribe head
		result[(result.length / 2)
				+ (int) (getSnakeHead().getX() - 1 + (getSnakeHead().getY() - 2) * getBoardSizeX())] = 1;

		return result;
	}

	/**
	 * calculates a step. If snake is not controlled via keyborad, dir argument will
	 * be ignored
	 */
	public void step(JPanel JPanel, int dir, boolean randomFoodGeneration) {

		// Set new direction
		if (AIplaying) {
			dir = Static.argmax(player.getBrain().getResult());
			if (Math.abs(dir - getSnakeDir()) == 2) {
				double[] res = player.getBrain().getResult();
				res[dir] = Double.NEGATIVE_INFINITY;
				dir = Static.argmax(res);
			}
		}
		if (Math.abs(dir - getSnakeDir()) != 2) {
			setSnakeDir(dir);
		}

		doStep(JPanel, randomFoodGeneration);
	}

	private void doStep(JPanel JPanel, boolean randomGeneration) {
		playerChanged = false;

		// update body, food, score and head
		Point oldHeadLoc = new Point(getSnakeHead());
		translateSnakeHead(dirToPoint(getSnakeDir()));

		// is food eaten?
		if (getSnakeHead().equals(foodLoc)) {
			incScore();
			resetFood(randomGeneration);
		}

		translateSnakeBody(oldHeadLoc);

		steps++;

		JPanel.repaint();

		// check if dead or if the maximum number of steps is exceeded (if ai playing)
		if (getSnakeBody().contains(getSnakeHead()) || getSnakeHead().x < 1 || getSnakeHead().x > getBoardSizeX()
				|| getSnakeHead().y < 2 || getSnakeHead().y > getBoardSizeY() + 1
				|| (AIplaying && getSteps() > 100 + 400 * getScore())) {
			death();
		}
	}

	public void resetFood(boolean randomGeneration) {
		Point point;
		if (randomGeneration) {
			point = new Point();
			do {
				point.move((int) (Math.random() * getBoardSizeX()) + 1, (int) (Math.random() * getBoardSizeY()) + 2);
			} while (getSnakeBody().contains(point) || point.equals(getSnakeHead()));
		} else {
			point = new Point(18 - getScore() * 4, 12);
			do {
				while (point.x < 1) {
					point.x += 4;
					point.y -= 4;
				}
				while (point.y < 2) {
					point.y += 4;
					point.x += 4;
				}
				while (point.x > 30) {
					point.x -= 4;
					point.y += 4;
				}
				while (point.y > 15) {
					point.y -= 4;
					point.x -= 4;
				}
			} while (point.x < 1 || point.y < 2 || point.x > 30 || point.y > 15);
		}
		setFoodLoc(point);

	}

	public void death() {
		// to reward direct pathing: subtract the number of steps needed to get to the
		// next apple from the score * 10
		setScore(getScore() * 10 - (int) (getSteps() * 0.1));

		// reach in the score
		player.addScore(getScore());
		addHighScore(getPlayer());

		// reset score, snake, food and steps
		setScore(0);
		setSnakeHead(new Point(19, 10));
		snakeBody.clear();
		setFoodLoc(new Point(18, 12));
		steps = 0;

		// change player
		switch (pause.getAiOption()) {
		case AIBackprop:
			playerChanged = true;
			break;
		case AIEvolution:
			player = base.next();
			playerChanged = true;
			break;
		default:
			break;
		}
	}
}
