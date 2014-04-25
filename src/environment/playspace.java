package environment;

import javax.swing.*;

import shapes.Ball;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import com.threed.jpct.*;
import com.threed.jpct.util.*;

public class Playspace extends JFrame {

	private static final long serialVersionUID = 1L; Graphics g = null;
	private KeyMapper keyMapper = null;
	private FrameBuffer fb = null;
	private World world = null;
	public Object3D plane = null;
	private Object3D plane2 = null;
	private Object3D plane3 = null;
	private Object3D plane4 = null;
	private Object3D plane5 = null;
	private boolean doloop = true;
	private Player player;
	private ArrayList<Ball> Balls, BallsToRemove;
	private int tickRateMS;

	public Playspace() {

		int numberOfProcs = Runtime.getRuntime().availableProcessors();

		Config.useMultipleThreads = numberOfProcs > 1;
		Config.useMultiThreadedBlitting = numberOfProcs > 1;
		Config.loadBalancingStrategy = 1;
		Config.maxNumberOfCores = numberOfProcs;
		Config.lightMul = 1;
		Config.mtDebug = true;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1024, 768);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		g = getGraphics();
		tickRateMS = 1;
		Balls = new ArrayList<Ball>();
		BallsToRemove = new ArrayList<Ball>();

	}

	private void initializeWorld() {
		fb = new FrameBuffer(1024, 768, FrameBuffer.SAMPLINGMODE_NORMAL);
		keyMapper = new KeyMapper(this);
		fb.enableRenderer(IRenderer.RENDERER_SOFTWARE);

		world = new World();

		plane = Primitives.getPlane(20, 10);
		plane.rotateX((float) Math.PI / 2f);
		plane.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		world.addObject(plane);

		plane2 = Primitives.getPlane(20,10);
		plane2.rotateY((float) Math.PI / 2f);
		plane2.translate(new SimpleVector(100,-100,0));
		plane2.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		world.addObject(plane2);

		plane3 = Primitives.getPlane(20,10);
		plane3.rotateY((float) -Math.PI / 2f);
		plane3.translate(new SimpleVector(-100, -100, 0));
		plane3.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		world.addObject(plane3);

		plane4 = Primitives.getPlane(20, 10);
		plane4.rotateZ((float) Math.PI / 2f);
		plane4.translate(new SimpleVector(0,-100,100));
		plane4.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		world.addObject(plane4);

		plane5 = Primitives.getPlane(20, 10);
		plane5.rotateY((float) Math.toRadians(180));
		plane5.translate(new SimpleVector(0,-100,-100));
		plane5.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		world.addObject(plane5);

		player = new Player(world, keyMapper, plane, Balls);

		Light light = new Light(world);
		light.setPosition(new SimpleVector(0, -80, 0));
		light.setIntensity(140, 120, 120);
		light.setAttenuation(-1);
		world.setAmbientLight(20, 20, 20);

		world.buildAllObjects();
	}


	private void startGame() {

		Camera cam = world.getCamera();
		cam.moveCamera(Camera.CAMERA_MOVEOUT, 100);
		cam.moveCamera(Camera.CAMERA_MOVEUP, 100);

		Ticker ticker = new Ticker(tickRateMS);
		int belowFloor = 0;
		Random r = new Random();
		
		while (doloop) {
			player.move();
			cam.setPositionToCenter(player.getObject());
			cam.align(player.getObject());

			cam.rotateCameraX((float) Math.toRadians(30));
			cam.moveCamera(Camera.CAMERA_MOVEOUT, 100);

			
			int randInt = r.nextInt(50);
			if(randInt == 4){
				SimpleVector randomVector = new SimpleVector(0, -Math.random()*(130-50), 0);
				Ball newBall = new Ball(randomVector, world, 10, 2, tickRateMS,r.nextInt(30), r.nextInt(30),r.nextInt(30));
				createBallInWorld(newBall);
			}
			long ticks = ticker.getTicks();
			BallsToRemove = new ArrayList<Ball>();
			if (ticks > 0) {
				if(Balls.size()>0){
					for(Ball b:Balls){
						SimpleVector howMuchBallMoved = b.gravMove();
						if(b.getVector().y>belowFloor){
							BallsToRemove.add(b);
						}
					}
					for(Ball d:BallsToRemove){
						Balls.remove(d);
						world.removeObject(d.getSphere());
					}
				}
			}
			
			fb.clear(Color.BLUE);
			world.renderScene(fb);
			world.draw(fb);

			fb.update();
			fb.display(g);
		}

		fb.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		System.exit(0);
	}
	
	private void createBallInWorld(Ball ball){
		Balls.add(ball);
		ball.build();
		ball.getSphere().build();
		//world.addObject(ball);
		world.addObject(ball.getSphere());
	}

	public void lockCheck(){
		for(Ball first:Balls){
			for(Ball second: Balls){
				double firstXVector = (double) first.getVector().x;
				double firstYVector = (double) first.getVector().y;
				double firstZVector = (double) first.getVector().z;
				double secondXVector = (double) second.getVector().x;
				double secondYVector = (double) second.getVector().y;
				double secondZVector = (double) second.getVector().z;

				double distance = Math.sqrt(Math.pow(firstXVector,secondXVector) + Math.pow(firstYVector,secondYVector) + Math.pow(firstZVector,secondZVector));
				if(distance <= first.getSize() + second.getSize()){

				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		Playspace cd = new Playspace();
		cd.initializeWorld();
		cd.startGame();
	}
}