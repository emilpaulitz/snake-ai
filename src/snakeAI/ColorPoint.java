package snakeAI;

import java.awt.Point;
import java.awt.Color;

@SuppressWarnings("serial")
public class ColorPoint extends Point {
	private Color color;
	
	public ColorPoint(int x, int y, Color col) {
		this.x = x;
		this.y = y;
		this.color = col;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
