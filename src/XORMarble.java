
public class XORMarble {
	
	private Coordinates first;
	private Coordinates second;
	
	public Coordinates getFirstPosition() {
		return first;
	}
	
	public Coordinates getSecondPosition() {
		return second;
	}
	
	//Maybe useless, we'll see
	public Coordinates switchPosition(Coordinates c) {
		if(c.equals(first)) {
			return second;
		} else {
			return first;
		}
	}
	
	public XORMarble(Coordinates f, Coordinates s) {
		first = f;
		second = s;
	}

}
