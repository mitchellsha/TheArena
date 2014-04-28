package shapes;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Ball extends Object3D implements CollisionListener{
	private SimpleVector ballVector;
	private double velocityX, velocityY, velocityZ;
	private final double g = 9.8;
	float ballScale;
	private Object3D myBall;
	private double delta;
	private int countCollisionX, countCollisionY, countCollisionZ = 0;
	private float size;
	private HashMap<Integer, String> wallIDs;
	private BallList ballList;
	
	public Ball(SimpleVector vector, HashMap<Integer, String> wallIds, BallList ballLst, int resolution, float scale, int tickRate, double vx, double vy, double vz){
		super(resolution);
		ballScale = scale;
		ballVector = new SimpleVector(vector);
		size = scale;
		wallIDs = wallIds;
		delta = (tickRate*10)/tickRate;
		velocityX = vx;
		velocityY = vy;
		velocityZ = vz;
		myBall = Primitives.getSphere(resolution, scale);
		ballList = ballLst;
		myBall.setOrigin(ballVector);
		myBall.setCollisionMode(Object3D.COLLISION_CHECK_SELF|Object3D.COLLISION_CHECK_OTHERS);
		myBall.setCollisionOptimization(true);
		myBall.addCollisionListener(this);
	}
	
	public void collisionMove(String wall){
		boolean vectorChange = false;
		SimpleVector prevVector = new SimpleVector(ballVector.x, ballVector.y, ballVector.z);

		if(wall.equals("Right")){
			countCollisionX++;
			if(countCollisionX==1){
				velocityX = -velocityX;
				countCollisionY=0;
				countCollisionZ=0;
				vectorChange = false;
			}
			else{
				velocityX = -velocityX;
				ballVector.x -= size;
				vectorChange = true;
			}
		} 
		else if(wall.equals("Left")){
			countCollisionX++;
			if(countCollisionX==1){
				velocityX = -velocityX;
				countCollisionY=0;
				countCollisionZ=0;
				vectorChange = false;
			}
			else{
				velocityX = -velocityX;
				ballVector.x += size;
				vectorChange = true;
			}
		} 
		else if(wall.equals("Bottom")){
			countCollisionY++;
			if(countCollisionY == 1){
				velocityY = -velocityY;
				countCollisionX=0;
				countCollisionZ=0;
				vectorChange = false;
			}
			else{
				velocityY = -velocityY;
				ballVector.y -= size/4;
				vectorChange=true;
			}
		}
		else if(wall.equals("Close")){
			countCollisionZ++;
			if(countCollisionZ==1){
				velocityZ = -velocityZ;
				countCollisionX=0;
				countCollisionY=0;
				vectorChange = false;
			}
			else{
				velocityZ = -velocityZ;
				ballVector.z -= size;
				vectorChange = true;
			}
		}
		else if(wall.equals("Far")){
			countCollisionZ++;
			if(countCollisionZ==1){
				velocityZ = -velocityZ;
				countCollisionX=0;
				countCollisionY=0;
				vectorChange = false;
			}
			else{
				velocityZ = -velocityZ;
				ballVector.z += size;
				vectorChange = true;
			}
		}
		if(vectorChange){
			SimpleVector finalMoveVector = ballVector.calcSub(prevVector);
			myBall.translate(finalMoveVector);
		}
	}
	
	public Object3D getSphere(){
		return myBall;
	}
	
	public SimpleVector getVector(){
		return new SimpleVector(ballVector);
	}
	
	public void ballCollisionMove(){
		
	}
	
	public void gravMove(){
		SimpleVector prevVector = new SimpleVector(ballVector.x, ballVector.y, ballVector.z);
		changeVectorsandVelocities();
		SimpleVector finalMoveVector = ballVector.calcSub(prevVector);
		SimpleVector sphericalDestination = myBall.checkForCollisionSpherical(finalMoveVector, 5);
		//if (!sphericalDestination.equals(finalMoveVector)) {
			//sphere on sphere collision
			//System.out.println("The spheres collided!");
		//}
		myBall.translate(finalMoveVector);
	}
	
	private void changeVectorsandVelocities(){
		velocityY += g/(2*delta);
		ballVector.x -= velocityX/delta;
		ballVector.y += velocityY/delta;
		ballVector.z -= velocityZ/delta;
		velocityY += g/(2*delta);
	}
	
	public BallList getBallList(){
		return ballList;
	}
	
	public double getSize(){
		return size;
	}
	
	public void updateBallList(BallList bList){
		//will creating a new ballList give me the most up to date one since its static?
		ballList = bList;
	}

	@Override
	public void collision(CollisionEvent e) {
		Object3D obj = e.getSource();
//		System.out.println("source: " + obj.getID());
		for(Object3D target: e.getTargets()){
			if(ballList.contains(obj.getID())||ballList.contains(target.getID())){
				System.out.println("yes one of these objects is a ball");
			}
			//System.out.println("Target id compared to the source: " + target.getID());
			if(wallIDs.containsKey(target.getID())){
				collisionMove(wallIDs.get(target.getID()));
			}
			else{
				//System.out.println(ballList.toString());
//				it seems like the ball ids being captured here are one more than was
//				put into the ballList.
				//System.out.println("ball ids involved are " + target.getID() + " and source" + obj.getID());
				if(ballList.contains(obj.getID()-1)){
//					System.out.println("ball: " + obj.getID() + " just hit the ball: " +target.getID());
					ballCollisionMove();
				}
			}
		}
	}

	@Override
	public boolean requiresPolygonIDs() {
		// TODO Auto-generated method stub
		return false;
	}
}