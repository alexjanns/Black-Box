import java.util.ArrayList;
import java.util.Scanner;

public class Solver {
	
	private ArrayList<Coordinates> input;
	private Analyser analyser;
	private Board solution;
	MarbleArray marbles;
	private int boardDimension;
	private int marbleCount;
	
	public void solveBruteForce() {
		marbles = new MarbleArray(marbleCount, boardDimension);
		input.remove(0);
		Checker checker = new Checker(input);
		
		solution = marbleArrayToBoard(marbles);
		System.out.println("\033[s");
		solution.print();
		System.out.println("\033[u");
		boolean checkresult = checker.check(solution);
		
		while(!marbles.finalPosition() && !checkresult) {
			marbles.advance();
			solution = marbleArrayToBoard(marbles);
			solution.print();
			System.out.println("\033[u");
			checkresult = checker.check(solution);
		}
		
		if(checkresult) {
			System.out.println("Solution has been found. It is:");
			for(int i = 0; i < getMarbles().length(); i++) {
				System.out.println(getMarbles().access(i).getGameCoordinates().toString());
			}
		} else {
			System.out.println("Sorry, but your solution is in another castle.");
		}
	}
	
	public void solvePreprocessing() {
		Scanner scanner = new Scanner(System.in);
		analyser = new Analyser(input);
		input.remove(0);
		Checker checker = new Checker(input);
		marbles = new MarbleArray(marbleCount, boardDimension, analyser.getForbidden(), analyser.getFixed(), analyser.getXOR());
		
		System.out.println("Preprocessing complete. Here's the result:");
		analyser.printResult();
		System.out.println("Press Enter to continue.");
		scanner.nextLine();
		
		solution = marbleArrayToBoard(marbles);
		solution.print();
		boolean checkresult = checker.check(solution);
		
		while(!marbles.finalPosition() && !checkresult) {
			marbles.advance();
			solution = marbleArrayToBoard(marbles);
			solution.print();
			checkresult = checker.check(solution);
		}
		
		if(checkresult) {
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
		
		if(analyser != null) {
			for(int i = 0; i < analyser.getForbidden().size(); i++) {
				result.putContent(analyser.getForbidden().get(i).getFirst(), analyser.getForbidden().get(i).getSecond(), "x");
			}
			
			for(int i = 0; i < analyser.getXOR().size(); i++) {
				result.putContent(analyser.getXOR().get(i).getFirstPosition().getFirst(), analyser.getXOR().get(i).getFirstPosition().getSecond(), "?");
				result.putContent(analyser.getXOR().get(i).getSecondPosition().getFirst(), analyser.getXOR().get(i).getSecondPosition().getSecond(), "?");
			}
		}
		
		for(int i = 0; i < arr.length(); i++) {
			Marble temp = arr.access(i);
			result.putMarble(temp.getArrayX(), temp.getArrayY());
		}
		
		return result;
	}
	
	public Solver(ArrayList<Coordinates> inp) {
		input = inp;
		Coordinates dimension_marbles = input.get(0);
		boardDimension = dimension_marbles.getFirst();
		marbleCount = dimension_marbles.getSecond();
	}

}
