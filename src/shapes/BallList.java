package shapes;

import java.util.ArrayList;

public class BallList {
	static private ArrayList<Integer> ballList;
	
	public BallList(){
		ballList = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> getBallList(){
		return ballList;
	}
	
	public void addToBallList(Integer id){
		ballList.add(id);
	}
	
	public void removeFromBallList(Integer id){
		ballList.remove(id);
	}
	
	public boolean contains(Integer id){
		if(ballList.contains(id)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Integer size(){
		return ballList.size();
	}
	
	public Integer get(Integer index){
		return ballList.get(index);
	}
	
	public String toString(){
		String result = "";
		for (Integer i: ballList){
			result += i + ";";
		}
		return result;
	}
	
}
