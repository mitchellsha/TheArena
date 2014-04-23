import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.World;

public class Ball extends Object3D implements CollisionListener{
	private static World world;
	private float x, y, z;
	private float speedX, speedY, speedZ;
	private double velocityX, velocityY, velocityZ;
	private float radius;
	private final double gravity = 9.8;
	private double mass;
	private Color color;
	float ballScale;
	private double heading;
	static Object3D myBall;
	
	public Ball(int resolution, float scale, float size, float direciton, double weight){
		super(myBall);
		ballScale = scale;
		mass = weight;
		speedX = 10;
		speedY = 10;
		speedZ = 10;
		
		
		radius = size;
		heading = Math.toRadians(direciton);
		
		velocityX = speedX*heading;
		velocityY = speedY*heading;
		velocityZ = speedZ*heading;
		
		

		myBall = Primitives.getSphere(resolution, scale);
		
	}
	
	public void move(){
		//check collision detection with all other balls in arena
		//check collision detection with walls
		//check collision detection with person
		double ballMinX = 0;
		double ballMinY = 0;
		double ballMinZ = 0;
		
		x += speedX;
		y += speedY;
		z += speedZ;
		
		if(x <= ballMinX){
			
		} else if(y < ballMinY){
			
		} else if(z < ballMinZ){
			
		}
	}
	
	public void draw(Graphics g){
		
	}
	
	public float getSpeed(){
		return (float)Math.sqrt(speedX*speedX + 
				speedY*speedY + speedZ*speedZ);
	}

	@Override
	public void collision(CollisionEvent e) {
		// TODO Auto-generated method stub
		Object3D obj = e.getSource();
		
	}

	@Override
	public boolean requiresPolygonIDs() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
