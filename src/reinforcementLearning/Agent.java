package reinforcementLearning;

import java.awt.Point;
import java.util.LinkedList;

import reinforcementLearning.MoveTree.EndOFTreeException;

public class Agent {
	
	// TODO: nächster Schritt: pruning und mehr Schritte in die Zukunft rechnen
	
	State currState;
	double gamma = 0.1;
	int T = 3;
	
	// temporal
	public Dir[] bestDirs = new Dir[T];
	public double[] rewards = new double[T];
	
	public Agent(State currState){
		this.currState = currState;
	}

	public Agent(State currState, double gamma, int T){
		this.currState = currState;
		this.gamma = gamma;
		this.T = T;
	}
	
	private static int l1(Point p1, Point p2) {
		return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
	}
	
	public double calcReward(Dir direction, State currState) {
		Point newHead = direction.translate(currState.getHead());
		
		// death
		if (currState.getBody().contains(newHead) || newHead.x < 1 || newHead.x > currState.getBoardSizeX()
				|| newHead.y < 2 || newHead.y > currState.getBoardSizeY() + 1) {
			return Double.NEGATIVE_INFINITY;
		}
		
		// food
		if (newHead.equals(currState.getFoodLoc())) {
			return 100.0;
		}
		
		double reward = 0.0;
		
		// reward coming closer to food
		reward += l1(currState.getHead(), currState.getFoodLoc()) - l1(newHead, currState.getFoodLoc());
		
		// penalize each body part close to head
		for (Point bodyPart : currState.getBody()) {
			int dist = l1(bodyPart, newHead);
			
			if (dist == 1) {
				reward -= 1;
			}
			
			if (dist == 2) {
				reward -= 0.5;
			}
		}
		
		return reward;
	}
	
	public Dir nextMove() throws Exception {
		
		MoveTree possMoves = new MoveTree(T); // depth = T
		
		// for each combination of moves, calculate state and corresponding reward
		double maxReward = Double.NEGATIVE_INFINITY;
		Dir bestNextDir = Dir.baseCase();
		
		Dir[] dirArr = possMoves.getCurr();
		
		while(!possMoves.reachedEnd()) {
			
			boolean pruned = false;
			double reward = 0.0;
			State tempState = this.currState.deepcopy();
			
			// calculate reward for this set of moves
			for (int i = 0; i < dirArr.length; i++) { // TODO: not recalculate each path
				
				// calculate reward
				reward += calcReward(dirArr[i], tempState) * (Math.pow(gamma, i));
				if (Double.isInfinite(reward)) {
					possMoves.prune(i);
					pruned = true;
					break;
				}
				
				// translate body
				tempState.getBody().pollLast();
				tempState.getBody().addFirst((Point) tempState.getHead().clone()); 
				
				// translate head
				dirArr[i].translateInPlace(tempState.getHead());
			}
			
			if (reward > maxReward){
				
				maxReward = reward;				
				bestNextDir = dirArr[0];
			}
			
			if (!pruned) {
				dirArr = possMoves.getNext();
			} else {
				dirArr = possMoves.getCurr();				
			}
		}
		
		return bestNextDir;
	}
	
	public void updateFood(Point foodLoc) {
		this.currState.setFoodLoc(foodLoc);
	}
	
	public void updateHead(Point head) {
		this.currState.setHead(head);
	}
	
	public State getState() {
		return this.currState;
	}

	public int getT() {
		return T;
	}
}
















