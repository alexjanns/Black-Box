import java.util.ArrayList;

public class Analyser {
	
	private ArrayList<Coordinates> forbiddenPos;
	private ArrayList<Coordinates> fixedPos;
	private ArrayList<XORMarble> xorPos;
	
	public ArrayList<Coordinates> getForbidden() {
		return forbiddenPos;
	}
	
	public ArrayList<Coordinates> getFixed(){
		return fixedPos;
	}
	
	public ArrayList<XORMarble> getXOR(){
		return xorPos;
	}
	
	private void analyse(ArrayList<Coordinates> input) {
		int boardDim = input.get(0).getFirst();
		int marbleCount = input.get(0).getSecond();
		input.remove(0);
		
		boolean keepGoing = true;
		int iteration = 0;
		//Look at the left edge
		while(keepGoing) {
			//Does the ray just pass through?
			if(input.contains(new Coordinates(1+iteration, boardDim*3 - iteration))) {
				for(int i = 0; i < boardDim; i++) {
					Coordinates toAdd = new Coordinates(0+iteration, i); //Array Coordinates
					if(!forbiddenPos.contains(toAdd)) {
						forbiddenPos.add(toAdd);
					}	
				}
				iteration++;
			} else if(marbleCount > 0){
				//Do we have a direct hit?
				if(!input.contains(new Coordinates(iteration+1))) {
					//Check for edge deflection, since no hit
					for(int j = 0; j < boardDim; j++) {
						if(input.contains(new Coordinates(iteration+1, boardDim*4-j))){
							Coordinates toAdd = new Coordinates(iteration+1, j+1); //Array Coordinates
							if(!fixedPos.contains(toAdd)) {
								fixedPos.add(toAdd);
								marbleCount--;
							}
							break;
						}
					}
					for(int j = boardDim-1; j >= 0; j--) {
						if(input.contains(new Coordinates(boardDim*3-iteration, boardDim*4-j))){
							Coordinates toAdd = new Coordinates(iteration+1, j+1); //Array Coordinates
							if(!fixedPos.contains(toAdd)) {
								fixedPos.add(toAdd);
								marbleCount--;
							}
							break;
						}
					}
				} else {
					//We have a hit, so we may infer some XOR marbles
					if(iteration == 0) {
						//Only on the very edge do rays miss the board entirely
						for(int k = boardDim*3+1; k <= boardDim*4; k++) {
							boolean found = false;
							for(int m = 0; m < input.size(); m++) {
								if(input.get(m).getFirst() == k || input.get(m).getSecond() == k) {
									found = true;
									break;
								}
							}
							if(!found) {
								//If we're on a corner, we know where the marble is that causes this
								if(boardDim*4-k == 0) {
									Coordinates toAdd = new Coordinates(0, boardDim*4-k+1);
									if(!fixedPos.contains(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else if(boardDim*4-k == boardDim-1) {
									Coordinates toAdd = new Coordinates(0, boardDim*4-k-1);
									if(!fixedPos.contains(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else {
									XORMarble toAdd = new XORMarble(new Coordinates(0, boardDim*4-k+1), new Coordinates(0, boardDim*4-k-1));
									if(!xorPos.contains(toAdd)) {
										xorPos.add(toAdd);
										marbleCount--;
									}
								}
								
							}
						}
					} else {
						//Any deflection straight up or down, going in from the left side, is guaranteed to be caused by only one marble
						for(int i = boardDim*3+1; i <= boardDim*4; i++) {
							if(input.contains(new Coordinates(iteration+1, i))) {
								Coordinates toAdd = new Coordinates(iteration+1, boardDim*4+i);
								if(!fixedPos.contains(toAdd)) {
									fixedPos.add(toAdd);
									marbleCount--;
								}
							} else if(input.contains(new Coordinates(boardDim*3-iteration, i))) {
								Coordinates toAdd = new Coordinates(iteration+1, boardDim*4-i);
								if(!fixedPos.contains(toAdd)) {
									fixedPos.add(toAdd);
									marbleCount--;
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	public Analyser(ArrayList<Coordinates> input) {
		forbiddenPos = new ArrayList<Coordinates>();
		fixedPos = new ArrayList<Coordinates>();
		xorPos = new ArrayList<XORMarble>();
		ArrayList<Coordinates> inp = (ArrayList<Coordinates>) input.clone();
		analyse(inp);
	}

}
