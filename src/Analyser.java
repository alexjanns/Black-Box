import java.util.ArrayList;

public class Analyser {
	
	private ArrayList<Coordinates> forbiddenPos;
	private ArrayList<Coordinates> fixedPos;
	private ArrayList<XORMarble> xorPos;
	private boolean impossible;
	
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
		
		//Special case: Does our input only consist of hits and nothing else?
		boolean specialCase = true;
		if(input.size() == 4*boardDim && marbleCount >= 4*boardDim-4) {
			for(int i = 1; i <= input.size(); i++) {
				if(!input.contains(new Coordinates(i))) {
					specialCase = false;
					break;
				}
			}
		} else {
			specialCase = false;
		}
		
		if(specialCase) {
			for(int j = 0; j < boardDim; j++) {
				for(int i = 0; i < boardDim; i++) {
					if(j == 0 || j == boardDim-1) {
						fixedPos.add(new Coordinates(i, j));
					} else {
						fixedPos.add(new Coordinates(0, j));
						fixedPos.add(new Coordinates(boardDim-1, j));
						break;
					}
				}
			}
			checkValidity();
			return;
		}
		
		//Normal preprocessing
		boolean keepGoing = true;
		int iteration = 0;
		//Look at the left edge
		while(keepGoing) {
			//Does the ray just pass through?
			if(input.contains(new Coordinates(1+iteration, boardDim*3 - iteration))) {
				for(int i = 0; i < boardDim; i++) {
					Coordinates toAdd = new Coordinates(iteration, i); //Array Coordinates
					if(!forbiddenPos.contains(toAdd)) {
						forbiddenPos.add(toAdd);
					}	
				}
				iteration++;
			} else if(marbleCount > 0){
				//Do we have a direct hit?
				if(!input.contains(new Coordinates(iteration+1))) {
					//No hit, so check for deflections
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
					
					if(iteration > 0) {
						//We came in knowing there was no deflection further out, so there can't be any marbles here
						for(int i = 0; i < boardDim; i++) {
							Coordinates toAdd = new Coordinates(iteration, i); //Array Coordinates
							if(!forbiddenPos.contains(toAdd)) {
								forbiddenPos.add(toAdd);
							}	
						}
					}
					
					//No further reduction possible
					keepGoing = false;
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
									if(!fixedPos.contains(toAdd) && !checkXORforFixedPos(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else if(boardDim*4-k == boardDim-1) {
									Coordinates toAdd = new Coordinates(0, boardDim*4-k-1);
									if(!fixedPos.contains(toAdd) && !checkXORforFixedPos(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else {
									XORMarble toAdd = new XORMarble(new Coordinates(0, boardDim*4-k+1), new Coordinates(0, boardDim*4-k-1));
									if(!xorPos.contains(toAdd) && !(fixedPos.contains(toAdd.getFirstPosition()) || fixedPos.contains(toAdd.getSecondPosition()))) {
										xorPos.add(toAdd);
										marbleCount--;
									}
								}

							}
						}
					}

					//No further reduction possible
					keepGoing = false;
				}
			}
		}
		
		//We're not done yet
		keepGoing = true;
		iteration = 0;
		//Pretty much copypasta for the other three edges
		//Look at the upper edge
		while(keepGoing) {
			//Does the ray just pass through?
			if(input.contains(new Coordinates(boardDim+1 + iteration, boardDim*4-iteration))) {
				for(int i = 0; i < boardDim; i++) {
					Coordinates toAdd = new Coordinates(i, iteration); //Array Coordinates
					if(!forbiddenPos.contains(toAdd)) {
						forbiddenPos.add(toAdd);
					}	
				}
				iteration++;
			} else if(marbleCount > 0){
				//Do we have a direct hit?
				if(!input.contains(new Coordinates(boardDim+1+iteration))) {
					//No hit, so check for deflections
					for(int j = 0; j < boardDim; j++) {
						if(input.contains(new Coordinates(j+1, boardDim+1+iteration))){
							Coordinates toAdd = new Coordinates(j-1, iteration+1); //Array Coordinates
							if(!fixedPos.contains(toAdd)) {
								fixedPos.add(toAdd);
								marbleCount--;
							}
							break;
						}
					}
					for(int j = boardDim-1; j >= 0; j--) {
						if(input.contains(new Coordinates(j+1, boardDim*4-iteration))){
							Coordinates toAdd = new Coordinates(j+1, iteration+1); //Array Coordinates
							if(!fixedPos.contains(toAdd)) {
								fixedPos.add(toAdd);
								marbleCount--;
							}
							break;
						}
					}
					
					if(iteration > 0) {
						//We came in knowing there was no deflection further out, so there can't be any marbles here
						for(int i = 0; i < boardDim; i++) {
							Coordinates toAdd = new Coordinates(i, iteration); //Array Coordinates
							if(!forbiddenPos.contains(toAdd)) {
								forbiddenPos.add(toAdd);
							}	
						}
					}
					
					//No further reduction possible
					keepGoing = false;
				} else {
					//We have a hit, so we may infer some XOR marbles
					if(iteration == 0) {
						//Only on the very edge do rays miss the board entirely
						for(int k = 1; k <= boardDim; k++) {
							boolean found = false;
							for(int m = 0; m < input.size(); m++) {
								if(input.get(m).getFirst() == k || input.get(m).getSecond() == k) {
									found = true;
									break;
								}
							}
							if(!found) {
								//If we're on a corner, we know where the marble is that causes this
								if(k == 1) {
									Coordinates toAdd = new Coordinates(1, 0);
									if(!fixedPos.contains(toAdd) && !checkXORforFixedPos(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else if(k == boardDim) {
									Coordinates toAdd = new Coordinates(k-2, 0);
									if(!fixedPos.contains(toAdd) && !checkXORforFixedPos(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else {
									XORMarble toAdd = new XORMarble(new Coordinates(k-2, 0), new Coordinates(k, 0));
									if(!xorPos.contains(toAdd) && !(fixedPos.contains(toAdd.getFirstPosition()) || fixedPos.contains(toAdd.getSecondPosition()))) {
										xorPos.add(toAdd);
										marbleCount--;
									}
								}

							}
						}
					}
				
					//No further reduction possible
					keepGoing = false;
				}
			}
		}
		
		keepGoing = true;
		iteration = 0;
		//Look at the right edge
		while(keepGoing) {
			//Does the ray just pass through?
			if(input.contains(new Coordinates(boardDim-iteration, boardDim*2+1+iteration))) {
				for(int i = 0; i < boardDim; i++) {
					Coordinates toAdd = new Coordinates(boardDim-1-iteration, i); //Array Coordinates
					if(!forbiddenPos.contains(toAdd)) {
						forbiddenPos.add(toAdd);
					}	
				}
				iteration++;
			} else if(marbleCount > 0){
				//Do we have a direct hit?
				if(!input.contains(new Coordinates(boardDim-iteration))) {
					//No hit, so check for deflections
					for(int j = 0; j < boardDim; j++) {
						if(input.contains(new Coordinates(boardDim-iteration, boardDim+1+j))){
							Coordinates toAdd = new Coordinates(boardDim-iteration-1, j+1); //Array Coordinates
							if(!fixedPos.contains(toAdd)) {
								fixedPos.add(toAdd);
								marbleCount--;
							}
							break;
						}
					}
					for(int j = boardDim-1; j >= 0; j--) {
						if(input.contains(new Coordinates(boardDim*2+1+iteration, boardDim+1+j))){
							Coordinates toAdd = new Coordinates(boardDim-iteration-1, j-1); //Array Coordinates
							if(!fixedPos.contains(toAdd)) {
								fixedPos.add(toAdd);
								marbleCount--;
							}
							break;
						}
					}
					
					if(iteration > 0) {
						//We came in knowing there was no deflection further out, so there can't be any marbles here
						for(int i = 0; i < boardDim; i++) {
							Coordinates toAdd = new Coordinates(boardDim-iteration, i); //Array Coordinates
							if(!forbiddenPos.contains(toAdd)) {
								forbiddenPos.add(toAdd);
							}	
						}
					}
					
					//No further reduction possible
					keepGoing = false;
				} else {
					//We have a hit, so we may infer some XOR marbles
					if(iteration == 0) {
						//Only on the very edge do rays miss the board entirely
						for(int k = boardDim+1; k <= boardDim*2; k++) {
							boolean found = false;
							for(int m = 0; m < input.size(); m++) {
								if(input.get(m).getFirst() == k || input.get(m).getSecond() == k) {
									found = true;
									break;
								}
							}
							if(!found) {
								//If we're on a corner, we know where the marble is that causes this
								if(boardDim*2-k == 0) {
									Coordinates toAdd = new Coordinates(boardDim-1, boardDim-2);
									if(!fixedPos.contains(toAdd) && !checkXORforFixedPos(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else if(boardDim*2-k == boardDim-1) {
									Coordinates toAdd = new Coordinates(boardDim-1, 1);
									if(!fixedPos.contains(toAdd) && !checkXORforFixedPos(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else {
									XORMarble toAdd = new XORMarble(new Coordinates(boardDim-1, boardDim*2-k+1), new Coordinates(boardDim-1, boardDim*2-k-1));
									if(!xorPos.contains(toAdd) && !(fixedPos.contains(toAdd.getFirstPosition()) || fixedPos.contains(toAdd.getSecondPosition()))) {
										xorPos.add(toAdd);
										marbleCount--;
									}
								}

							}
						}
					}

					//No further reduction possible
					keepGoing = false;
				}
			}
		}
		
		keepGoing = true;
		iteration = 0;
		//Look at the lower edge
		while(keepGoing) {
			//Does the ray just pass through?
			if(input.contains(new Coordinates(boardDim*2-iteration, boardDim*3+1+iteration))) {
				for(int i = 0; i < boardDim; i++) {
					Coordinates toAdd = new Coordinates(i, boardDim-1-iteration); //Array Coordinates
					if(!forbiddenPos.contains(toAdd)) {
						forbiddenPos.add(toAdd);
					}	
				}
				iteration++;
			} else if(marbleCount > 0){
				//Do we have a direct hit?
				if(!input.contains(new Coordinates(boardDim*2-iteration))) {
					//No hit, so check for deflections
					for(int j = 0; j < boardDim; j++) {
						if(input.contains(new Coordinates(boardDim*2-iteration, boardDim*2+1+j))){
							Coordinates toAdd = new Coordinates(boardDim-2-j, boardDim-iteration-2); //Array Coordinates
							if(!fixedPos.contains(toAdd)) {
								fixedPos.add(toAdd);
								marbleCount--;
							}
							break;
						}
					}
					for(int j = boardDim-1; j >= 0; j--) {
						if(input.contains(new Coordinates(boardDim*2+1+j, boardDim*3+1+iteration))){
							Coordinates toAdd = new Coordinates(j+1, boardDim-iteration-2); //Array Coordinates
							if(!fixedPos.contains(toAdd)) {
								fixedPos.add(toAdd);
								marbleCount--;
							}
							break;
						}
					}
					
					if(iteration > 0) {
						//We came in knowing there was no deflection further out, so there can't be any marbles here
						for(int i = 0; i < boardDim; i++) {
							Coordinates toAdd = new Coordinates(i, boardDim-1-iteration); //Array Coordinates
							if(!forbiddenPos.contains(toAdd)) {
								forbiddenPos.add(toAdd);
							}	
						}
					}
					
					//No further reduction possible
					keepGoing = false;
				} else {
					//We have a hit, so we may infer some XOR marbles
					if(iteration == 0) {
						//Only on the very edge do rays miss the board entirely
						for(int k = boardDim*2+1; k <= boardDim*3; k++) {
							boolean found = false;
							for(int m = 0; m < input.size(); m++) {
								if(input.get(m).getFirst() == k || input.get(m).getSecond() == k) {
									found = true;
									break;
								}
							}
							if(!found) {
								//If we're on a corner, we know where the marble is that causes this
								if(boardDim*3-k == 0) {
									Coordinates toAdd = new Coordinates(1, boardDim-1);
									if(!fixedPos.contains(toAdd) && !checkXORforFixedPos(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else if(boardDim*3-k == boardDim-1) {
									Coordinates toAdd = new Coordinates(boardDim-2, boardDim-1);
									if(!fixedPos.contains(toAdd) && !checkXORforFixedPos(toAdd)) {
										fixedPos.add(toAdd);
										marbleCount--;
									}
								} else {
									XORMarble toAdd = new XORMarble(new Coordinates(boardDim*3-k+1, boardDim-1), new Coordinates(boardDim*3-k-1, boardDim-1));
									if(!xorPos.contains(toAdd) && !(fixedPos.contains(toAdd.getFirstPosition()) || fixedPos.contains(toAdd.getSecondPosition()))) {
										xorPos.add(toAdd);
										marbleCount--;
									}
								}

							}
						}
					}

					//No further reduction possible
					keepGoing = false;
				}
			}
		}
		
		//One last thing...
		checkValidity();
	}
	
	//Sets the flag that the board is deemed impossible to solve if any of the positions gleaned are invalid
	private void checkValidity() {
		for(int i = 0; i < forbiddenPos.size(); i++) {
			if(forbiddenPos.get(i).getFirst() < 0 || forbiddenPos.get(i).getSecond() < 0) {
				impossible = true;
			}
		}
		
		for(int i = 0; i < fixedPos.size(); i++) {
			if(fixedPos.get(i).getFirst() < 0 || fixedPos.get(i).getSecond() < 0) {
				impossible = true;
			}
		}
		
		for(int i = 0; i < xorPos.size(); i++) {
			if(xorPos.get(i).getFirstPosition().getFirst() < 0 || xorPos.get(i).getFirstPosition().getSecond() < 0 || xorPos.get(i).getSecondPosition().getFirst() < 0 || xorPos.get(i).getSecondPosition().getSecond() < 0) {
				impossible = true;
			}
		}
	}
	
	private boolean checkXORforFixedPos(Coordinates pos) {
		for(int i = 0; i < xorPos.size(); i++) {
			if(xorPos.get(i).getFirstPosition().equals(pos) || xorPos.get(i).getSecondPosition().equals(pos)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isImpossible() {
		return impossible;
	}
	
	public Analyser(ArrayList<Coordinates> input) {
		impossible = false;
		forbiddenPos = new ArrayList<Coordinates>();
		fixedPos = new ArrayList<Coordinates>();
		xorPos = new ArrayList<XORMarble>();
		ArrayList<Coordinates> inp = new ArrayList<Coordinates>(input);
		analyse(inp);
	}

}
