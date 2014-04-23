package Shape;
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

	
	public Ball(SimpleVector vector, int resolution, float scale, float size, float direciton, double weight){
		super(myBall);
		ballScale = scale;
		mass = weight;
		ballVector = vector;
		
		radius = size;
		heading = Math.toRadians(direciton);
		
		velocityX = speedX*heading;
		velocityY = speedY*heading;
		velocityZ = speedZ*heading;
		
		myBall = Primitives.getSphere(resolution, scale);
		
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
	
	public void gravMove(){
		velocityZ -= g/2;
		ballVector.x -= velocityX;
		ballVector.y -= velocityY;
		ballVector.z += velocityZ;
		velocityZ -= g/2;
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
