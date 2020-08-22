package snakeAI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class Pause {

	private boolean pause = false;

	public boolean changed = false;

	private boolean typing = false;
	
	private Menu.AIOptions aiOption;

	String name = "Emil";

	JFrame frame;

	pauseKeyAdapter keys;

	public String getName() {
		return name;
	}

	public Menu.AIOptions getAiOption() {
		return aiOption;
	}

	public void setAiOption(Menu.AIOptions aiOption) {
		this.aiOption = aiOption;
	}

	public boolean isPause() {
		return pause;
	}

	public void pause() {
		this.pause = true;
		this.changed = true;
	}

	public void unpause() {
		this.pause = false;
		frame.removeKeyListener(keys);
		keys = null;
		changed = true;
		typing = false;
	}

	public boolean isTyping() {
		return typing;
	}

	public void addKeyListener(JFrame frame) {
		keys = new pauseKeyAdapter();
		frame.addKeyListener(keys);
		this.frame = frame;
	}

	class pauseKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				unpause();
				break;
			case KeyEvent.VK_ENTER:
				switch (aiOption) {
				case AIBackprop:
					unpause();
					typing = true;
					break;
				case Player:
					if (!typing) {
						typing = true;
						name = "";
					}
					break;
				default:
					break;
				}
				break;
			case KeyEvent.VK_A:
				if (typing) {
					name += "a";
				}
				break;
			case KeyEvent.VK_B:
				if (typing) {
					name += "b";
				}
				break;
			case KeyEvent.VK_C:
				if (typing) {
					name += "c";
				}
				break;
			case KeyEvent.VK_D:
				if (typing) {
					name += "d";
				}
				break;
			case KeyEvent.VK_E:
				if (typing) {
					name += "e";
				}
				break;
			case KeyEvent.VK_F:
				if (typing) {
					name += "f";
				}
				break;
			case KeyEvent.VK_G:
				if (typing) {
					name += "g";
				}
				break;
			case KeyEvent.VK_H:
				if (typing) {
					name += "h";
				}
				break;
			case KeyEvent.VK_I:
				if (typing) {
					name += "i";
				}
				break;
			case KeyEvent.VK_J:
				if (typing) {
					name += "j";
				}
				break;
			case KeyEvent.VK_K:
				if (typing) {
					name += "k";
				}
				break;
			case KeyEvent.VK_L:
				if (typing) {
					name += "l";
				}
				break;
			case KeyEvent.VK_M:
				if (typing) {
					name += "m";
				}
				break;
			case KeyEvent.VK_N:
				if (typing) {
					name += "n";
				}
				break;
			case KeyEvent.VK_O:
				if (typing) {
					name += "o";
				}
				break;
			case KeyEvent.VK_P:
				if (typing) {
					name += "p";
				}
				break;
			case KeyEvent.VK_Q:
				if (typing) {
					name += "q";
				}
				break;
			case KeyEvent.VK_R:
				if (typing) {
					name += "r";
				}
				break;
			case KeyEvent.VK_S:
				if (typing) {
					name += "s";
				}
				break;
			case KeyEvent.VK_T:
				if (typing) {
					name += "t";
				}
				break;
			case KeyEvent.VK_U:
				if (typing) {
					name += "u";
				}
				break;
			case KeyEvent.VK_V:
				if (typing) {
					name += "v";
				}
				break;
			case KeyEvent.VK_W:
				if (typing) {
					name += "w";
				}
				break;
			case KeyEvent.VK_X:
				if (typing) {
					name += "x";
				}
				break;
			case KeyEvent.VK_Y:
				if (typing) {
					name += "y";
				}
				break;
			case KeyEvent.VK_Z:
				if (typing) {
					name += "z";
				}
				break;
			case KeyEvent.VK_BACK_SPACE:
				if (typing) {
					name = name.substring(0, name.length()-1);
				}
			default:
				break;
			}
		}
	}
}
