package shapes;

import java.util.HashMap;
import java.util.Set;

public class BallList extends HashMap{
	static private HashMap<Integer, Ball> ballList;
	
	public BallList(){
		ballList = new HashMap<Integer, Ball>();
	}
	
	public HashMap<Integer, Ball> getBallList(){
		return ballList;
	}
	
	public void addToBallList(Integer id, Ball ball){
		ballList.put(id, ball);
	}
	
	public void removeFromBallList(Integer id){
		ballList.remove(id);
	}
	
	public boolean contains(Integer id){
		if(ballList.containsKey(id)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int size(){
		return ballList.size();
	}
	
	public Ball get(Integer index){
		return ballList.get(index);
	}
	
	public String toString(){
		String result = "";
		Set<Integer> set = ballList.keySet();
		for (Integer i: set){
			result += i + ": "+ballList.get(i)+"; ";
		}
		return result;
	}
	
}