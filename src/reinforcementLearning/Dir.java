package reinforcementLearning;

import java.awt.Point;

public enum Dir {
	NORTH,
	EAST,
	SOUTH,
	WEST,
	ERROR;

	public void translateInPlace(Point head) {
		switch (this) {
		case NORTH:
			head.translate(0, -1);
			break;
		case EAST:
			head.translate(1, 0);			
			break;
		case SOUTH:
			head.translate(0, 1);			
			break;
		case WEST:
			head.translate(-1, 0);			
			break;
		default:
			break;
		}
	}
	
	public Point translate(Point head) {
		switch (this) {
		case NORTH:
			return new Point(head.x, head.y - 1);
		case EAST:
			return new Point(head.x + 1, head.y);
		case SOUTH:
			return new Point(head.x, head.y + 1);
		case WEST:
			return new Point(head.x - 1, head.y);
		default:
			return new Point(0,0);
		}
	}
	
	public int toInt() {
		switch (this) {
		case EAST:
			return 0;
		case SOUTH:
			return 1;
		case WEST:
			return 2;
		case NORTH:
			return 3;
		default:
			return -1;
		}
	}
	
	public static Dir fromInt(int i) {
		switch (i) {
		case 0:
			return EAST;
		case 1:
			return SOUTH;
		case 2:
			return WEST;
		case 3:
			return NORTH;
		default:
			return ERROR;
		}
	}
	
	public Dir increase() {
		switch (this) {
		case EAST:
			return SOUTH;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		case NORTH:
			return EAST;
		default:
			return ERROR;
		}
	}
	
	public static Dir baseCase() {
		return fromInt(0);
	}
	
	public static Dir max() {
		return fromInt(3);
	}
}
