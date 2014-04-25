import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import shapes.Ball;

import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

import environment.Playspace;

public class TheArenaTests
{
	
	@Before
	public void setup()
	{
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void arenaTest()
	{
		
	}
	
	@Test
	public void abstractionTest()
	{
		SimpleVector startVector = SimpleVector.ORIGIN;
		Ball ball = new Ball(startVector, new World(), 1, 1, 1, 0, 0, 0);
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
	public void gravityTest()
	{
		SimpleVector startVector = SimpleVector.ORIGIN;
		double yVelo = -9.8/2;
		Ball ball = new Ball(startVector, new World(), 1, 1, 1, 0, yVelo, 0);
		System.out.println("Start: "+startVector);
		System.out.println("velo: "+yVelo);
		
		ball.gravMove();
		SimpleVector curVector = ball.getVector();
		System.out.println("Start: "+startVector+"\tCurrent:"+curVector);
		assertNotEquals(startVector, curVector);
		SimpleVector firstVector = new SimpleVector(curVector);
		
		ball.gravMove();
		curVector = ball.getVector();
		System.out.println("Start: "+startVector+"\tFirst:"+firstVector+"\tCurrent:"+curVector);
		assertNotEquals(startVector, curVector);
		SimpleVector secondVector = new SimpleVector(curVector);
		
		ball.gravMove();
		curVector = ball.getVector();
		System.out.println("Start: "+startVector+"\tFirst:"+firstVector+"\tSecond: "+secondVector+"\tCurrent:"+curVector);
		assertNotEquals(startVector, curVector);
		assertEquals(secondVector, curVector);
		
		ball.gravMove();
		curVector = ball.getVector();
		System.out.println("Start: "+startVector+"\tFirst:"+firstVector+"\tCurrent:"+curVector);
		assertNotEquals(startVector, curVector);
		assertEquals(firstVector, curVector);
	}
	
	@Test
	public void ballCollisionTest()
	{
		World world = new World();
		SimpleVector vector1 = new SimpleVector(30, 0, -40);
		SimpleVector vector2 = new SimpleVector(-vector1.x, vector1.y, -vector1.z);
		Ball ball1 = new Ball(vector1, world, 1, 1, 1, vector1.x, 0, vector1.z);
		Ball ball2 = new Ball(vector2, world, 1, 1, 1, vector2.x, 0, vector2.z);
		
		for(int i = 0; i < 5; i++) //In the ball class, delta is 5
		{
			System.out.println("count: "+i);
			assertNotEquals(vector1, vector2);
			
			ball1.gravMove();
			vector1 = ball1.getVector();
			assertNotEquals(vector1, vector2);
			
			ball2.gravMove();
			vector2 = ball2.getVector();
		}

		assertNotEquals(vector1, vector2);	//Collision should have occurred
	}
	
	@Test
	public void characterTest()
	{
		
	}
}
