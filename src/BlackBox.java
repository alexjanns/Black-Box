import java.util.ArrayList;
import java.util.Scanner;

public class BlackBox {

	public static void main(String[] args) {
		//Recreate sample input from problem description
		ArrayList<Coordinates> sample = new ArrayList<Coordinates>();
		sample.add(new Coordinates(5,4));
		sample.add(new Coordinates(1));
		sample.add(new Coordinates(2,6));
		sample.add(new Coordinates(3));
		sample.add(new Coordinates(4));
		sample.add(new Coordinates(5,7));
		sample.add(new Coordinates(8));
		sample.add(new Coordinates(9));
		sample.add(new Coordinates(10,11));
		sample.add(new Coordinates(12));
		sample.add(new Coordinates(13,14));
		sample.add(new Coordinates(15));
		sample.add(new Coordinates(17));
		sample.add(new Coordinates(18,18));
		sample.add(new Coordinates(19));
		
		Scanner scanner = new Scanner(System.in);
		String in;
		do {
			System.out.println("Would you like to load a file? Y/N");
			in = scanner.nextLine();
		} while(!in.equals("Y") && !in.equals("y") && !in.equals("N") && !in.equals("n")); 
		
		if(in.equals("Y") || in.equals("y")) {
			System.out.println("Specify file to read:");
			in = scanner.nextLine();
			CoordinateListParser parser = new CoordinateListParser(in);
			ArrayList<Coordinates> input = parser.getInputList();
			System.out.println("File read. Content is the following:");
			for(int i = 0; i < input.size(); i++) {
				System.out.println(input.get(i).toString());
			}
			System.out.println("Solving using brute force method. Press Enter to continue.");
			scanner.nextLine();
			Solver solver = new Solver(input);
			solver.solve();
		} else {
			System.out.println("Proceeding with sample input. Using brute force method. Press Enter to continue.");
			scanner.nextLine();
			Solver solver = new Solver(sample);
			solver.solve();
		}
		
		scanner.close();

	}

}
