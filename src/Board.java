
public class Board {
	private int dimension;
	
	private Cell[][] board;
	
	public Cell getAtCoordinates(int x, int y) {
		return board[x][y];
	}
	
	//How to output in ASCII in O(n^2) (well, not that there's much choice)
	private String toASCII() {
		//Start new string with one leading space (top left diagonal)
		String result = new String(" ");
		
		//Add top column numbers to output
		for(int n = 0; n < dimension; n++) {
			result = result.concat(String.valueOf(n+1));
			if(n == dimension-1) {
				//Add top right diagonal empty space and line break;
				result = result.concat(" \n");
			}
		}
		
		//Main board with row numbers to left and right
		for(int i = 0; i < dimension; i++) {
			for(int j = 0; j < dimension; j++) {
				if(j == 0) {
					int leftLineNumber = (dimension*4)-i;
					result = result.concat(String.valueOf(leftLineNumber));
				}
				
				result = result.concat(board[j][i].getContent());
				
				if(j == dimension - 1) {
					int rightLineNumber = dimension+i+1;
					result = result.concat(String.valueOf(rightLineNumber));
					result = result.concat("\n");
				}
			}
		}
		
		//Add bottom line numbers to output
		for(int m = 0; m < dimension; m++) {
			if(m == 0) {
				result = result.concat(" ");
			}
			
			result = result.concat(String.valueOf(new Integer((dimension*3)-m)));
			
			if(m == dimension-1) {
				result = result.concat(" \n");
			}
		}
		
		return result;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public void print() {
		System.out.print(this.toASCII());
	}
	
	//Print ratings (set by solver)
	public void printRating() {
		String result = new String();
		for(int i = 0; i < dimension; i++) {
			for(int j = 0; j < dimension; j++) {			
				result = result.concat(String.valueOf(board[j][i].getRating()));
				if(j == dimension -1) {
					result = result.concat("\n");
				}
			}
			if(i == dimension -1) {
				result = result.concat("\n");
			}
		}
		
		System.out.println(result);
	}
	
	//Create empty board
	public Board(int dim) {
		if(dim < 3) {
			dimension = 3;
		} else {
			dimension = dim;
		}
		board = new Cell[dimension][dimension];
		for(int i = 0; i < dimension; i++) {
			for(int j = 0; j < dimension; j++) {
				board[j][i] = new Cell();
			}
		}
	}
	
	//Put a marble at the specified coordinates
	public void putMarble(int x, int y) {
		if(x >= 0 && x < dimension && y >= 0 && y < dimension) {
			board[x][y].setContent("o");
		}
	}
	
	//Fill the board with a fixed number of marbles at random places
	public void fillWithMarbles(int num) {
		if(num > dimension*dimension || num <= 0) {
			return;
		}
		
		//This can potentially take forever if you try to fill the entire board with marbles
		for(int i = 1; i <= num; i++) {
			int x = (int)(Math.random()*dimension);
			int y = (int)(Math.random()*dimension);
			if(board[x][y].getContent() != "o") {
				putMarble(x,y);
			} else {
				i--;
			}
		}
	}
	
	//Fill board with a random number of marbles at random places
	public void fillWithRandomMarbles() {
		fillWithMarbles((int)(Math.random()*(dimension*dimension)));
	}

}
