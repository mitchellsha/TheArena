package shapes;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Ball extends Object3D implements CollisionListener{
	private SimpleVector ballPosition, ballVelocity;
	private final double g = 9.8;
	float ballScale;
	private Object3D myBall;
	private double delta;
	private int countCollisionX, countCollisionY, countCollisionZ, ballCounter = 0;
	private int countCollisionLeft, countCollisionFar = 0;
	private double size;
	private HashMap<Integer, String> wallIDs;
	private BallList ballList;
	private boolean vectorChange = false;
	private double vx1, vy1, vz1, vx2, vy2, vz2;
	private boolean alreadyHit;
	private float velocityDecay;
	private boolean locked = false;
	
	
	public Ball(SimpleVector vector, HashMap<Integer, String> wallIds, BallList ballLst, int resolution, float scale, int tickRate, double vx, double vy, double vz){
		super(resolution);
		ballScale = scale;
		ballPosition = new SimpleVector(vector);
		ballVelocity = new SimpleVector(vx, vy, vz);
		size = scale;
		wallIDs = wallIds;
		delta = (tickRate*10)/tickRate;
		myBall = Primitives.getSphere(resolution, scale);
		ballList = ballLst;
		myBall.setOrigin(ballPosition);
		myBall.setCollisionMode(Object3D.COLLISION_CHECK_SELF|Object3D.COLLISION_CHECK_OTHERS);
		myBall.setCollisionOptimization(true);
		myBall.addCollisionListener(this);
		alreadyHit = false;
		velocityDecay = .999f;
	}
	
	public void collisionMove(String wall){
		SimpleVector prevVector = new SimpleVector(ballPosition.x, ballPosition.y, ballPosition.z);
		if(wall.equals("Right")){
			rightWallCollision();
		} else if(wall.equals("Left")){
			leftWallCollision();
		} else if(wall.equals("Bottom")){
			bottomWallCollision();
		} else if(wall.equals("Close")){
			closeWallCollision();
		} else if(wall.equals("Far")){
			farWallCollision();
		}
		if(vectorChange){
			SimpleVector finalMoveVector = ballPosition.calcSub(prevVector);
			myBall.translate(finalMoveVector);
		}
		decayVelocity(this);
	}
	
	private void rightWallCollision(){
		countCollisionLeft++;
		if(countCollisionX==1){
			ballVelocity.x = -ballVelocity.x;
			countCollisionY=0;
			countCollisionZ=0;
			countCollisionLeft=0;
			countCollisionFar=0;
			vectorChange = false;
		}
		else{
			ballVelocity.x = -ballVelocity.x;
			ballPosition.x -= (size/2)*countCollisionX;
			vectorChange = true;
		}
	}
	
	private void leftWallCollision(){
		countCollisionX++;
		if(countCollisionX==1){
			ballVelocity.x = -ballVelocity.x;
			countCollisionY=0;
			countCollisionZ=0;
			countCollisionFar=0;
			vectorChange = false;
		}
		else{
			ballVelocity.x = -ballVelocity.x;
			ballPosition.x += (size/2)*countCollisionX;
			vectorChange = true;
		}
	}
	
	private void bottomWallCollision(){
		countCollisionY++;
		if(countCollisionY == 1){
			ballVelocity.y = -ballVelocity.y;
			countCollisionX=0;
			countCollisionZ=0;
			countCollisionLeft=0;
			countCollisionFar=0;
			vectorChange = false;
		}
		else{
			ballVelocity.y = -ballVelocity.y;
			ballPosition.y -= (size/8); //fix this
			vectorChange=true;
		}
	}
	
	private void closeWallCollision(){
		countCollisionZ++;
		if(countCollisionZ==1){
			ballVelocity.z = -ballVelocity.z;
			countCollisionX=0;
			countCollisionY=0;
			countCollisionLeft=0;
			countCollisionFar=0;
			vectorChange = false;
		}
		else{
			ballVelocity.z = -ballVelocity.z;
			ballPosition.z += (size/2)*countCollisionZ;
			vectorChange = true;
		}
	}
	
	private void farWallCollision(){
		countCollisionFar++;
		if(countCollisionZ==1){
			ballVelocity.z = -ballVelocity.z;
			countCollisionX=0;
			countCollisionY=0;
			countCollisionLeft=0;
			vectorChange = false;
		}
		else{
			ballVelocity.z = -ballVelocity.z;
			ballPosition.z -= (size/2)*countCollisionZ;
			vectorChange = true;
		}
	}
	
	public Object3D getSphere(){
		return myBall;
	}
	
	public SimpleVector getVector(){
		return new SimpleVector(ballPosition);
	}

	public void ballCollisionMove(Ball other){
		lockCheck(this, other);
		if(!locked){
			locked = false;
			float vx1p = other.ballVelocity.x;
			float vz1p = other.ballVelocity.z;
			vx1 = (double) ballVelocity.x;
			vz1 = (double) ballVelocity.z;
			vy1 = (double) ballVelocity.y;
			vx2 = (double) other.ballVelocity.x;
			vz2 = (double) other.ballVelocity.z;
			vy2 = (double) other.ballVelocity.y;
			ballVelocity.x = vx1p;
			ballVelocity.z = vz1p;
			float vy1p = getYPrime(other, vx1p, vz1p);
			float vx2p= getXPrimeOther(other, vx1p, vz1p);
			float vz2p = getZPrimeOther(other, vx1p, vz1p);
			float vy2p = getYPrimeOther(other, vx1p, vz1p);
			ballVelocity.y = vy1p;
			other.ballVelocity.x = vx2p;
			other.ballVelocity.z = vz2p;
			other.ballVelocity.y = vy2p;

			decayVelocity(this);
			decayVelocity(other);
		}
		locked = false;
	}

	private float getYPrime(Ball other, float vx1p, float vz1p){
		double result = 0;
		result = (1/(2*(size+size)))*(2*size*vy1+2*size*vy2 +
			Math.sqrt((Math.pow(((-2)*size*vy1-2*size*vy2), 2))-4*(size+size)*(size*Math.pow(vx1, 2)-size*Math.pow(vx1, 2)
					-2*size*vx1*vx1p+size*Math.pow(vx1p, 2)+size*Math.pow(vx1p, 2) + 
					2*size*vx1*vx2-2*size*vx1p*vx2+
					size*Math.pow(vz1, 2)-size*Math.pow(vz1, 2)-2*size*vz1*vz1p+size*Math.pow(vz1p, 2)+size*Math.pow(vz1p, 2)
					+2*size*vz1*vz2-2*size*vz1p*vz2+size*Math.pow(vy1, 2)-size*Math.pow(vy1, 2)+
					2*size*vy1*vy2)));
	return (float) result;
}

private float getYPrimeOther(Ball other, float vx1p, float vz1p){
	
	double result = 0;
	result = (-1/size)*(-size*vy1+(Math.pow(size, 2)*vy1)/(size+size)-size*vy2 + (size*size*vy2)/(size+size)+
			(1/(2*(size+size)))*size*Math.sqrt(Math.pow((-2)*size*vy1-2*size*vy2,2)-4*(size+size)
					*(size*Math.pow(vx1, 2)-size*Math.pow(vx1, 2)-2*size*vx1*vx1p+size*Math.pow(vx1p, 2)+
							size*Math.pow(vx1p, 2)+2*size*vx1*vx2-2*size*vx1p*vx2+size+Math.pow(vz1, 2)
							-size*Math.pow(vz1, 2)-2*size*vz1*vz1p+size*Math.pow(vz1p, 2)+size*Math.pow(vz1p, 2)+
							2*size*vz1*vz2-2*size*vz1p*vz2+size*Math.pow(vy1, 2)-size*Math.pow(vy1, 2)+
							2*size*vy1*vy2)));
	return (float) result;
}

private float getXPrimeOther(Ball other, float vx1p, float vz1p){
	double result = ((size*vx1-size*vx1p+size*vx2)/size);
	return (float) result;
}

private float getZPrimeOther(Ball other, float vx1p, float vz1p){
	double result = ((size*vz1-size*vz1p+size*vz2)/size);
	return (float) result;
}
	
	public void lockCheck(Ball first, Ball second){
		
		Ball firstBall = first;
		Ball secondBall = second;
		double firstXPos = (double) firstBall.ballPosition.x;
		double firstYPos = (double) firstBall.ballPosition.y;
		double firstZPos = (double) firstBall.ballPosition.z;
		double secondXPos = (double) secondBall.ballPosition.x;
		double secondYPos = (double) secondBall.ballPosition.y;
		double secondZPos = (double) secondBall.ballPosition.z;

		double distance = Math.sqrt(Math.pow(firstXPos-secondXPos,2) + Math.pow(firstYPos-secondYPos,2) + Math.pow(firstZPos-secondZPos,2));
		
		if(distance <= (firstBall.getSize() + secondBall.getSize())){
			ballCounter++;
			if(ballCounter>2){
				ballCounter=0;
				locked = true;
			}
		}

	}

	public void gravMove(){
		SimpleVector prevVector = new SimpleVector(ballPosition.x, ballPosition.y, ballPosition.z);
		changeVectorsandVelocities();
		SimpleVector finalMoveVector = ballPosition.calcSub(prevVector);
		SimpleVector sphericalDestination = myBall.checkForCollisionSpherical(finalMoveVector, 5);
		myBall.translate(finalMoveVector);
	}
	
	private void changeVectorsandVelocities(){
		ballVelocity.y += g/(2*delta);
		ballPosition.x -= ballVelocity.x/delta;
		ballPosition.y += ballVelocity.y/delta;
		ballPosition.z -= ballVelocity.z/delta;
		ballVelocity.y += g/(2*delta);
	}
	
	public BallList getBallList(){
		return ballList;
	}
	
	public double getSize(){
		return size;
	}
	
	public void updateBallList(BallList bList){
		ballList = bList;
	}

	@Override
	public void collision(CollisionEvent e) {
		Object3D obj = e.getSource();
		for(Object3D target: e.getTargets()){
			if(wallIDs.containsKey(target.getID())){
				collisionMove(wallIDs.get(target.getID()));
			}
			else{
				if(!hasBallBeenHit(ballList.get(obj.getID()))||!hasBallBeenHit(ballList.get(target.getID()))){
					if(ballList.contains(target.getID()-1)&&ballList.contains(obj.getID()-1)){
						ballCollisionMove(ballList.get(target.getID()-1));
						setBallToHit(ballList.get(obj.getID()-1));
						setBallToHit(ballList.get(target.getID()-1));
					}
				}
			}
		}
	}

	@Override
	public boolean requiresPolygonIDs() {
		return false;
	}
	
	private void decayVelocity(Ball ball){
		ball.ballVelocity.x = ball.ballVelocity.x*velocityDecay;
		ball.ballVelocity.y = ball.ballVelocity.y*velocityDecay;
		ball.ballVelocity.z = ball.ballVelocity.z*velocityDecay;
	}
	
	public boolean hasBallBeenHit(Ball ball){
		return alreadyHit;
	}
	
	public void setBallToHit(Ball ball){
		ball.alreadyHit = true;
	}
	
	public void setBallNotHit(Ball ball){
		ball.alreadyHit = false;
	}
}