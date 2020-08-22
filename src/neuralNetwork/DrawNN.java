package neuralNetwork;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import snakeAI.ColorPoint;

@SuppressWarnings("serial")
public class DrawNN extends Canvas {

	private Player player;

	private NN toDraw;

	private ColorPoint[] locs;

	@SuppressWarnings("unused")
	private int id = 0, width = 1500, height = 800, step = 0;

	private boolean drawInput;
	
	private Font standard;
	
	private Font caption;
	
	public DrawNN(Player player, boolean drawInput) {
		this.player = player;
		toDraw = player.getBrain();
		locs = new ColorPoint[toDraw.getNeuronNumber()];
		this.drawInput = drawInput;
	}

	public void step(Canvas canvas, double[] input) {
		toDraw.stepTopo(input);
		setId(0);
		canvas.repaint();
		step++;
	}
	
	public void changePlayer (Player player) {
		this.player = player;
		this.toDraw = player.getBrain();
	}

	public NN getToDraw() {
		return toDraw;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void paint(Graphics g) {
		int neuronsPerHiddenLayer = toDraw.getHidden() / toDraw.getLayers();
		int maxNeuronsPerLayer = Math.max(toDraw.getOutput(), neuronsPerHiddenLayer);
		if (drawInput) {
			maxNeuronsPerLayer = Math.max(toDraw.getInput() + 1, maxNeuronsPerLayer);
		}
		int spacesHoriz = width / (2 * (toDraw.getLayers() + 2) + 1);
		int spacesVert = height / (2 * maxNeuronsPerLayer + 1);
		int diameter = Math.min(spacesHoriz, spacesVert);

		standard = g.getFont();
		caption = new Font(null, 0, (int) (diameter / 3.5));
		
		int y = 0;		
		// Calculate graphic representation for input Neurons and bias
		if (drawInput) {
			for (int i = 1; i <= toDraw.getInput() + 1; i++) {
				y = spacesVert * (2 * i + maxNeuronsPerLayer - toDraw.getInput() - 2);
				locs[getId()] = new ColorPoint(spacesHoriz, y, actColor(getId()));
				setId(getId() + 1);
			}
		} else { // if input won't get drawn, bias neuron locs also lie in locs[0]	
			locs[0] = new ColorPoint(spacesHoriz, (height + diameter) / 2, actColor(0));
			setId(toDraw.getInput() + 1);
		}
		
		// Calculate graphic representation for hidden Neurons
		for (int i = 1; i <= toDraw.getLayers(); i++) {
			for (int j = 1; j <= neuronsPerHiddenLayer; j++) {
				y = spacesVert * (2 * j - 1 + (int) (maxNeuronsPerLayer - neuronsPerHiddenLayer));
				locs[getId()] = new ColorPoint(spacesHoriz * (i * 2 + 1), y, actColor(getId()));
				setId(getId() + 1);
			}
		}

		// Calculate graphic representation for output Neurons
		for (int i = 1; i <= toDraw.getOutput(); i++) {
			y = spacesVert * (2 * i - 1 + (int) (maxNeuronsPerLayer - toDraw.getOutput()));
			locs[getId()] = new ColorPoint(width - 2 * spacesHoriz, y, actColor(getId()));
			setId(getId() + 1);
		}
		setId(0);

		// prepare for drawing or not drawing the input neurons
		int start = 1;
		if (!drawInput) {
			start = toDraw.getInput() + 1;
		}

		// Paint Synapses
		int offset = diameter / 2;
		paintSynapses(g, 0, offset);
		for (int i = start; i < locs.length; i++) {
			paintSynapses(g, i, offset);
		}

		// Paint Neurons
		paintNeuron(g, 0, diameter);
		for (int i = start; i < locs.length; i++) {
			paintNeuron(g, i, diameter);
		}

		// Draw player name
		g.setColor(Color.white);
		g.setFont(caption);
		g.drawString(player.getName(), 0, 30);
	}

	public void paintSynapses (Graphics g, int i, int offset) {
		for (Synapsis syn : toDraw.getNeuron(i).getOut()) {
			g.setColor(weightColor(syn.getWeight()));
			g.drawLine(locs[i].x + offset, locs[i].y + offset,
					locs[syn.getEnd().getId()].x + offset, locs[syn.getEnd().getId()].y + offset);
		}
	}
	
	public void paintNeuron (Graphics g, int i, int diameter) {
		g.setColor(locs[i].getColor());
		g.fillOval(locs[i].x, locs[i].y, diameter, diameter);
		g.setFont(standard);
		g.drawString(Double.toString(toDraw.getNeuron(i).getActivation()), locs[i].x,
				locs[i].y + (int) (1.2 * diameter));
		g.setColor(Color.black);
		g.setFont(caption);
		String caption = Integer.toString(i);
		if (i == 0) {
			caption = "BIAS";
		}
		g.drawString(caption, locs[i].x + (int) (diameter * 0.1),
				locs[i].y + (int) (0.6 * diameter));
		g.setFont(standard);
	}
	/**
	 * function to calculate the color of a synapsis depending on its weight
	 * 
	 * @param weight
	 *            weight of the synapsis
	 * @return color to be used: either green(ish) or red(ish)
	 */
	public Color weightColor(double weight) {
		if (weight > 0) {
			return new Color(0, fun(weight), 0);
		}
		return new Color(fun(-1 * weight), 0, 0);
	}

	/**
	 * function that returns strength of color depending on weight of synapsis
	 * 
	 * @param x
	 *            weight of the synapsis
	 * @return strength of color
	 */
	public static int fun(double x) {
		return 255 - (int) Math.pow(Math.E, Math.log(255) - (x));
	}

	/**
	 * calculates color of the neuron depending on its activation
	 * 
	 * @param id
	 *            id number of the neuron (needed to retrieve its activation)
	 * @return Color to be used (either red(ish) oder blue(ish))
	 */
	public Color actColor(int id) {
		double activation = toDraw.getNeurons()[id].getActivation();
		if (activation < -1 || activation > 1) {
			activation = Math.tanh(activation);
		}
		int intensity = 0;
		Color color;
		if (activation < 0) {
			intensity = (int) (255 * (-1) * activation);
			color = new Color(255 - intensity, 255 - intensity, 255);
		} else {
			intensity = (int) (255 * activation);
			color = new Color(255, 255 - intensity, 255 - intensity);
		}
		return color;
	}
}
