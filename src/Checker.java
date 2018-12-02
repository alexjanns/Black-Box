import java.util.ArrayList;
import java.util.List;

public class Checker {
	
	private List<Coordinates> toVerify;
	
	public boolean check(Board sol) {
		return (toVerify.equals(castRays(sol)));
	}
	
	private List<Coordinates> castRays(Board sol){
		List<Coordinates> result = new ArrayList<Coordinates>();
		
		for(int i = 1; i <= sol.getDimension()*4; i++) {
			Ray ray = new Ray(i, sol);
			Coordinates toAdd = ray.go();
			if(!result.contains(toAdd) && !toAdd.empty()) {
				result.add(toAdd);
			}
			if(!toVerify.containsAll(result)) {
				break;
			}
		}
		
		return result;
	}
	
	public Checker(List<Coordinates> solution) {
		toVerify = solution;
	}

}
