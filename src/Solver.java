import java.util.ArrayList;

public class Solver {
	
	private ArrayList<Coordinates> input;
	private Board solution;
	MarbleArray marbles;
	private int boardDimension;
	private int marbleCount;
	
	public void solve() {
		Checker checker = new Checker(input);
		
		solution = marbleArrayToBoard(marbles);
		solution.print();
		
		while(!marbles.finalPosition() && !checker.check(solution)) {
			marbles.advance();
			solution = marbleArrayToBoard(marbles);
			solution.print();
		}
		
		if(!marbles.finalPosition()) {
			System.out.println("Solution has been found. It is:");
			for(int i = 0; i < getMarbles().length(); i++) {
				System.out.println(getMarbles().access(i).getGameCoordinates().toString());
			}
		} else {
			System.out.println("Sorry, but your solution is in another castle.");
		}

		
	}
	
	public MarbleArray getMarbles() {
		return marbles;
	}
	
	private Board marbleArrayToBoard(MarbleArray arr) {
		Board result = new Board(boardDimension);
		
		for(int i = 0; i < arr.length(); i++) {
			Marble temp = arr.access(i);
			result.putMarble(temp.getArrayX(), temp.getArrayY());
		}
		
		return result;
	}
	
	public Solver(ArrayList<Coordinates> inp) {
		input = inp;
		Coordinates dimension_marbles = input.get(0);
		input.remove(0);
		boardDimension = dimension_marbles.getFirst();
		marbleCount = dimension_marbles.getSecond();
		marbles = new MarbleArray(marbleCount, boardDimension);
	}

}
