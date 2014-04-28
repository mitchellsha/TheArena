package shapes;
import java.awt.Color;
import java.awt.Graphics;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class BadBall extends Object3D implements CollisionListener{
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

	
	public BadBall(SimpleVector vector, World myWorld, int resolution, float scale, int tickRate, double vx, double vy, double vz){
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
		if(LEFT-offSet < ballVector.x && ballVector.x < LEFT+offSet || RIGHT-offSet < ballVector.x && ballVector.x < RIGHT+offSet){
			if(countx==0){
				velocityX = -velocityX;
				countx++;
				county=0;
				countz=0;
			}
			else{
				ballVector.x += -velocityX*2;
				velocityX = -velocityX;
			}
		} else if(BOTTOM-offSet < ballVector.y && ballVector.y < BOTTOM+offSet ){
			if(county==0){
				velocityY = -velocityY;
				county++;
				countx = 0;
				countz = 0;
			}
			else{
				ballVector.y += -velocityY*2;
				velocityY = -velocityY;

			}
		} else if(CLOSE-offSet < ballVector.z && ballVector.z < (CLOSE+offSet) || FAR-offSet < ballVector.z && ballVector.z < FAR+offSet){
			if(countz==0){
				velocityZ = -velocityZ;
				countz++;
				county=0;
				countx=0;
			}
			else{
				ballVector.z += -velocityZ*2;
				velocityZ = -velocityZ;
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
		SimpleVector prevVector = new SimpleVector(ballVector.x, ballVector.y, ballVector.z);
		velocityY += g/(2*delta);
		ballVector.x -= velocityX/delta;
		ballVector.y += velocityY/delta;
		ballVector.z -= velocityZ/delta;
		velocityY += g/(2*delta);
		
		SimpleVector finalMoveVector = ballVector.calcSub(prevVector);
		
		SimpleVector sphericalDestination = myBall.checkForCollisionSpherical(finalMoveVector, 5);
		if (!sphericalDestination.equals(finalMoveVector)) {
//			System.out.println("I really, really did hit something");
		}
		
		myBall.translate(finalMoveVector);
		return finalMoveVector;
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
	
	public double getSize(){
		return size;
	}

	@Override
	public void collision(CollisionEvent e) {
		// TODO Auto-generated method stub
		Object3D obj = e.getSource();
		SimpleVector impactLocation = e.getFirstContact();
		collisionMove();
	}

	@Override
	public boolean requiresPolygonIDs() {
		// TODO Auto-generated method stub
		return false;
	}

}