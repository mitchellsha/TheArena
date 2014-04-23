package environment;
import com.threed.jpct.*;

public class playspace {
	
	public playspace() throws InterruptedException{
		World world = new World();
		world.setAmbientLight(0, 255, 0);
		
//		TextureManager.getInstance().addTexture("box", new Texture("box.jpg"));
		Object3D walls = loadModel("src/Models/finalized/backwalls.3ds", 1);
		walls.build();
		world.addObject(walls);
//		Object3D wall1 = Primitives.getBox(5f,5f);
//		wall1.build();
//		world.addObject(wall1);
//		wall1.setTexture("box");
//		wall1.setEnvmapped(Object3D.ENVMAP_ENABLED);

		
//		Object3D wall2 = Primitives.getBox(5f, 5f);
//		box.setTexture("box");
//		box.setEnvmapped(Object3D.ENVMAP_ENABLED);

		world.getCamera().setPosition(50, -50, -5);
		world.getCamera().lookAt(walls.getTransformedCenter());

		FrameBuffer buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
		
		while (!org.lwjgl.opengl.Display.isCloseRequested()) {
//	        wall1.rotateY(0.01f);
	        buffer.clear(java.awt.Color.BLUE);
	        world.renderScene(buffer);
	        world.draw(buffer);
	        buffer.update();
	        buffer.displayGLOnly();
	        Thread.sleep(10);
		}
	}
	
    private Object3D loadModel(String filename, float scale) {
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
	
	public static void main(String arg[]) throws InterruptedException{
		playspace play = new playspace();
	}
}