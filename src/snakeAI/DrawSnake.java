package snakeAI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.LinkedList;

import evolution.PlayerBase;
import evolution.SnakeGame;

@SuppressWarnings("serial")
public class DrawSnake extends SnakeGame implements Static {
	private Color tileCol = new Color(0, 210, 51);
	
	public LinkedList<ColorPoint> colTiles = new LinkedList<ColorPoint>(); 

	private int gameSpeed;

	public Color getTileCol() {
		return tileCol;
	}

	public int getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(int gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	public DrawSnake(PlayerBase players, Pause pause) {
		super(players, pause);
	}

	public DrawSnake(String name, Pause pause) {
		super(name, pause);
	}

	public void paint(Graphics g) {
		if (getPause() != null && getPause().isPause()) {
			switch (getPause().getAiOption()) {
			case AIBackprop:
				g.setColor(Color.black);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.white);
				int bstringY = getHeight() / 5, bfontSize = getHeight() / 30, bstringX = getWidth() / 10;
				g.setFont(new Font(null, 0, bfontSize));
				g.drawString("Paused!", bstringX, bstringY);
				if (!isAIplaying()) {
					g.drawString("If you want to end recording lessons, press 'Enter'", bstringX, bstringY + bfontSize);
				}
				g.drawString("To resume, press 'Esc'", bstringX, bstringY + (2 * bfontSize));
				break;
			case Player:
				g.setColor(Color.black);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.white);
				int stringY = getHeight() / 5, fontSize = getHeight() / 30, stringX = getWidth() / 10;
				g.setFont(new Font(null, 0, fontSize));
				g.drawString("Paused!", stringX, stringY);
				g.drawString("If you want to change your name, press 'Enter'", stringX, stringY + fontSize);
				g.drawString("To resume, press 'Esc'", stringX, stringY + (2 * fontSize));
				g.drawString("Current Player: " + getPause().getName(), stringX, stringY + (3 * fontSize));
				break;
			default:
				break;
			}
		} else {
			// Paint background
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());

			// Paint scoreboard
			// Leaderboard:
			g.setColor(Color.black);
			g.setFont(new Font(null, 0, getTileSize() / 4));
			for (int i = 0; i < getHighScores().length; i++) {
				g.drawString(getHighScores()[i].toString(), getWidth() / 2, getTileSize() * (3 - i) / 4);
			}
			// Score
			g.setFont(new Font(null, 0, 25));
			g.drawString("Apples eaten: " + getScore(), getTileSize(), getTileSize() * 3 / 5);
			// "Highscores: "
			g.drawString("Highscores: ", getWidth() / 3, getTileSize() * 3 / 5);
			// name
			g.drawString(getPlayer().getName(), getWidth() * 2 / 3, getTileSize() * 3 / 5);
			// gamespeed
			int speed = 0;
			if (gameSpeed == 0) {
				speed = -1;
			} else {
				speed = (int) Static.log2(gameSpeed);
			}
			speed = 10 - speed;
			g.drawString("Speed: " + speed, getWidth() * 5 / 6, getTileSize() * 3 / 5);

			// Paint walls
			for (int x = 0; x < getWidth(); x += getTileSize()) {
				if (x == 0 || x == getWidth() - getTileSize()) {
					for (int y = getScoreHeight(); y < getHeight(); y += getTileSize()) {
						paintWall(g, x, y);
					}
				} else {
					paintWall(g, x, getScoreHeight());
					paintWall(g, x, getHeight() - getTileSize());
				}
			}

			// Paint field
			for (int x = getTileSize(); x < getWidth() - getTileSize(); x += getTileSize()) {
				for (int y = getScoreHeight() + getTileSize(); y < getHeight() - getTileSize(); y += getTileSize()) {
					paintTile(g, x, y);
				}
			}
			
			// paint colored tiles if specified
			for (ColorPoint p : colTiles) {
				paintTile(g, p.x * getTileSize(), p.y * getTileSize(), p.getColor());
			}

			// Paint food
			paintFood(g, getFoodLoc().x * getTileSize(), getFoodLoc().y * getTileSize());

			// Paint snake
			for (Point p : getSnakeBody()) {
				paintSnakeBody(g, p.x * getTileSize(), p.y * getTileSize());
			}
			paintSnakeHead(g, getSnakeHead().x * getTileSize(), getSnakeHead().y * getTileSize(), getSnakeDir());
		}
	}

	public void paintWall(Graphics g, int x, int y) {
		g.setColor(Color.darkGray);
		g.fillRect(x, y, getTileSize(), getTileSize());
	}

	public void paintFood(Graphics g, int x, int y) {
		g.setColor(Color.red);
		g.fillOval(x + 2, y + 2, getTileSize() - 4, getTileSize() - 4);
		int diameter = getTileSize() / 5;
		g.setColor(new Color(60, 40, 20));
		g.fillRect(x + getTileSize() / 2 - 2, y + 4 - getTileSize() / 5, 4, diameter);
		g.setColor(new Color(0, 100, 0));
		g.fillOval(x + getTileSize() / 2 + 2, y - diameter / 2, diameter, diameter);
	}

	public void paintTile(Graphics g, int x, int y) {
		g.setColor(getTileCol());
		g.fillRect(x + 2, y + 2, getTileSize() - 4, getTileSize() - 4);
	}
	
	public void paintTile(Graphics g, int x, int y, Color col) {
		g.setColor(col);
		g.fillRect(x + 2, y + 2, getTileSize() - 4, getTileSize() - 4);
	}

	public void paintSnakeBody(Graphics g, int x, int y) {
		g.setColor(Color.black);
		g.fillRect(x, y, getTileSize(), getTileSize());
		g.setColor(Color.yellow);
		int radius = 10;
		g.fillOval(x + getTileSize() / 2 - radius, y + getTileSize() / 2 - radius, 2 * radius, 2 * radius);
	}

	public void paintSnakeHead(Graphics g, int x, int y, int dir) {
		g.setColor(Color.black);
		g.fillRect(x, y, getTileSize(), getTileSize());
		g.setColor(Color.green);
		paintSnakeFace(g, x, y, dir);
	}

	public void paintSnakeFace(Graphics g, int x, int y, int dir) {
		int radius = 4, closeDist = getTileSize() / 6 + radius;
		int tgLen = getTileSize() / 3, tgCut = tgLen * 2 / 3, tgBroadth = getTileSize() / 5;
		Point eye1 = new Point(), eye2 = new Point(), tg = null;
		Polygon p = null;

		switch (dir) {
		case 0:
			eye2.x = eye1.x = x + getTileSize() - closeDist - 2 * radius;
			eye1.y = y + getTileSize() / 3 - radius;
			eye2.y = y + 2 * getTileSize() / 3 - radius;
			tg = new Point(x + getTileSize(), y + getTileSize() * 2 / 5);
			p = new Polygon(new int[] { tg.x, tg.x, tg.x + tgLen, tg.x + tgCut, tg.x + tgLen },
					new int[] { tg.y, tg.y + tgBroadth, tg.y + tgBroadth, tg.y + tgBroadth / 2, tg.y }, 5);
			break;
		case 1:
			eye2.y = eye1.y = y + getTileSize() - closeDist - 2 * radius;
			eye1.x = x + getTileSize() / 3 - radius;
			eye2.x = x + 2 * getTileSize() / 3 - radius;
			tg = new Point(x + getTileSize() * 2 / 5, y + getTileSize());
			p = new Polygon(new int[] { tg.x, tg.x + tgBroadth, tg.x + tgBroadth, tg.x + tgBroadth / 2, tg.x },
					new int[] { tg.y, tg.y, tg.y + tgLen, tg.y + tgCut, tg.y + tgLen }, 5);
			break;
		case 2:
			eye2.x = eye1.x = x + closeDist;
			eye1.y = y + getTileSize() / 3 - radius;
			eye2.y = y + 2 * getTileSize() / 3 - radius;
			tg = new Point(x, y + getTileSize() * 2 / 5);
			p = new Polygon(new int[] { tg.x, tg.x, tg.x - tgLen, tg.x - tgCut, tg.x - tgLen },
					new int[] { tg.y, tg.y + tgBroadth, tg.y + tgBroadth, tg.y + tgBroadth / 2, tg.y }, 5);
			break;
		case 3:
			eye2.y = eye1.y = y + closeDist;
			eye1.x = x + getTileSize() / 3 - radius;
			eye2.x = x + 2 * getTileSize() / 3 - radius;
			tg = new Point(x + getTileSize() * 2 / 5, y);
			p = new Polygon(new int[] { tg.x, tg.x + tgBroadth, tg.x + tgBroadth, tg.x + tgBroadth / 2, tg.x },
					new int[] { tg.y, tg.y, tg.y - tgLen, tg.y - tgCut, tg.y - tgLen }, 5);
			break;
		}

		// paint eyes
		g.fillOval(eye1.x, eye1.y, 2 * radius, 2 * radius);
		g.fillOval(eye2.x, eye2.y, 2 * radius, 2 * radius);

		// paint tongue
		g.setColor(Color.red);
		g.fillPolygon(p);
	}
}
