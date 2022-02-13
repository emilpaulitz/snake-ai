package reinforcementLearning;

import java.util.Arrays;



public class MoveTree {
	
	public class EndOFTreeException extends Exception { 
	    public EndOFTreeException(String errorMessage) {
	        super(errorMessage);
	    }
	}

	private Dir[] currPos;
	private int depth = 3;
	private boolean endOfTree = false;
	
	public MoveTree(){
		this.currPos = new Dir[depth];
		resetCurrPos();
	}
	
	public MoveTree(int depth) throws Exception{
		if (depth < 2) {
			throw new Exception("Depth too small");
		}
		this.currPos = new Dir[depth];
		resetCurrPos();
		
		this.depth = depth;
	}
		
	public Dir[] getCurr() {
		return currPos;
	}
	
	/*
	 * Warning: if end of tree is reached, this will return the baseCase again. 
	 * However, reachedEnd() will return true in that case
	 */
	public Dir[] getNext() {

		// increase currPos
		int pointer = currPos.length - 1;
		currPos[pointer] = currPos[pointer].increase();
		
		if (currPos[pointer] == Dir.baseCase()) { // carry over occurred
			
			pointer--;
			while (currPos[pointer] == Dir.max()) { // carry over until one is not Dir.max()
				
				if (pointer == 0) {
					endOfTree = true;
					resetCurrPos();
					return currPos;
				}
				
				currPos[pointer] = Dir.baseCase();
				pointer--;
			}
			
			currPos[pointer] = currPos[pointer].increase(); // increase first that is not max()
		}
		
		// return increase currPos
		return currPos;
	}
	
	private void resetCurrPos() {
		Arrays.fill(currPos, Dir.baseCase());
	}

	public void prune(int d) {
		
		for (int i = d + 1; i < currPos.length; i++) { // reset all directions below d
			currPos[i] = Dir.baseCase();
		}
		
		currPos[d] = currPos[d].increase();
		
		if (currPos[d] == Dir.baseCase()) { // carry over occurred
			
			do {  // carry over until one is not Dir.max()
				
				if (d == 0) { // if all are max, we reached the end of the tree
					endOfTree = true;
					resetCurrPos();
					return;
				}
				
				currPos[d] = Dir.baseCase(); // not harmful
				d--;
				
			} while (currPos[d] == Dir.max());
			
			currPos[d] = currPos[d].increase(); // increase first that is not max()
		}	
		
	}
	
	public boolean reachedEnd() {
		return this.endOfTree;
	}
}
