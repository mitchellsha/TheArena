package environment;
import javax.swing.JFrame;

import com.threed.jpct.*;
import com.threed.jpct.util.KeyMapper;

import shapes.Character;

public class Playspace extends JFrame{
	
	public Playspace(){
		World world = new World();
		world.setAmbientLight(0, 100, 100);
		
//		TextureManager.getInstance().addTexture("box", new Texture("box.jpg"));
		
		Object3D rearWalls = new ArenaRearWalls();
		world.addObject(rearWalls);

		Object3D invisibleWalls = new ArenaInvisibleWalls();
		world.addObject(invisibleWalls);
		
		Object3D character = new Character();
		world.addObject(character);

//		world.getCamera().setPosition(20, -15, -15);
		world.getCamera().setPosition(22, -11, -17);
		world.getCamera().lookAt(rearWalls.getTransformedCenter());

		FrameBuffer buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
		
		while (!org.lwjgl.opengl.Display.isCloseRequested()) {
//			rearWalls.rotateY(0.01f);
	        buffer.clear(java.awt.Color.BLUE);
	        world.renderScene(buffer);
	        world.draw(buffer);
	        buffer.update();
	        buffer.displayGLOnly();
	        try
			{
				Thread.sleep(10);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String arg[]) throws InterruptedException{
		Playspace play = new Playspace();
	}
}
