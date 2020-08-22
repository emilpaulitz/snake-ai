package snakeAI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class Menu extends JPanel {

	private boolean shown = true;

	public enum AIOptions {
		HardCodeAI, AIEvolution, AIBackprop, Player
	};

	private AIOptions ai;

	private boolean randomFoodGeneration = true;

	private int width, height;
	
	menuKeyAdapter keys;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isShown() {
		return shown;
	}

	public AIOptions getAi() {
		return ai;
	}

	private void setAi(AIOptions ai) {
		this.ai = ai;
		this.repaint();
	}

	public boolean isRandomFoodGeneration() {
		return randomFoodGeneration;
	}

	private void setRandomFoodGeneration(boolean randomFoodGeneration) {
		this.randomFoodGeneration = randomFoodGeneration;
		shown = false;
		this.repaint();
	}

	public Menu(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void addKeyListener(JFrame frame) {
		keys = new menuKeyAdapter();
		frame.addKeyListener(keys);
	}

	public void paint(Graphics g) {
		if (this.ai == null) {
			g.setColor(Color.black);
			g.fillRect(0, 0, width, height);

			paintButton(g, "Press 'Q' to train the AI using Backpropagation!", width / 3, height / 7, width / 3, height / 5);
			paintButton(g, "Press 'A' to make the AI evolve!", width / 3, 3 * height / 7, width / 3, height / 5);
			paintButton(g, "Press 'S' to play yourself!", width / 3, 5 * height / 7, width / 3, height / 5);
		} else {
			g.setColor(Color.black);
			g.fillRect(0, 0, width, height);
			g.setColor(Color.white);
			g.setFont(new Font(null, 0, height/20));
			paintButton(g, "Generate food randomly (press 'a')", width / 3, height / 5, width / 3, height / 5);
			paintButton(g, "Generate food in a fixed pattern (press 's')", width / 3, 3 * height / 5, width / 3,
					height / 5);
		}
	}

	public void paintButton(Graphics g, String str, int x, int y, int width, int height) {
		g.setColor(Color.white);
		g.fillRect(x, y, width, height);

		g.setColor(Color.black);
		g.setFont(new Font(null, 0, (int) (1.5 * width / str.length())));
		g.drawString(str, x + (width / 8), y + (int) (height / 1.8));
	}
	
	class menuKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				if (shown) {
					if (ai == null) {
						setAi(AIOptions.AIEvolution);
					} else {
						setRandomFoodGeneration(true);
					}
				}
				break;
			case KeyEvent.VK_Q:
				if (shown) {
					if (ai == null) {
						setAi(AIOptions.AIBackprop);
					}
				}
				break;
			case KeyEvent.VK_W:
				if (shown) {
					if (ai == null) {
						setAi(AIOptions.HardCodeAI);
					}
				}
				break;
			case KeyEvent.VK_S:
				if (shown) {
					if (ai == null) {
						setAi(AIOptions.Player);
					} else {
						setRandomFoodGeneration(false);
					}
				}
				break;
			default:
				break;
			}
		}
	}
}
