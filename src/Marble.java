
//Class for a single marble placed by the "solution generator".
//Has functionality to advance in place by one cell, or more if there's other marbles in the way.
public class Marble {
	
	private int boardDimensions;
	private int arrayX;
	private int arrayY;
	private boolean fixed;
	private XORMarble positions;
	private boolean xorMovedLastTime;
	
	public int getArrayX() {
		return arrayX;
	}
	
	public int getArrayY() {
		return arrayY;
	}
	
	public int getDimension() {
		return boardDimensions;
	}
	
	public Coordinates getGameCoordinates() {
		return new Coordinates(arrayX+1, arrayY+boardDimensions+1);
	}
	
	public Coordinates getArrayCoordinates() {
		return new Coordinates(arrayX, arrayY);
	}
	
	public boolean wasMovedLastTime() {
		return xorMovedLastTime;
	}
	
	public void xorFlipFlag() {
		if(positions != null) {
			xorMovedLastTime = !xorMovedLastTime;
		}
	}
	
	//Explicit setting of marble position
	public void setArrayCoordinates(Coordinates c) {
		if(c.getFirst() >= 0 && c.getSecond() >= 0 && c.getFirst() < boardDimensions && c.getSecond() < boardDimensions) {
			arrayX = c.getFirst();
			arrayY = c.getSecond();
		}
	}
	
	public boolean endOfBoard() {
		return ((arrayX == boardDimensions-1 && arrayY == boardDimensions-1) || this.fixed);
	}
	
	//Advance the marble by one cell to the right. Roll over into the next row if it would go out of bounds.
	public Marble advance() {
		if(!fixed) {
			if(positions == null) {
				int newX = new Integer(arrayX+1);
				int newY = new Integer(arrayY);
				if(newX == boardDimensions) {
					newX = 0;
					newY++;
					if(newY == boardDimensions) {
						newY = 0;
					}
				}
				
				return new Marble(newX, newY, boardDimensions);
			} else {
				return new Marble(new XORMarble(positions.getSecondPosition(), positions.getFirstPosition()), boardDimensions);
			}
		} else {
			return this;
		}
	}
	
	public boolean isXOR() {
		return !(positions == null);
	}
	
	public XORMarble getXORPositions() {
		return positions;
	}
	
	public boolean isFixed() {
		return fixed;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + arrayX;
		result = prime * result + arrayY;
		result = prime * result + boardDimensions;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Marble other = (Marble) obj;
		if (arrayX != other.arrayX)
			return false;
		if (arrayY != other.arrayY)
			return false;
		if (boardDimensions != other.boardDimensions)
			return false;
		return true;
	}
	
	public Marble(int x, int y, int d) {
		arrayX = x;
		arrayY = y;
		boardDimensions = d;
		fixed = false;
		positions = null;
		xorMovedLastTime = false;
	}
	
	public Marble(int x, int y, int d, boolean fix) {
		arrayX = x;
		arrayY = y;
		boardDimensions = d;
		fixed = fix;
		positions = null;
		xorMovedLastTime = false;
	}
	
	public Marble(XORMarble xorm, int d) {
		boardDimensions = d;
		positions = xorm;
		arrayX = positions.getFirstPosition().getFirst();
		arrayY = positions.getFirstPosition().getSecond();
		xorMovedLastTime = false;
	}

}
