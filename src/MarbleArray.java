import java.util.ArrayList;

// An array of marbles with extra advance function
public class MarbleArray {
	
	private Marble[] marbles;
	private ArrayList<Coordinates> forbiddenPos;
	private ArrayList<Coordinates> fixedMarbles;
	private ArrayList<XORMarble> xorPos;
	private int fixedUpperIndex;
	private int xorUpperIndex;
	
	public void advance() {
		//Advance non-restricted marbles (only once all XOR configurations have been tested)
		if(allXORPositionsTested()) {
			if(!endPosition(marbles.length-1) && !marbles[marbles.length-1].isXOR()) {
				do {
					marbles[marbles.length-1] = marbles[marbles.length-1].advance();
				} while(!checkPosition(marbles[marbles.length-1]));
			} else if(marbles.length-1 != xorUpperIndex){
				for(int i = marbles.length-1; i > xorUpperIndex; i--) {
					if(!endPosition(i)) {
						do {
							marbles[i] = marbles[i].advance();
						} while(!checkPosition(marbles[i]));
						for(int j = i+1; j < marbles.length; j++) {
							marbles[j] = marbles[j-1].advance();
							while(!checkPosition(marbles[j])) {
								marbles[j] = marbles[j].advance();
							}
						}
						break;
					}
				}
			}
		}
		
		//Advance XOR marbles
		if(xorUpperIndex >= 0) {
			for(int i = xorUpperIndex; i > fixedUpperIndex; i--) {
				do {
					marbles[i] = marbles[i].advance();
				}while(!checkPosition(marbles[i]));
				marbles[i].xorFlipFlag();
				if(marbles[i].wasMovedLastTime()) {
					break;
				}
			}
		}
		
	}
	
	public Marble access(int index) {
		return marbles[index];
	}
	
	public int length() {
		return marbles.length;
	}
	
	private boolean checkPosition(Marble m) {
		if(m.isFixed()) {
			return fixedMarbles.contains(m.getArrayCoordinates());
		} else if(m.isXOR()) {
			return checkXORPositions(m.getArrayCoordinates());
		} else {
			Coordinates test = m.getArrayCoordinates();
			return !forbiddenPos.contains(test) && !fixedMarbles.contains(test) && !checkXORPositions(test);
		}
	}
	
	private boolean allXORPositionsTested() {
		boolean result = true;
		for(int i = xorUpperIndex; i > fixedUpperIndex; i--) {
			result = result && marbles[i].wasMovedLastTime();
		}
		return result;
	}
	
	private boolean checkXORPositions(Coordinates test) {
		if(xorPos != null) {
			for(int i = 0; i < xorPos.size(); i++) {
				if(test.equals(xorPos.get(i).getFirstPosition()) || test.equals(xorPos.get(i).getSecondPosition())) {
					return true;
				}
			}
			
			return false;
		}
		return false;
	}
	
	private boolean endPosition(int index) {
		if(marbles[index].isFixed()) {
			return true;
		} else if(marbles[index].isXOR()) {
			return marbles[index].wasMovedLastTime();
		} else if(index > xorUpperIndex) {
			if(!marbles[index].endOfBoard()) {
				Marble temp = new Marble(marbles[index].getArrayX(), marbles[index].getArrayY(), marbles[index].getDimension());
				temp = temp.advance();
				while(!checkPosition(temp)) {
					temp = temp.advance();
					if(temp.endOfBoard()) {
						return true;
					}
				}
				return false;
			} else {
				return true;
			}
		} else {
			System.out.println("MarbleArray.endPosition(index): supplied index was out of bounds. Returning true by default.");
			return true;
		}
	}
	
	public boolean finalPosition() {
		boolean  result = endPosition(marbles.length-1);
		for(int i = marbles.length-2; i > xorUpperIndex; i--) {
			boolean temp = endPosition(i);
			result = result && temp;
			if(!result) {
				break;
			}
		}
		if(result) {
			result = result && allXORPositionsTested();
		}
		
		return result;
	}
	
	//Constructor for brute-forcing. No restrictions on marble positions
	public MarbleArray(int length, int boardDim) {
		forbiddenPos = new ArrayList<Coordinates>();
		fixedMarbles = new ArrayList<Coordinates>();
		fixedUpperIndex = -1;
		xorPos = new ArrayList<XORMarble>();
		xorUpperIndex = -1;
		if(length >= 1) {
			marbles = new Marble[length];
			marbles[0] = new Marble(0, 0, boardDim);
			for(int i = 1; i < length; i++) {
				marbles[i] = marbles[i-1].advance();
			}
		}	
	}
	
	//Constructor for preprocessed input.
	public MarbleArray(int length, int boardDim, ArrayList<Coordinates> forbidden, ArrayList<Coordinates> fixed, ArrayList <XORMarble> xor) {
		forbiddenPos = forbidden;
		fixedMarbles = fixed;
		xorPos = xor;
		if(length >= 1) {
			marbles = new Marble[length];
			int i = 0;
			//This works, because the sum of the list sizes for fixed and XOR-marbles should always be <= the size of the marble array
			for(; i < fixedMarbles.size(); i++) {
				marbles[i] = new Marble(fixedMarbles.get(i).getFirst(), fixedMarbles.get(i).getSecond(), boardDim, true);
			}
			fixedUpperIndex = i-1;
			for(i = 0; i < xorPos.size(); i++) {
				marbles[fixedUpperIndex+1+i] = new Marble(xorPos.get(i), boardDim, false);
			}
			xorUpperIndex = fixedUpperIndex+i;
			if(xorUpperIndex < marbles.length-1) {
				i = 1;
				marbles[xorUpperIndex+i] = new Marble(0,0, boardDim);
				while(!checkPosition(marbles[xorUpperIndex+i])) {
					marbles[xorUpperIndex+i] = marbles[xorUpperIndex+i].advance();
				}
				for(i++; xorUpperIndex+i < length; i++) {
					marbles[xorUpperIndex+i] = marbles[xorUpperIndex+i-1].advance();
					while(!checkPosition(marbles[xorUpperIndex+i])) {
						marbles[xorUpperIndex+i] = marbles[xorUpperIndex+i].advance();
					}
				}
			}
		}
	}

}
