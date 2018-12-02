
public class Cell {
	private String content;
	
	private int rating;
	
	public String getContent() {
		return content;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setContent(String cont) {
		content = cont;
	}
	
	public void setRating( int rat) {
		rating = rat;
	}
	
	public Cell() {
		content = " ";
		rating = 0;
	}
	
	public Cell(String cont) {
		content = cont;
		rating = 0;
	}

}
