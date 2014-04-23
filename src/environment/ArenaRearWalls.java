package environment;

import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class ArenaRearWalls extends Object3D {
	
	public ArenaRearWalls(){
		super(loadModel("Models/finalized/backwallsB2.3ds", 1));		
		this.setAdditionalColor(0,100,100);
		this.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
	}
	
	public ArenaRearWalls(String path, int scale){
		super(loadModel(path, scale));		
		this.setAdditionalColor(0,100,100);
		this.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
	}
	
    private static Object3D loadModel(String filename, float scale) {
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
}
