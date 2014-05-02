package shapes;

import java.awt.Color;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import environment.Player;

@SuppressWarnings("serial")
public class coneFood extends Object3D implements CollisionListener {
	private SimpleVector position;
	private World world;
	private float scale;
	private Object3D food;
	private int playerID;
	private int range;
	private float Y;
	private int pointsTally;
	
	public coneFood(SimpleVector vector, World world, int resolution, float scale, int playerID) {
		super(resolution);
		range = 90;
		pointsTally = 0;
		position = vector;
		this.world = world;
		this.scale = scale;
		this.playerID = playerID + 2;
		food = Primitives.getCube(scale);
		food.setOrigin(position);
		food.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS | Object3D.COLLISION_CHECK_SELF);
		food.setCollisionOptimization(true);
		food.addCollisionListener(this);
		world.addObject(food);
		Y = -10;
	}

	@Override
	public void collision(CollisionEvent ce) {
		// TODO Auto-generated method stub
		Object3D[] obj = ce.getTargets();
		
		//SimpleVector impactLocation = ce.getFirstContact()
		for(Object3D target: obj){
			System.out.println("this is an angela println " + target.getID() + " " +  playerID);
			if (target.getID() == playerID) {
				float randX = (float)Math.random()*range;
				float randZ = (float)Math.random()*range;
				food.setOrigin(new SimpleVector(randX, Y, randZ));
				pointsTally +=5;
			}
		}
	}

	@Override
	public boolean requiresPolygonIDs() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Object3D getObject() {
		return food;
	}
	
	public int getPoints() {
		return pointsTally;
	}
	
	public void rotate() {
		food.rotateY((float) (Math.toRadians(.1)));
	}
	
}
