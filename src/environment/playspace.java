package environment;

import javax.swing.*;

import shapes.Ball;
import shapes.BallList;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.threed.jpct.*;
import com.threed.jpct.util.*;

public class Playspace extends JFrame {

	private static final long serialVersionUID = 1L; Graphics g = null;
	private KeyMapper keyMapper = null;
	private FrameBuffer fb = null;
	private World world = null;
	public Object3D floor = null;
	private Object3D rightWall = null;
	private Object3D leftWall = null;
	private Object3D farWall = null;
	private Object3D closeWall = null;
	private boolean doloop = true;
//	private Player player;
	private PlayerAnimated player;
	private ArrayList<Ball> Balls, BallsToRemove;
	private HashMap<Integer, String> WallIDs;
	private int tickRateMS;
	private BallList ballList;
	private TextureManager tm;


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
		WallIDs = new HashMap<Integer,String>();
		ballList = new BallList();

	}

	private void initializeWorld() {
		tm = TextureManager.getInstance();
		tm.addTexture("gift", new Texture("src/gift.jpg"));
		tm.addTexture("chevron", new Texture("src/chevron.jpg"));
		tm.addTexture("rubber ball", new Texture("src/rubberball.jpg"));
		tm.addTexture("basketball", new Texture("src/basketball.jpg"));
		tm.addTexture("wood floor", new Texture("src/woodfloor.jpg"));
		tm.addTexture("space", new Texture("src/space.jpg"));
		
		fb = new FrameBuffer(1024, 768, FrameBuffer.SAMPLINGMODE_NORMAL);
		keyMapper = new KeyMapper(this);
		fb.enableRenderer(IRenderer.RENDERER_SOFTWARE);

		world = new World();		
		floor = Primitives.getPlane(20, 10);
		floor.rotateX((float) Math.PI / 2f);
		floor.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		giveTexture(floor, "wood floor");
		world.addObject(floor);

		rightWall = Primitives.getPlane(20,10);
		rightWall.rotateY((float) Math.PI / 2f);
		rightWall.rotateX((float) Math.PI / 2f);
		rightWall.translate(new SimpleVector(100,-100,0));
		rightWall.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		giveTexture(rightWall, "space");
		world.addObject(rightWall);

		leftWall = Primitives.getPlane(20,10);
		leftWall.rotateY((float) -Math.PI / 2f);
		rightWall.rotateX((float) -Math.PI / 2f);
		leftWall.translate(new SimpleVector(-100, -100, 0));
		leftWall.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		giveTexture(leftWall, "space");
		world.addObject(leftWall);

		farWall = Primitives.getPlane(20, 10);
		farWall.rotateZ((float) Math.PI / 2f);
		farWall.translate(new SimpleVector(0,-100,100));
		farWall.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		giveTexture(farWall, "space");
		world.addObject(farWall);

		closeWall = Primitives.getPlane(20, 10);
		closeWall.rotateY((float) Math.toRadians(180));
		closeWall.translate(new SimpleVector(0,-100,-100));
		closeWall.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		giveTexture(closeWall, "space");
		world.addObject(closeWall);
		Random r = new Random();
//		SimpleVector randomVector = new SimpleVector(0,-130,0);
//		Ball newBall = new Ball(randomVector, WallIDs, ballList, 10, 2, tickRateMS,0, 0,0);
//		createBallInWorld(newBall);
//		SimpleVector randomVector2 = new SimpleVector(0,-50,0);
//		Ball newBall2 = new Ball(randomVector2, WallIDs, ballList, 10, 2, tickRateMS,0, 0,0);
//		createBallInWorld(newBall2);
		
//		player = new Player(world, keyMapper, floor, Balls);
		player = new PlayerAnimated(world, keyMapper, floor, Balls);

		Light light = new Light(world);
		light.setPosition(new SimpleVector(0, -80, 0));
		light.setIntensity(140, 120, 120);
		light.setAttenuation(-1);
		world.setAmbientLight(20, 20, 20);
		populateWallIds();
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
			cam.moveCamera(Camera.CAMERA_MOVEOUT, 300);

			int randInt = r.nextInt(50);
			if(randInt == 4){
				SimpleVector randomVector = new SimpleVector(0, -Math.random()*(130-50), 0);
				Ball newBall = new Ball(randomVector, WallIDs, ballList, 10, 2, tickRateMS,r.nextInt(30), r.nextInt(30),r.nextInt(30));
				createBallInWorld(newBall);
			}
			long ticks = ticker.getTicks();
			BallsToRemove = new ArrayList<Ball>();
			if (ticks > 0) {
				if(Balls.size()>0){
					for(Ball b:Balls){
						b.gravMove();
						b.updateBallList(ballList);
						//System.out.println("The ball with id: " + b.getID() + " has a size of " + b.getBallList().size());
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
		ball.getSphere().setTexture("basketball");
		ball.getSphere().calcTextureWrap();
		ball.getSphere().setAdditionalColor(Color.orange);
		ball.getSphere().compileAndStrip();
		world.addObject(ball.getSphere());
		System.out.println("added the ball id " + ball.getID() + " to ball list");
		ballList.addToBallList(ball.getID());
		System.out.println(ballList.get(ballList.size()-1));
	}
	
	public void giveTexture(Object3D object, String texname) {
		object.setTexture(texname);
		//object.calcTextureWrap();
		object.setLighting(Object3D.LIGHTING_ALL_ENABLED);
		object.setSpecularLighting(true);
		object.setAdditionalColor(Color.gray);
		object.compileAndStrip();
	}
	
	public void populateWallIds(){
		System.out.println(floor.getID()+" " +rightWall.getID()+" " +leftWall.getID()+" " +farWall.getID()+" " +closeWall.getID());
		WallIDs.put(floor.getID(),"Bottom");
		WallIDs.put(rightWall.getID(),"Right");
		WallIDs.put(leftWall.getID(),"Left");
		WallIDs.put(farWall.getID(),"Far");
		WallIDs.put(closeWall.getID(),"Close");

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