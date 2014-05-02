package shapes;

import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;


@SuppressWarnings("serial")
public class Food extends Object3D implements CollisionListener {
	private SimpleVector position;
	private World world;
	private float scale;
	private Object3D food;
	private int playerID;
	private int range;
	private float Y;
	private int pointsTally;
	private boolean sign = false;
	
	public Food(SimpleVector vector, World world, int resolution, float scale, int playerID) {
		super(resolution);
		range = 90;
		pointsTally = 0;
		position = vector;
		this.world = world;
		this.scale = scale;
		this.playerID = playerID;
		food = Primitives.getCube(scale);
		food.setOrigin(position);
		food.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		food.setCollisionOptimization(true);
		food.addCollisionListener(this);
		world.addObject(food);
		Y = -10;
	}

	@Override
	public void collision(CollisionEvent ce) {
		// TODO Auto-generated method stub
		Object3D obj = ce.getSource();
		if (obj.getID() == playerID) {
			if(sign){
				float randX = (float) Math.random()*range;
				float randZ = (float) Math.random()*range;
				sign = false;
				food.setOrigin(new SimpleVector(randX, Y, randZ));


			}
			else{
				float randX = (float) -Math.random()*range;
				float randZ = (float) -Math.random()*range;
				sign = true;
				food.setOrigin(new SimpleVector(randX, Y, randZ));
			}
			pointsTally +=1;
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
		food.rotateY((float) (Math.toRadians(3)));
	}
	
}