import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class CoordinateListParser {
	
	private BufferedReader reader;
	private ArrayList<Coordinates> readContent;
	
	private void parse() {
		readContent = new ArrayList<Coordinates>();
		Scanner scanner = new Scanner(reader);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Scanner inner = new Scanner(line);
			if(inner.hasNextInt()) {
				Coordinates temp = new Coordinates();
				temp.setFirst(inner.nextInt());
				if(inner.hasNextInt()) {
					temp.setSecond(inner.nextInt());
				}
				readContent.add(temp);
				inner.close();
			}
		}
		
		scanner.close();
	}
	
	public ArrayList<Coordinates> getInputList() {
		return readContent;
	}
	
	public CoordinateListParser(String file) {
		try {
			reader = new BufferedReader(new FileReader(file));
			this.parse();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
