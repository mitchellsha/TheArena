package environment;

import javax.swing.*;

import shapes.Ball;
import shapes.BallList;
import shapes.Food;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


import com.threed.jpct.*;
import com.threed.jpct.util.*;

public class PlayspacePanel extends JPanel {

	private FrameBuffer fb;
	private static final long serialVersionUID = 1L;
	private KeyMapper keyMapper;
	private World world = null;
	private Object3D floor, closeWall, rightWall, leftWall, farWall;
	private Player player;
	private ArrayList<Ball> Balls, BallsToRemove;
	private HashMap<Integer, String> WallIDs;
	private int tickRateMS;
	private BallList ballList;
	private TextureManager tm;
	private Timer animator;
	private Random rand = new Random();
	private Food food;
	private Camera cam;
	boolean up = false, down = false, left = false, right = false;
	private Frame frame;
	private Projector projector;
	private ShadowHelper sh = null;

	public PlayspacePanel(Frame f) {
		frame = f;
		keyMapper = new KeyMapper(frame);
		int numberOfProcs = Runtime.getRuntime().availableProcessors();

		Config.useMultipleThreads = numberOfProcs > 1;
		Config.useMultiThreadedBlitting = numberOfProcs > 1;
		Config.loadBalancingStrategy = 1;
		Config.maxNumberOfCores = numberOfProcs;
		Config.lightMul = 1;
		Config.mtDebug = true;
		tickRateMS = 10;
		Balls = new ArrayList<Ball>();
		BallsToRemove = new ArrayList<Ball>();
		WallIDs = new HashMap<Integer,String>();
		ballList = new BallList();

		initializeWorld();
		startGame();

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
		fb.enableRenderer(IRenderer.RENDERER_SOFTWARE);


		world = new World();	
		cam = world.getCamera();
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

		player = new Player(world, keyMapper, ballList);
		SimpleVector randomFoodVector = new SimpleVector(30,-5,30);
		food = new Food(randomFoodVector, world, 10, 3, player.getObject().getID());
		giveTexture(food.getObject(), "chevron");
		food.getObject().setAdditionalColor(Color.RED);
		food.getObject().setTransparency(0);

		Light light = new Light(world);
		light.setPosition(new SimpleVector(0, -80, 0));
		light.setIntensity(140, 120, 120);
		light.setAttenuation(-1);
		world.setAmbientLight(20, 20, 20);
		populateWallIds();
		initializeShadows();
		world.buildAllObjects();
	}
	
	private void initializeShadows(){
		projector = new Projector();
		projector.setFOV(1.5f);
		projector.setYFOV(1.5f);

		sh = new ShadowHelper(world, fb, projector, 2048);
		sh.setCullingMode(false);
		sh.setAmbientLight(new Color(20,20,20));
		sh.setLightMode(true);
		sh.setBorder(1);

		sh.addReceiver(floor);

	}

	private void startGame() {
		cam.moveCamera(Camera.CAMERA_MOVEOUT, 100);
		cam.moveCamera(Camera.CAMERA_MOVEUP, 100);

		animator = new Timer(tickRateMS, new Animator());
		animator.start();
	}

	private class Animator implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(player.getAlive()){
				frame.setScoreToUser(food.getPoints());
				food.rotate();
				int randInt = rand.nextInt(25);

				if(randInt == 4){
					if(!(Balls.size()>15)){
						SimpleVector randomVector = new SimpleVector(0, -Math.random()*(130-50), 0);
						Ball newBall = new Ball(randomVector, WallIDs, ballList, 10, 4, tickRateMS,rand.nextInt(30), rand.nextInt(30),rand.nextInt(30));
						createBallInWorld(newBall);
					}
				}

				BallsToRemove = new ArrayList<Ball>();
				if(Balls.size()>0){
					for(Ball b:Balls){
						b.gravMove();
						b.updateBallList(ballList);
						if(b.getVector().y>0){
							BallsToRemove.add(b);
						}
					}
					for(Ball d:BallsToRemove){
						Balls.remove(d);
						world.removeObject(d.getSphere());
					}
				}
				move();
				draw();
			}
			else{
				createEndOfGame();
			}
			sh.updateShadowMap();

		}
	}

	public void draw() {
		fb.clear(Color.BLACK);
		world.renderScene(fb);
		world.draw(fb);
		fb.update();
		repaint();
	}


	private void createEndOfGame(){
		JOptionPane.showMessageDialog(null, "You're dead. Your score was: "+ food.getPoints()+"\nThanks for playing!");
		System.exit(0);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		fb.display(g);
	}

	private void createBallInWorld(Ball ball){
		Balls.add(ball);
		ball.build();
		ball.getSphere().build();
		ball.getSphere().setTexture("basketball");
		ball.getSphere().calcTextureWrap();
		ball.getSphere().setAdditionalColor(Color.orange);
		ball.getSphere().compileAndStrip();
		world.addObject(ball.getSphere());
		ballList.addToBallList(ball.getID(), ball);
		sh.addCaster(ball);
	}

	public void giveTexture(Object3D object, String texname) {
		object.setTexture(texname);
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

	public void move() {
		cam.setPositionToCenter(player.getObject());
		cam.align(player.getObject());
		cam.rotateCameraX((float) Math.toRadians(30));
		cam.moveCamera(Camera.CAMERA_MOVEOUT, 175);
		player.move(keyMapper);
	}
	
	public int getScore(){
		return food.getPoints();
	}
}