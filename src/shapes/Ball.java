package shapes;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Ball extends Object3D implements CollisionListener{
	private static World world;
	private float x, y, z;
	private SimpleVector ballVector;
	private float speedX, speedY, speedZ;
	private double velocityX, velocityY, velocityZ;
	private float radius;
	private final double g = 9.8;
	private double mass;
	private Color color;
	float ballScale;
	private double heading;
	static Object3D myBall;
	private float delta;

	
	public Ball(SimpleVector vector, World myWorld, int resolution, float scale, float direciton, int tickRate){
		super(myBall);
		ballScale = scale;
		ballVector = vector;
		world = myWorld;
		heading = Math.toRadians(direciton);
		
		speedX=0.1f;
		speedY=0.1f;
		speedZ=0.1f;
		delta = tickRate*10000;
//		velocityX = speedX*heading;
//		velocityY = speedY*heading;
//		velocityZ = speedZ*heading;
		
		velocityX = 0;
		velocityY = 0;
		velocityZ = 0;
		
		myBall = Primitives.getSphere(resolution, scale);
		myBall.setOrigin(ballVector);
		
	}
	
	public void collisionMove(){
		//check collision detection with all other balls in arena
		//check collision detection with walls
		//check collision detection with person
		double ballMinX = 0;
		double ballMinY = 0;
		double ballMinZ = 0;
		
		if(x <= ballMinX ){//if ball hits the x walls
			ballVector.x = flipSign(ballVector.x);
		} else if(y < ballMinY ){//if ball hits the floor... and ceiling?
			ballVector.y = flipSign(ballVector.y);
		} else if(z < ballMinZ){//if ball hits the y walls
			ballVector.z = flipSign(ballVector.z);
		}
	}
	
	public Object3D getSphere(){
		return myBall;
	}
	
	public void gravMove(){
		System.out.println("~~~~~~~~~~STARTING MOVE~~~~~~~~~~");

		System.out.println("Vector X: "+ballVector.x+" Vector Y: " + ballVector.y+" Vector Z: "+ballVector.z);
		System.out.println("Delta is: " + delta);
		System.out.println("Starting Z velocity: "+velocityZ);
		velocityY += g/(2*delta);
		System.out.println("velocity we are adding to Z is: " + (g/(2*delta))*2);
		ballVector.x -= velocityX/delta;
		ballVector.y += velocityY/delta;
		ballVector.z -= velocityZ/delta;
		velocityY += g/(2*delta);
		System.out.println("Vector X: "+ballVector.x+" Vector Y: " + ballVector.y+" Vector Z: "+ballVector.z);
		System.out.println("Ending Z velocity: "+velocityZ);
		System.out.println("~~~~~~~~~~FINISHED MOVE~~~~~~~~~~");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();

		myBall.translate(ballVector);
	}
	
	public float flipSign(float num){
		if(num > 0){
			return -num;
		}
		else{
			return num;
		}
	}
	
	public void draw(Graphics g){
		//going to have to change this to 3D?
		g.fillOval((int) ballVector.x,(int) ballVector.y,(int) radius/2,(int) radius/2);
		
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
		collisionMove();
	}

	@Override
	public boolean requiresPolygonIDs() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
