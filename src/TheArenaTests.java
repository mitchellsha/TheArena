import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import shapes.Ball;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

import environment.Playspace;
import environment.Ticker;

public class TheArenaTests
{	
	private World world;
	
	@Before
	public void setup()
	{
		world = new World();
	}
	
	@Test
	public void tickerTest()
	{
		int rate = 10;
		int ticks = 10;
		Ticker ticker = new Ticker(rate);
		long start = ticker.getTime();
		long stop = ticker.getTime();
		
		while(stop-start < rate*ticks)
			stop = ticker.getTime();
		
		assertTrue(ticker.getTicks() >= ticks);
	}
	
	@Test
	public void abstractionTest()
	{
		SimpleVector startVector = SimpleVector.ORIGIN;
		Ball ball = new Ball(startVector, world, 1, 1, 1, 0, 0, 0);
		world.addObject(ball.getSphere());
		assertEquals(startVector, ball.getVector());
		assertFalse(startVector == ball.getVector());
		
		SimpleVector afterVector = ball.gravMove();
		assertNotEquals(startVector, afterVector);
		assertEquals(afterVector, ball.getVector());
		afterVector = ball.getVector();
		assertNotEquals(startVector, afterVector);
		assertFalse(afterVector == ball.getVector());
		
		assertNotEquals(afterVector, ball.gravMove());
		assertNotEquals(afterVector, ball.getVector());
	}
	
	@Test
	public void ballGravityTest()
	{
		SimpleVector startVector = SimpleVector.ORIGIN;
		Ball ball = new Ball(startVector, world, 1, 1, 1, 0, 0, 0);
		world.addObject(ball.getSphere());
		
		SimpleVector curVector = ball.gravMove();
		assertNotEquals(curVector, startVector);
		
		float pastHeight = startVector.y;
		float curHeight = curVector.y;
		float curDist = curHeight - pastHeight;
		float pastDist = curDist;
		
		curVector = ball.gravMove();
		pastHeight = curHeight;
		curHeight = curVector.y;
		curDist = curHeight - pastHeight;
		pastDist = curDist;
		
		assertTrue(pastHeight > curHeight);		//It seems that the y-axis is upside-down
		assertTrue(curDist > pastDist);			//It seems that the y-axis is upside-down
	}
	
	@Test
	public void collisionTest()
	{
		Object3D plane = Primitives.getPlane(20,10);
		plane.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		world.addObject(plane);
		
		SimpleVector startVector = plane.getOrigin();
		startVector = new SimpleVector(startVector.x, startVector.y - 100, startVector.z);
		Ball ball = new Ball(startVector, world, 1, 1, 1, 0, 0, 0);
		world.addObject(ball.getSphere());
		
		SimpleVector curVector = ball.gravMove();
		assertNotEquals(curVector, startVector);
		
		float pastHeight = startVector.y;
		float curHeight = curVector.y;
		int count = 1;
		
		while(curHeight > pastHeight)
		{
			pastHeight = curHeight;
			curVector = ball.gravMove();
			curHeight = curVector.y;
			count++;
		}
		
		for(int i = 0; i < count*5; i++)
		{
			pastHeight = curHeight;
			curVector = ball.gravMove();
			curHeight = curVector.y;
		}
		
		assertTrue(curHeight > pastHeight);
	}
}
