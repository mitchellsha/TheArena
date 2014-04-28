package environment;

import javax.swing.*;

import shapes.Ball;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.threed.jpct.*;
import com.threed.jpct.util.*;

public class Player {
	private World world;
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	private KeyMapper keyMapper;
	private SimpleVector moveRes = new SimpleVector(0, 0, 0);
	private SimpleVector ellipsoid = new SimpleVector(2, 2, 2);
	private Object3D cone;
	private static final float DAMPING = 0.1f;
	private static final float SPEED = 1f;
	private static final float MAXSPEED = 1f;
	public Object3D plane;
	private ArrayList<Ball> ballslist;

	public Player(World w, KeyMapper k, Object3D p, ArrayList<Ball> balls) {
		ballslist = balls;
		keyMapper = k;
		world = w;
		plane = p;
		cone = Primitives.getCone(2);
		cone.translate(-50, -100, -50);
		cone.setCollisionMode(Object3D.COLLISION_CHECK_SELF|Object3D.COLLISION_CHECK_OTHERS);
		cone.addCollisionListener(new collision());
		world.addObject(cone);
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

		// move the cube
		if (up) {
			SimpleVector t = cone.getZAxis();
			t.scalarMul(SPEED);
			moveRes.add(t);
		}

		if (down) {
			SimpleVector t = cone.getZAxis();
			t.scalarMul(-SPEED);
			moveRes.add(t);
		}

		if (left) {
			cone.rotateY((float) Math.toRadians(-1));
		}

		if (right) {
			cone.rotateY((float) Math.toRadians(1));
		}

		if (moveRes.length() > MAXSPEED) {
			moveRes.makeEqualLength(new SimpleVector(0, 0, MAXSPEED));
		}

		cone.translate(0, -0.02f, 0);

		moveRes = cone.checkForCollisionEllipsoid(moveRes, ellipsoid, 8);
		cone.translate(moveRes);
		SimpleVector t = new SimpleVector(0, 1, 0);
		t = cone.checkForCollisionEllipsoid(t, ellipsoid, 1);

		cone.translate(t);

		// damping
		if (moveRes.length() > DAMPING) {
			moveRes.makeEqualLength(new SimpleVector(0, 0, DAMPING));
		} else {
			moveRes = new SimpleVector(0, 0, 0);
		}
	}

	public Object3D getObject() {
		return new Object3D(cone);
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
