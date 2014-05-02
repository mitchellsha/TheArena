package environment;

import javax.swing.*;

import shapes.Ball;
import shapes.BallList;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.threed.jpct.*;
import com.threed.jpct.util.*;

public class Player {
	private World world;
	private boolean alive = true;
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	private SimpleVector moveRes = new SimpleVector(0, 0, 0);
	private SimpleVector ellipsoid = new SimpleVector(8, 8, 8);
	private Object3D model;
	private static final float SCALE = 0.2f;
	private static final float DAMPING = 0.1f;
	private static final float SPEED = 2f;
	private static final float MAXSPEED = 3f;
	private BallList ballslist;
	float ind = 0;
	private int animKey = 2;//do a walk animation
	
	public Player(World w, KeyMapper k, BallList balls) {
		ballslist = balls;
		world = w;
		createPlayer();
	}

	private void createPlayer() {
//		model = Primitives.getCone(2);
		model = loadModel("src/res/figure.3ds", SCALE);
		
		model.translate(-50, -50, -50);
		model.setCollisionMode(Object3D.COLLISION_CHECK_SELF|Object3D.COLLISION_CHECK_OTHERS);
		model.addCollisionListener(new collision());
		createAnimation();
		world.addObject(model);	
	}

	public void move(KeyMapper keyMapper) {
		if (alive) {
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
				SimpleVector t = model.getZAxis();
				t.scalarMul(SPEED);
				moveRes.add(t);
			}

			if (down) {
				SimpleVector t = model.getZAxis();
				t.scalarMul(-SPEED);
				moveRes.add(t);
			}

			if (left) {
				model.rotateY((float) Math.toRadians(-4));
			}

			if (right) {
				model.rotateY((float) Math.toRadians(4));
			}

			if (moveRes.length() > MAXSPEED) {
				moveRes.makeEqualLength(new SimpleVector(0, 0, MAXSPEED));
			}

			model.translate(0, -0.02f, 0);

			moveRes = model.checkForCollisionEllipsoid(moveRes, ellipsoid, 8);
			model.translate(moveRes);
			SimpleVector t = new SimpleVector(0, 1, 0);
			t = model.checkForCollisionEllipsoid(t, ellipsoid, 1);

			model.translate(t);

			// damping
			if (moveRes.length() > DAMPING) {
				moveRes.makeEqualLength(new SimpleVector(0, 0, DAMPING));
			} else {
				moveRes = new SimpleVector(0, 0, 0);
			}
		}
	}

	public Object3D getObject() {
		return model;
	}

	public void doAnim() {
        {
            ind += 0.05f;
            if (ind > 1f) {
                ind -= 1f;
            }
        }
        model.animate(ind, animKey);
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
	
	public boolean getAlive(){
		return alive;
	}
	
	private void createAnimation() {
		Animation anim = new Animation(5);
        anim.createSubSequence("idle");
        anim.addKeyFrame(model.getMesh()); 
        
		anim.createSubSequence("walk");
        anim.addKeyFrame(loadModel("src/res/" + "walk2.3ds", SCALE).getMesh());
        anim.addKeyFrame(loadModel("src/res/" + "walk3.3ds", SCALE).getMesh());
        anim.addKeyFrame(loadModel("src/res/" + "walk4.3ds", SCALE).getMesh());
        anim.addKeyFrame(loadModel("src/res/" + "walk1.3ds", SCALE).getMesh());
  
        model.setAnimationSequence(anim);
	}
	@SuppressWarnings("serial")
	private class collision implements CollisionListener {

		@Override
		public void collision(CollisionEvent c1) {
			for (Object3D o: c1.getTargets()) {
				if (ballslist.contains(o.getID()-1)) {
					System.out.println("you're dead.");
					alive = false;
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
