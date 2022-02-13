package reinforcementLearning;

import java.awt.Point;
import java.util.LinkedList;

public class State implements Cloneable {
	private Point foodLoc;
	private LinkedList<Point> body;
	private Point head;
	private int boardSizeX;
	private int boardSizeY;
	
	public State(){
	}
	
	public State(Point foodLoc, LinkedList<Point> body, Point head, int boardSizeX, int boardSizeY) {
		this.foodLoc = foodLoc;
		this.body = body;
		this.head = head;
		this.boardSizeX = boardSizeX;
		this.boardSizeY = boardSizeY;
	}	
	
	public String toString() {
		return "Food: (" + Integer.toString(foodLoc.x) + ", " + Integer.toString(foodLoc.y) + ")\n"
				+ "Head: (" + Integer.toString(head.x) + ", " + Integer.toString(head.y) + ")\n";
	}
	
	public State deepcopy() {
		LinkedList<Point> newBody = new LinkedList<Point>(body);
		State newState = new State(new Point(foodLoc.x, foodLoc.y),
				newBody,
				new Point(head.x, head.y),
				this.boardSizeX,
				this.boardSizeY);
		
		return newState;
	}
	
	public State clone() throws CloneNotSupportedException {
        return this.clone();
    }
	
	public Point getFoodLoc() {
		return foodLoc;
	}
	public void setFoodLoc(Point foodLoc) {
		this.foodLoc = foodLoc;
	}
	
	public LinkedList<Point> getBody() {
		return body;
	}
	
	public void setBody(LinkedList<Point> body) {
		this.body = body;
	}
	
	public Point getHead() {
		return head;
	}
	
	public void setHead(Point head) {
		this.head = head;
	}	
	
	public int getBoardSizeX() {
		return boardSizeX;
	}

	public void setBoardSizeX(int boardSizeX) {
		this.boardSizeX = boardSizeX;
	}

	public int getBoardSizeY() {
		return boardSizeY;
	}

	public void setBoardSizeY(int boardSizeY) {
		this.boardSizeY = boardSizeY;
	}
}
