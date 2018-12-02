import java.util.ArrayList;

// An array of marbles with extra advance function
public class MarbleArray {
	
	private Marble[] marbles;
	private ArrayList<Coordinates> forbiddenPos;
	private ArrayList<Coordinates> fixedMarbles;
	private ArrayList<XORMarble> xorPos;
	
	public void advance() {
		if(!endPosition(marbles.length-1)) {
			do {
				marbles[marbles.length-1] = marbles[marbles.length-1].advance();
			} while(!checkPosition(marbles[marbles.length-1]));
		} else {
			for(int i = marbles.length-1; i >= 0; i--) {
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
			Coordinates test = m.advance().getArrayCoordinates();
			return !forbiddenPos.contains(test) && !fixedMarbles.contains(test) && !checkXORPositions(test);
		}
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
		}else if(index == marbles.length-1) {
			return marbles[index].endOfBoard();
		} else if(index < marbles.length-1 && index >= 0) {
			return (marbles[index].advance().equals(marbles[index+1]) && (!marbles[index+1].isXOR() || !marbles[index+1].isFixed()));
		} else {
			System.out.println("MarbleArray.endPosition(index): supplied index was out of bounds. Returning true by default.");
			return true;
		}
	}
	
	public boolean finalPosition() {
		boolean  result = endPosition(0);
		for(int i = 1; i < marbles.length; i++) {
			boolean temp = endPosition(i);
			result = result && temp;
			if(!result) {
				break;
			}
		}
		
		return result;
	}
	
	public MarbleArray(int length, int boardDim) {
		if(length >= 1) {
			marbles = new Marble[length];
			marbles[0] = new Marble(0, 0, boardDim);
			for(int i = 1; i < length; i++) {
				marbles[i] = marbles[i-1].advance();
			}
		}	
	}

}
