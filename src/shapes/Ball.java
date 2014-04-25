package shapes;
import java.awt.Color;
import java.awt.Graphics;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Ball extends Object3D implements CollisionListener{
	private static World world;
	private SimpleVector ballVector;
	private float speedX, speedY, speedZ;
	private double velocityX, velocityY, velocityZ;
	private double LEFT, RIGHT, CLOSE, FAR, UP, BOTTOM;
	private float radius;
	private final double g = 9.8;
	private double mass;
	private Color color;
	float ballScale;
	private double heading;
	private Object3D myBall;
	private double delta;
	private int collisionCounter;
	private final double offSet = 4;
	private int countx, county, countz = 0;
	private float size;

	
	public Ball(SimpleVector vector, World myWorld, int resolution, float scale, int tickRate, double vx, double vy, double vz){
		super(resolution);
		LEFT=95;
		RIGHT=-95;
		CLOSE=95;
		FAR=-95;
		BOTTOM=0;
		ballScale = scale;
		ballVector = new SimpleVector(vector);
		world = myWorld;
		collisionCounter = 0;
		size = scale;
		
		speedX=0.1f;
		speedY=0.1f;
		speedZ=0.1f;
		delta = (tickRate*35)/tickRate;
//		velocityX = speedX*heading;
//		velocityY = speedY*heading;
//		velocityZ = speedZ*heading;
		
		velocityX = vx;
		velocityY = vy;
		velocityZ = vz;
		myBall = Primitives.getSphere(resolution, scale);
		myBall.setOrigin(ballVector);
		myBall.setCollisionMode(Object3D.COLLISION_CHECK_SELF);
		myBall.setCollisionOptimization(true);
		myBall.addCollisionListener(this);
		
	}
	
	public void collisionMove(){
//		System.out.println(ballVector);
		//check collision detection with all other balls in arena
		//check collision detection with walls
		//check collision detection with person
		//collisionCounter = 2;
//		System.out.println(collisionCounter);
//		if(LEFT-offSet < ballVector.x && ballVector.x < LEFT+offSet || RIGHT-offSet < ballVector.x && ballVector.x < RIGHT+offSet){
//				velocityX = -velocityX;
//		} else if(BOTTOM-offSet < ballVector.y && ballVector.y < BOTTOM+offSet ){
//				velocityY = -velocityY;
//		} else if(CLOSE-offSet < ballVector.z && ballVector.z < CLOSE+offSet || FAR-offSet < ballVector.z && ballVector.z < FAR+offSet){
//				velocityZ = -velocityZ;
//		}
		
		
//		System.out.println("INSIDE COLLISION MOVE");
		if(LEFT-offSet < ballVector.x && ballVector.x < LEFT+offSet || RIGHT-offSet < ballVector.x && ballVector.x < RIGHT+offSet){
//			System.out.println("INSIDE X");
			if(countx==0){
				velocityX = -velocityX;
				System.out.println("flipped X velocity");
				countx++;
				county=0;
				countz=0;
			}
			else{
				ballVector.x += -velocityX*2;
				velocityX = -velocityX;
				System.out.println("tried to reset x vector");
			}
		} else if(BOTTOM-offSet < ballVector.y && ballVector.y < BOTTOM+offSet ){
//			System.out.println("INSIDE Y");
			if(county==0){
				velocityY = -velocityY;
				System.out.println("flipped y velocity");
				county++;
				countx = 0;
				countz = 0;
			}
			else{
				ballVector.y += -velocityY*2;
				velocityY = -velocityY;
				System.out.println("tried to reset y vector");

			}
		} else if(CLOSE-offSet < ballVector.z && ballVector.z < (CLOSE+offSet) || FAR-offSet < ballVector.z && ballVector.z < FAR+offSet){
//			System.out.println("INSIDE Z");
			if(countz==0){
				velocityZ = -velocityZ;
				System.out.println("flipped z velocity");
				countz++;
				county=0;
				countx=0;
			}
			else{
				ballVector.z += -velocityZ*2;
				velocityZ = -velocityZ;
				System.out.println("tried to reset z vector");

			}
		}
		
	}
	
	public Object3D getSphere(){
		return myBall;
	}
	
	public SimpleVector getVector(){
		return new SimpleVector(ballVector);
	}
	
	public SimpleVector gravMove(){
		collisionCounter--;
//		System.out.println("~~~~~~~~~~STARTING MOVE~~~~~~~~~~");
//		System.out.println(collisionCounter);

		SimpleVector prevVector = new SimpleVector(ballVector.x, ballVector.y, ballVector.z);
//		System.out.println(prevVector);
//		System.out.println("Vector X: "+ballVector.x+" Vector Y: " + ballVector.y+" Vector Z: "+ballVector.z);
//		System.out.println("Delta is: " + delta);
//		System.out.println("Starting y velocity: "+velocityY);
		velocityY += g/(2*delta);
//		System.out.println("velocity we are adding to Z is: " + (g/(2*delta))*2);
		ballVector.x -= velocityX/delta;
		ballVector.y += velocityY/delta;
		ballVector.z -= velocityZ/delta;
		velocityY += g/(2*delta);
		
		SimpleVector finalMoveVector = ballVector.calcSub(prevVector);
		
//		System.out.println("Vector X: "+ballVector.x+" Vector Y: " + ballVector.y+" Vector Z: "+ballVector.z);
//		System.out.println("Ending y velocity: "+velocityY);
		
		SimpleVector sphericalDestination = myBall.checkForCollisionSpherical(finalMoveVector, 5);
		if (!sphericalDestination.equals(finalMoveVector)) {
//			System.out.println("I really, really did hit something");
		}
		//SimpleVector toTranslate = ballVector.calcSub(prevVector);
		
		myBall.translate(finalMoveVector);
		return finalMoveVector;
//		System.out.println("~~~~~~~~~~FINISHED MOVE~~~~~~~~~~");
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
	}
	
	public float flipSign(float num){
		if(num > 0){
			return -num;
		}
		else if(num < 0){
			return num;
		}
		else{
			return 0;
		}
	}
	
	public void draw(Graphics g){
		//going to have to change this to 3D?
		g.fillOval((int) ballVector.x,(int) ballVector.y,(int) radius/2,(int) radius/2);
		
	}
	
	public double getSize(){
		return size;
	}

	@Override
	public void collision(CollisionEvent e) {
		// TODO Auto-generated method stub
		Object3D obj = e.getSource();
		SimpleVector impactLocation = e.getFirstContact();
		/*float impactX = impactLocation.x;
		float impactY = impactLocation.y;
		float impactZ = impactLocation.z;
		*/
//		System.out.println("COLLISION)");
		collisionMove();
		//gravMove();
	}

	@Override
	public boolean requiresPolygonIDs() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}