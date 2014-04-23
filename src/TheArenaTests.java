import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.threed.jpct.SimpleVector;

import Shape.Ball;
import environment.playspace;

public class TheArenaTests
{
	private playspace arena;
	private Ball ball;
	
	@Before
	public void setup()
	{
		ball = new Ball(new SimpleVector(), 20, 10, 10, 10, 10);
		ball.build();
	}
	
	@Test
	public void arenaTest()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void physicsTest()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void moveBall()
	{
		assertFalse(ball.requiresPolygonIDs());
	}
	
	@Test
	public void characterTest()
	{
		fail("Not yet implemented");
	}
}
