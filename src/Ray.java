
//Ray that moves across the board and reports whether it's absorbed or where it exits if it does
public class Ray {
	
	private Board board;
	private Coordinates curPos;
	private int start;
	private boolean nextIsWallorMarble;
	
	private enum Direction{DOWN, LEFT, UP, RIGHT}
	private Direction dir;
	
	//Checks special cases and only if they don't apply, creates sub-rays to explore the board
	public Coordinates go() {
		
		switch(specialCases()) {
		case 0:
			//No special case, just go on
			break;
		case -1:
			//The ray never even reaches the board, return empty Pair
			return new Coordinates();
		case 1:
			//The ray gets reflected right back outside
			return new Coordinates(start, start);
		case 2:
			//The ray starts out on a marble
			return new Coordinates(start);
		default:
			System.out.println("Special case handling: Return value was invalid. Returning empty Pair.");
			return new Coordinates();
		}
		
		Ray ray = this;
		
		while(!ray.isNextWallorMarble()) {
			if(ray.checkAheadAndTurn()) {
				if(ray.isNextWallorMarble()) {
					break;
				}
				ray = ray.straight();
			}
		}
		
		Coordinates result = new Coordinates(start, ray.wallOrMarble());
		result.normalize();
		return result;
	}
	
	//Turn left
	private void left() {
		switch(dir) {
		case DOWN:
			dir = Direction.RIGHT;
			break;
		case LEFT:
			dir = Direction.DOWN;
			break;
		case UP:
			dir = Direction.LEFT;
			break;
		case RIGHT:
			dir = Direction.UP;
			break;
		}
	}
	
	//Turn right
	private void right() {
		switch(dir) {
		case DOWN:
			dir = Direction.LEFT;
			break;
		case LEFT:
			dir = Direction.UP;
			break;
		case UP:
			dir = Direction.RIGHT;
			break;
		case RIGHT:
			dir = Direction.DOWN;
			break;
		}
	}
	
	//Go straight (make new Ray)
	private Ray straight() {
		return new Ray(dir, calculateNextPosition(), this.nextIsWallorMarble, board);
	}
	
	public boolean isNextWallorMarble() {
		return nextIsWallorMarble;
	}
	
	//Returns whether the ray hit a marble(-1) or the wall of the board(the index of the exiting cell)
	public int wallOrMarble() {
		switch(dir) {
		case DOWN:
			if(curPos.getSecond()+1 < board.getDimension()) {
				return -1;
			} else {
				return board.getDimension()*3-curPos.getFirst();
			}
		case LEFT:
			if(curPos.getFirst()-1 >= 0) {
				return -1;
			} else {
				return board.getDimension()*4-curPos.getSecond();
			}
		case UP:
			if(curPos.getSecond()-1 >= 0) {
				return -1;
			} else {
				return curPos.getFirst()+1;
			}
		case RIGHT:
			if(curPos.getFirst()+1 < board.getDimension()) {
				return -1;
			} else {
				return curPos.getSecond()+1+board.getDimension();
			}
		default:
			System.out.println("wallOrMarble: Direction invalid. Returning marble by default.");
			return -1;
		}
	}
	
	//Turn the ray around when it can't go straight
	public boolean checkAheadAndTurn() {
		switch(dir) {
		case DOWN:
			if(curPos.getSecond()+1 < board.getDimension()) {
				if(board.getAtCoordinates(curPos.getFirst(), curPos.getSecond()+1).getContent() == "o") {
					nextIsWallorMarble = true;
					return true;
				} else if(curPos.getFirst()+1 < board.getDimension() && board.getAtCoordinates(curPos.getFirst()+1, curPos.getSecond()+1).getContent() == "o") {
					this.right();
					return false;
				} else if(curPos.getFirst()-1 >= 0 && board.getAtCoordinates(curPos.getFirst()-1, curPos.getSecond()+1).getContent() == "o") {
					this.left();
					return false;
				} else {
					return true;
				}
			} else {
				//Facing down and directly at a wall
				nextIsWallorMarble = true;
				return true;
			}
		case LEFT:
			if(curPos.getFirst()-1 >= 0) {
				if(board.getAtCoordinates(curPos.getFirst()-1, curPos.getSecond()).getContent() == "o") {
					nextIsWallorMarble = true;
					return true;
				} else if(curPos.getSecond()+1 < board.getDimension() && board.getAtCoordinates(curPos.getFirst()-1, curPos.getSecond()+1).getContent() == "o") {
					this.right();
					return false;
				} else if(curPos.getSecond()-1 >= 0 && board.getAtCoordinates(curPos.getFirst()-1, curPos.getSecond()-1).getContent() == "o") {
					this.left();
					return false;
				} else {
					return true;
				}
			} else {
				//Facing left and directly at a wall
				nextIsWallorMarble = true;
				return true;
			}
		case UP:
			if(curPos.getSecond()-1 >= 0) {
				if(board.getAtCoordinates(curPos.getFirst(), curPos.getSecond()-1).getContent() == "o") {
					nextIsWallorMarble = true;
					return true;
				} else if(curPos.getFirst()-1 >= 0 && board.getAtCoordinates(curPos.getFirst()-1, curPos.getSecond()-1).getContent() == "o") {
					this.right();
					return false;
				} else if(curPos.getFirst()+1 < board.getDimension() && board.getAtCoordinates(curPos.getFirst()+1, curPos.getSecond()-1).getContent() == "o") {
					this.left();
					return false;
				} else {
					return true;
				}
			} else {
				//Facing up and directly at a wall
				nextIsWallorMarble = true;
				return true;
			}
		case RIGHT:
			if(curPos.getFirst()+1 < board.getDimension()) {
				if(board.getAtCoordinates(curPos.getFirst()+1, curPos.getSecond()).getContent() == "o") {
					nextIsWallorMarble = true;
					return true;
				} else if(curPos.getSecond()-1 >= 0 && board.getAtCoordinates(curPos.getFirst()+1, curPos.getSecond()-1).getContent() == "o") {
					this.right();
					return false;
				} else if(curPos.getSecond()+1 < board.getDimension() && board.getAtCoordinates(curPos.getFirst()+1, curPos.getSecond()+1).getContent() == "o") {
					this.left();
					return false;
				} else {
					return true;
				}
			} else {
				//Facing right and directly at a wall
				nextIsWallorMarble = true;
				return true;
			}
		default:
			return true;
		}
	}
	
	//Special case handling only for first position
	private int specialCases() {
		//Ray starts out directly on a marble
		if(board.getAtCoordinates(curPos.getFirst(), curPos.getSecond()).getContent() == "o") {
			return 2;
		}
		
		switch(dir) {
		case DOWN:
		case UP:
			//Is there a marble both to the right and left of the Ray? If yes, the ray gets reflected 180° right away
			if(curPos.getFirst()-1 >= 0 && curPos.getFirst()+1 < board.getDimension() &&
				board.getAtCoordinates(curPos.getFirst()-1, curPos.getSecond()).getContent() == "o" &&
				board.getAtCoordinates(curPos.getFirst()+1, curPos.getSecond()).getContent() == "o") {
				return 1;
			} else if(curPos.getFirst()-1 >= 0 && board.getAtCoordinates(curPos.getFirst()-1, curPos.getSecond()).getContent() == "o" ||
					curPos.getFirst()+1 < board.getDimension() && board.getAtCoordinates(curPos.getFirst()+1, curPos.getSecond()).getContent() == "o") {
				//This is for a marble only to one side of the Ray. The ray never even reaches the board.
				return -1;
			} else {
				return 0;
			}
		case LEFT:
		case RIGHT:
			//Is there a marble both to the right and left of the Ray?
			if(curPos.getSecond()-1 >= 0 && curPos.getSecond()+1 < board.getDimension() &&
				board.getAtCoordinates(curPos.getFirst(), curPos.getSecond()+1).getContent() == "o" &&
				board.getAtCoordinates(curPos.getFirst(), curPos.getSecond()-1).getContent() == "o") {
				return 1;
			} else if(curPos.getSecond()-1 >= 0 && board.getAtCoordinates(curPos.getFirst(), curPos.getSecond()-1).getContent() == "o" ||
					curPos.getSecond()+1 < board.getDimension() && board.getAtCoordinates(curPos.getFirst(), curPos.getSecond()+1).getContent() == "o") {
				//This is for a marble only to one side of the Ray. The ray never even reaches the board.
				return -1;
			} else {
				return 0;
			}
		default:
			System.out.println("Special case handling: Direction invalid. Returning 0 by default.");
			return 0;
		}
	}
	
	//Public constructor to make the initial ray
	public Ray(int start, Board b) {
		board = b;
		this.start = start;
		nextIsWallorMarble = false;
		dir = calculateDirection(start);
		curPos = calculateStartingPosition(start, dir);
	}
	
	//Private constructor for creating successive rays
	private Ray(Direction facing, Coordinates to, boolean wall, Board b) {
		board = b;
		curPos = to;
		dir = facing;
		nextIsWallorMarble = wall;
	}
	
	//Calculate starting position from origin index
	private Coordinates calculateStartingPosition(int start, Direction dir) {
		
		switch(dir) {
		case DOWN:
			return new Coordinates(start-1, 0);
		case LEFT:
			return new Coordinates(board.getDimension()-1, start - board.getDimension() - 1);
		case UP:
			return new Coordinates(Math.abs(start-board.getDimension()*3), board.getDimension()-1);
		case RIGHT:
			return new Coordinates(0, Math.abs(start-board.getDimension()*4));
		default:
			System.out.println("Starting direction invalid, returning empty pair for position.");
			return new Coordinates();
		}
	
	}
	
	//Get the next position of the Ray based on the direction it is currently facing
	private Coordinates calculateNextPosition() {
		switch(dir) {
		case DOWN:
			return new Coordinates(curPos.getFirst(), curPos.getSecond()+1);
		case LEFT:
			return new Coordinates(curPos.getFirst()-1, curPos.getSecond());
		case UP:
			return new Coordinates(curPos.getFirst(), curPos.getSecond()-1);
		case RIGHT:
			return new Coordinates(curPos.getFirst()+1, curPos.getSecond());
		default:
			System.out.println("Current direction invalid. Cannot calculate next position. Returning empty Pair.");
			return new Coordinates();
		}
	}
	
	//Calculate the direction the ray starts out in
	private Direction calculateDirection(int start) {
		int indicator = (int)((start-1)/board.getDimension());
		
		switch(indicator) {
		case 0:
			return Direction.DOWN;
		case 1:
			return Direction.LEFT;
		case 2:
			return Direction.UP;
		case 3:
			return Direction.RIGHT;
		default:
			System.out.println("Starting index out of bounds, returning starting direction Down by default");
			return Direction.DOWN;
		}
	}

}
