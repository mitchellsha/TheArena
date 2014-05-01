package environment;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import shapes.Ball;

import com.threed.jpct.Animation;
import com.threed.jpct.CollisionEvent;
import com.threed.jpct.CollisionListener;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.util.KeyMapper;
import com.threed.jpct.util.KeyState;

public class PlayerAnimated {
	private World world;
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	private KeyMapper keyMapper;
	private SimpleVector moveRes = new SimpleVector(0, 0, 0);
	private SimpleVector ellipsoid = new SimpleVector(2, 2, 2);
	private Object3D figure;
	private static final float DAMPING = 0.1f;
	private static final float SPEED = 1f;
	private static final float MAXSPEED = 1f;
	public Object3D plane;
	private ArrayList<Ball> ballslist;
	private float scale = .3f;
	private int an = 2;//do a walk animation
	private float ind = 0;

	public PlayerAnimated(World w, KeyMapper k, Object3D p, ArrayList<Ball> balls) {
		ballslist = balls;
		keyMapper = k;
		world = w;
		plane = p;
		figure = loadModel("src/res/figure.3ds", scale);
//		
		figure.translate(-50, -100, -50);
		figure.setCollisionMode(Object3D.COLLISION_CHECK_SELF|Object3D.COLLISION_CHECK_OTHERS);
		figure.addCollisionListener(new collision());
		createAnimation();
		world.addObject(figure);
	}

	public void move() {
		KeyState ks = null;

		while ((ks = keyMapper.poll()) != KeyState.NONE) {
			if (ks.getKeyCode() == KeyEvent.VK_UP) {
				up = ks.getState();
			}
			if (ks.getKeyCode() == KeyEvent.VK_DOWN) {
				down = ks.getState();
			}
			if (ks.getKeyCode() == KeyEvent.VK_LEFT) {
				left = ks.getState();
			}
			if (ks.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = ks.getState();
			}
		}
		
		if(up || down || left || right) doAnim();

		// move the cube
		if (up) {
			SimpleVector t = figure.getZAxis();
			t.scalarMul(SPEED);
			moveRes.add(t);
		}

		if (down) {
			SimpleVector t = figure.getZAxis();
			t.scalarMul(-SPEED);
			moveRes.add(t);
		}

		if (left) {
			figure.rotateY((float) Math.toRadians(-1));
		}

		if (right) {
			figure.rotateY((float) Math.toRadians(1));
		}

		if (moveRes.length() > MAXSPEED) {
			moveRes.makeEqualLength(new SimpleVector(0, 0, MAXSPEED));
		}

		figure.translate(0, -0.02f, 0);

		moveRes = figure.checkForCollisionEllipsoid(moveRes, ellipsoid, 8);
		figure.translate(moveRes);
		SimpleVector t = new SimpleVector(0, 1, 0);
		t = figure.checkForCollisionEllipsoid(t, ellipsoid, 1);

		figure.translate(t);

		// damping
		if (moveRes.length() > DAMPING) {
			moveRes.makeEqualLength(new SimpleVector(0, 0, DAMPING));
		} else {
			moveRes = new SimpleVector(0, 0, 0);
		}
	}

	public Object3D getObject() {
		return new Object3D(figure);
	}
	
	public void doAnim() {
        {
            ind += 0.05f;
            if (ind > 1f) {
                ind -= 1f;
            }
        }
        figure.animate(ind, an);
    }
	
	private Object3D loadModel(String filename, float scale) {
        Loader.setVertexOptimization(false);
        Object3D[] model = Loader.load3DS(filename, scale);

        Object3D o3d = new Object3D(0);

        Object3D temp = null;

        for (int i = 0; i < model.length; i++) {
            temp = model[i];
            temp.setCenter(SimpleVector.ORIGIN);
            temp.rotateX((float)( -.5*Math.PI));
            temp.rotateMesh();
            temp.setRotationMatrix(new Matrix());
            o3d = Object3D.mergeObjects(o3d, temp);
            o3d.build();
        }
        return o3d;
    }
	
	private void createAnimation()
	{
		Animation anim = new Animation(5);
        anim.createSubSequence("idle");
        anim.addKeyFrame(figure.getMesh()); 
        
		anim.createSubSequence("walk");
        anim.addKeyFrame(loadModel("src/res/" + "walk2.3ds", scale).getMesh());
        anim.addKeyFrame(loadModel("src/res/" + "walk3.3ds", scale).getMesh());
        anim.addKeyFrame(loadModel("src/res/" + "walk4.3ds", scale).getMesh());
        anim.addKeyFrame(loadModel("src/res/" + "walk1.3ds", scale).getMesh());
  
        figure.setAnimationSequence(anim);
	}

	@SuppressWarnings("serial")
	private class collision implements CollisionListener {

		@Override
		public void collision(CollisionEvent c1) {
			for (Object3D o: c1.getTargets()) {
				for (Ball b: ballslist)
					if (b.getSphere().equals(o)){
						for (int i = 0; i < 100; i ++) {
							System.out.println("YOU'RE DEAD SON");
						}
					}
			}
		}

		@Override
		public boolean requiresPolygonIDs() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
