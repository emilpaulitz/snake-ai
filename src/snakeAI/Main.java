package snakeAI;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import evolution.EvPlayer;
import evolution.PlayerBase;
import neuralNetwork.Backprop;
import neuralNetwork.DrawNN;
import neuralNetwork.Lesson;
import neuralNetwork.NN;
import neuralNetwork.TrainingSet;

public class Main {
	// TODO Ideen:

	// Für Backprop braucht man Beispiele -> Spieler spielen lassen oder
	// hardgecodete AI spielen lassen, jeden Frame als Lesson speichern und danach
	// lernen lassen.

	// Hard gecodete AI: Wenn nicht trivial, "Navi" einbauen, in welcher Richtung
	// das Essen überhaupt und wie schnell erreicht wird -> Graphentheorie, evtl.
	// Klassen aus Algo verwenden?

	// Schlange als Input Abstandssensoren verpassen (vllt auch Körpersensoren?)

	static int gameSpeed = 256;
	static int dir = 0;
	static Menu menu = new Menu(1600, 850);
	static Pause pause = new Pause();
	static boolean aiPause = false;
	mainKeyAdapter keys;

	public static void main(String[] args) throws Exception {

		Main main = new Main();

		// open main menu
		JFrame sFrame = new JFrame("Snake Game");
		JPanel JPanel = openMenu(sFrame, menu);
		main.addKeyListener(sFrame);

		while (menu.isShown()) {
			Thread.sleep(100);
		}

		pause.setAiOption(menu.getAi());

		// open snake window
		DrawSnake board = null;
		switch (menu.getAi()) {
		case HardCodeAI:
			board = new DrawSnake("NPC", pause);
			break;
		case AIBackprop:
			board = new DrawSnake("Teacher", pause);
			break;
		case AIEvolution:
			board = new DrawSnake(new PlayerBase(20, 30 * 14 * 2, 10, 2, 4), pause);
			break;
		case Player:
			board = new DrawSnake("Emil", pause);
			break;
		}

		JPanel.removeAll();
		JPanel = openSnake(sFrame, board);
		JPanel.repaint();

		// main loop
		switch (menu.getAi()) {
		case HardCodeAI:
			Thread.sleep(1500);

			//HardCodeAI ai = new HardCodeAI();
			AStarAI ai = new AStarAI();
			int d = 0;

			// main loop
			while (true) {
				Thread.sleep(gameSpeed);
				board.setGameSpeed(gameSpeed);
				if (!aiPause) {
					d = ai.dir(board.gameState());
					
					// TODO wahre Distanz und geschätzte Distanz ist nicht gleich 
					// -> wie war nochmal das Prinzip des A* damit umzugehen?
					if (d == -1) {
						board.colTiles.clear();
						for (Point p : ai.getViewed()) {
							board.colTiles.add(new ColorPoint(p.x + 1, p.y + 2, Color.lightGray));
						}
						for (Point p : ai.getPlan()) {
							board.colTiles.add(new ColorPoint(p.x + 1, p.y + 2, Color.blue));
						}
						for (int x = 6; x < 25; x++) {
							board.colTiles.add(new ColorPoint(x + 1, 9 + 2, Color.gray));
						}
						
						board.repaint();
					} else {
						board.step(JPanel, d, menu.isRandomFoodGeneration());
					}
				}
			}
		case AIBackprop:
			Thread.sleep(1500);
			boolean teaching = true;
			TrainingSet ts = new TrainingSet();

			HardCodeAI ai1 = new HardCodeAI();
			int d1 = 0;

			while (teaching) {
				Thread.sleep(gameSpeed);
				board.setGameSpeed(gameSpeed);
				if (!pause.isPause()) {
					if (pause.changed) {
						sFrame.removeKeyListener(main.keys);
						main.addKeyListener(sFrame);
						pause.changed = false;
					}
					d1 = ai1.dir(board.gameState(), d1);
					board.step(JPanel, d1, menu.isRandomFoodGeneration());

					// board.step(JPanel, dir, menu.isRandomFoodGeneration());
					double[] in = board.gameState();
					double[] out = new double[] { -1, -1, -1, -1 };
					out[d1] = 1;
					ts.addLesson(new Lesson(in, out));
					System.out.print("Number of lessons: ");
					System.out.println(ts.getSet().size());
				} else {
					if (pause.changed) {
						pause.addKeyListener(sFrame);
						pause.changed = false;
					}
					board.repaint();
				}
				if (pause.isTyping()) {
					teaching = false;
				}
			}

			board.setAIplaying(true);
			board.setPlayer(new EvPlayer("Student", new NN(30 * 14 * 2, 210, 1, 4)));

			JFrame bnFrame = new JFrame("Neural Network");
			DrawNN bnn = new DrawNN(board.getPlayer(), false);
			Canvas bcanvas = openNN(bnFrame, bnn);

			Thread.sleep(1500);

			while (true) {
				// simulate until death
				Thread.sleep(gameSpeed);
				board.setGameSpeed(gameSpeed);
				if (!pause.isPause()) {
					if (pause.changed) {
						sFrame.removeKeyListener(main.keys);
						main.addKeyListener(sFrame);
						pause.changed = false;
					}
					bnn.step(bcanvas, board.gameState());
					board.step(JPanel, dir, menu.isRandomFoodGeneration());

					if (board.isPlayerChanged()) {
						for (int i = 0; i < 1; i++) {
							Backprop.trainOnce(board.getPlayer().getBrain(), ts, 0.1);
							System.out.println("Lessons learned " + (i + 1) + " times!");
						}
						System.out.println(Backprop.calcError(ts, board.getPlayer().getBrain()));
					}
				} else {
					if (pause.changed) {
						pause.addKeyListener(sFrame);
						pause.changed = false;
					}
					board.repaint();
				}
			}

		case AIEvolution:
			JFrame nFrame = new JFrame("Neural Network");
			DrawNN nn = new DrawNN(board.getPlayer(), false);
			Canvas canvas = openNN(nFrame, nn);

			Thread.sleep(1500);

			// main loop
			while (true) {
				Thread.sleep(gameSpeed);
				board.setGameSpeed(gameSpeed);
				if (!aiPause) {
					nn.step(canvas, board.gameState());
					board.step(JPanel, dir, menu.isRandomFoodGeneration());
					if (board.isPlayerChanged()) {
						nn.changePlayer(board.getPlayer());
					}
				}
			}
		case Player:
			Thread.sleep(1500);

			// main loop
			while (true) {
				Thread.sleep(gameSpeed);
				board.setGameSpeed(gameSpeed);
				if (!pause.isPause()) {
					if (pause.changed) {
						sFrame.removeKeyListener(main.keys);
						main.addKeyListener(sFrame);
						pause.changed = false;
						if (pause.getName() != "") {
							board.setPlayer(new EvPlayer(pause.getName(), null));
						}
					}
					board.step(JPanel, dir, menu.isRandomFoodGeneration());
				} else {
					if (pause.changed) {
						pause.addKeyListener(sFrame);
						pause.changed = false;
					}
					board.repaint();
				}
			}
		}
	}

	public void addKeyListener(JFrame sFrame) {
		keys = new mainKeyAdapter();
		sFrame.addKeyListener(keys);
	}

	public static JPanel openMenu(JFrame frame, Menu menu) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel JPanel = menu;
		JPanel.setSize(JPanel.getWidth(), JPanel.getHeight());
		JPanel.setBackground(Color.white);
		frame.add(JPanel);
		frame.setSize(menu.getWidth() + 20, menu.getHeight() + 55);
		frame.setVisible(true);
		menu.addKeyListener(frame);
		return JPanel;
	}

	public static JPanel openSnake(JFrame frame, DrawSnake board) {
		JPanel JPanel = board;
		JPanel.setSize(JPanel.getWidth(), JPanel.getHeight());
		JPanel.setBackground(Color.white);
		frame.add(JPanel);
		frame.setVisible(true);
		return JPanel;
	}

	public static Canvas openNN(JFrame frame, DrawNN nn) {
		Canvas canvas = nn;
		canvas.setSize(canvas.getWidth(), canvas.getHeight());
		canvas.setBackground(Color.black);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
		return canvas;
	}

	public static void printInput(DrawNN nn) {
		System.out.println("body + food:");
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 30; j++) {
				System.out.print(nn.getToDraw().getNeurons()[j + i * 30].getActivation() + "| ");
			}
			System.out.println();
		}
		System.out.println("head:");
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 30; j++) {
				System.out.print(nn.getToDraw().getNeurons()[j + 30 * 14 + i * 30].getActivation() + "| ");
			}
			System.out.println();
		}
		System.out.println(nn.getToDraw().getNeurons().length);
	}

	class mainKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_PLUS:
				gameSpeed /= 2;
				break;
			case KeyEvent.VK_MINUS:
				if (gameSpeed == 0) {
					gameSpeed = 1;
				} else {
					gameSpeed *= 2;
				}
				break;
			case KeyEvent.VK_UP:
				dir = 3;
				break;
			case KeyEvent.VK_DOWN:
				dir = 1;
				break;
			case KeyEvent.VK_RIGHT:
				dir = 0;
				break;
			case KeyEvent.VK_LEFT:
				dir = 2;
				break;
			case KeyEvent.VK_ESCAPE:
				switch (menu.getAi()) {
				case HardCodeAI:
					aiPause = !aiPause;
					break;
				case AIBackprop:
					pause.pause();
					break;
				case AIEvolution:
					aiPause = !aiPause;
					break;
				case Player:
					pause.pause();
					break;
				}
				break;
			default:
				break;
			}
		}
	}
}
