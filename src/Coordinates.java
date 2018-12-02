
//Class for pairs used for coordinates in the BlackBox solver.
//There can ever only be positive coordinates, so -1 (or any negative int) is used to denote "empty".
public class Coordinates {
	private int first;
	private int second;
	
	public int getFirst() {
		return first;
	}
	
	public int getSecond() {
		return second;
	}
	
	public void setFirst(int f) {
		first = f;
	}
	
	public void setSecond(int s) {
		second = s;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + first;
		result = prime * result + second;
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
		Coordinates other = (Coordinates) obj;
		if (first != other.first)
			return false;
		if (second != other.second)
			return false;
		return true;
	}

	public void normalize() {
		if(first > second && second >= 0) {
			int temp = second;
			second = first;
			first = temp;
		}
	}
	
	public boolean empty() {
		return (first == -1 && second == -1);
	}
	
	public String toString() {
		String result = new String();
		if(first >= 0) {
			result = result.concat(Integer.toString(first));
			if(second >= 0) {
				result = result.concat(", "+Integer.toString(second));
			}
		}
		return result;
	}
	
	public Coordinates(int f, int s){
		first = f;
		second = s;
	}
	
	public Coordinates(int f){
		first = f;
		second = -1;
	}
	
	public Coordinates() {
		first = -1;
		second = -1;
	}

}
