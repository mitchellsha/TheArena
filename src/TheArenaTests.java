import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import shapes.Ball;
import shapes.BallList;
import shapes.Food;

import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.KeyMapper;

import environment.Frame;
import environment.Player;

public class TheArenaTests
{	
	private World world;
	private BallList bList;
	private HashMap<Integer, String> walls;
	
	@Before
	public void setup()
	{
		world = new World();
		bList = new BallList();
		walls = new HashMap<Integer,String>();
	}
	
	@Test
	public void abstractionTest()
	{
		SimpleVector startVector = SimpleVector.ORIGIN;
		Ball ball = new Ball(startVector, walls, bList, 1, 1, 10, 0, 0, 0);
		world.addObject(ball.getSphere());
		assertEquals(startVector, ball.getVector());
		assertFalse(startVector == ball.getVector());
		
		ball.gravMove();
		SimpleVector afterVector = ball.getVector();
		assertNotEquals(startVector, afterVector);
		assertFalse(afterVector == ball.getVector());
		
		ball.gravMove();
		assertNotEquals(afterVector, ball.getVector());
	} 
	
	@Test
	public void collisionTest()
	{
		Object3D floor = createFloor();
		world.addObject(floor);
		walls.put(floor.getID(), "floor");
		
		SimpleVector startVector = floor.getOrigin();
		float floorHeight = startVector.y;
		float startHeight = floorHeight - 25;
		startVector = new SimpleVector(startVector.x, startHeight, startVector.z);
		Ball ball = new Ball(startVector, walls, bList, 1, 1, 10, 0, 0, 0);
		createBallInWorld(ball);
		
		ball.gravMove();
		SimpleVector curVector = ball.getVector();
		assertNotEquals(curVector, startVector);
		
		float curHeight = curVector.y;
		assertNotEquals(curHeight, startHeight);
		
		for(int i = 0; i < 100; i++)
		{
			ball.gravMove();
			curVector = ball.getVector();
			curHeight = curVector.y;
			assertFalse(curHeight > floorHeight);	//This fails and I don't know why
		}
	}
	
	@Test
	public void foodCollectionTest()
	{
		Player player = new Player(world, new KeyMapper(new Frame()), bList);
		SimpleVector playVector = new SimpleVector(player.getObject().getTransformedCenter());
		SimpleVector foodVector = new SimpleVector(playVector.x-50f, playVector.y, playVector.z-50f);
		Food food = new Food(new SimpleVector(foodVector), world, 1, 1, player.getObject().getID());
		world.addObject(food);
		
		player.getObject().translate(food.getTransformedCenter());
		assertNotEquals(food.getOrigin(), foodVector);
	}
	
	@Test
	public void foodRotationTest()
	{
		Player player = new Player(world, new KeyMapper(new Frame()), bList);
		SimpleVector playVector = new SimpleVector(player.getObject().getTransformedCenter());
		SimpleVector foodVector = new SimpleVector(playVector.x-50f, playVector.y, playVector.z-50f);
		Food food = new Food(new SimpleVector(foodVector), world, 1, 1, player.getObject().getID());
		world.addObject(food);
		
		Matrix pre = new Matrix(food.getObject().getRotationMatrix());
		food.rotate();
		assertNotEquals(food.getObject().getRotationMatrix(), pre);
	}
	
	private void createBallInWorld(Ball ball)
	{
		ball.build();
		ball.getSphere().build();
		world.addObject(ball.getSphere());
		bList.addToBallList(ball.getID(), ball);
	}
	
	private Object3D createFloor()
	{
		Object3D floor = Primitives.getPlane(20,10);
		floor.rotateX((float) Math.PI / 2f);
		floor.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		floor.build();
		
		return floor;
	}
	
	
	@Test
    public void playerTest(){
            Frame frame = new Frame();
            Object3D floor = createFloor();
            frame.setVisible(false);
            BallList bl = new BallList();
            KeyMapper km = new KeyMapper(frame);
            Player p = new Player(world,km,bl);
            assertTrue(p.getAlive());      

    }
	
    @Test
    public void listTest(){
            int offset = 10;
            for(int i = 0; i<5; i++){
                    SimpleVector v = new SimpleVector(i*offset,i*offset,i*offset);
                    Ball b = new Ball(v, walls, bList, 1, 2, 1, i*offset,i*offset,i*offset);
                    bList.addToBallList(i, b);
            }
            assertTrue(bList.size() == 5);
            bList.removeFromBallList(4);
            assertTrue(bList.size() == 4);
            assertFalse(bList.contains(4));
            assertTrue(bList.contains(0));
            HashMap redundant = bList.getBallList();
            assertEquals(redundant.size(), 4);
            assertTrue(bList.get(0).getCenter().x == 0);
            assertTrue(bList.get(0).getCenter().y == 0);
            assertTrue(bList.get(0).getCenter().z == 0);
    }
}


