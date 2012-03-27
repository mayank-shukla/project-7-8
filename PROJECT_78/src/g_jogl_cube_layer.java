import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.gl2.GLUgl2;

import com.jogamp.opengl.util.FPSAnimator;

public class g_jogl_cube_layer extends GLCanvas implements GLEventListener, MouseListener{

	private static final long serialVersionUID = 1L;
	private FPSAnimator animator;
	private GLUgl2 glu;
	private int layer,x,y;

	public g_jogl_cube_layer(int width, int height, GLCapabilities capabilities) {
		super(capabilities);
		setSize(width, height);
		layer = 0;
		x=0;
		y=0;
	}

	public void display(GLAutoDrawable drawable) 
	{
		int value1 = 18;
		int value2 = 16;
		
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		setCamera(gl, glu, 450);
		
		// Write triangle.
		gl.glBegin(GL2.GL_QUADS);

		for(int x=0;x<16;x++) 
		{
			for(int y=0;y<16;y++) 
			{
				if (g_jogl_cube.cube_bool[x][0][15-y])
					gl.glColor3f(1f, 1f, 0f);
				gl.glVertex3f(0+value1*x, value2+value1*y-32, 0);
				gl.glVertex3f(value2+value1*x, value2+value1*y-32, 0);
				gl.glVertex3f(value2+value1*x, 0+value1*y-32, 0);
				gl.glVertex3f(0+value1*x, 0+value1*y-32, 0);
				gl.glColor3f(0.9f, 0.9f, 0.9f);
				
				if(x==15) 
				{
					gl.glVertex3f(0+value1*x+60, value2+value1*y-32, 0);
					gl.glVertex3f(value2+value1*x+60, value2+value1*y-32, 0);
					gl.glVertex3f(value2+value1*x+60, 0+value1*y-32, 0);
					gl.glVertex3f(0+value1*x+60, 0+value1*y-32, 0);
					gl.glColor3f(0.9f, 0.9f, 0.9f);
				}
			}
		}
		gl.glEnd();
	}

	private void setCamera(GL2 gl, GLUgl2 glu2, float distance) 
	{
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);
		glu.gluLookAt(167, 117, distance, 167, 117, 0, 0, 1, 0);

		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void dispose(GLAutoDrawable drawable) {}

	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		drawable.setGL(new DebugGL2(gl));

		// Global settings.
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);

		// Start animator (which should be a field).
		animator = new FPSAnimator(this, 60);
		animator.start();

		glu = new GLUgl2();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
	}

	public void mouseClicked(MouseEvent e)
	{	
		double x = e.getX();
		double z = e.getY();
		
		x -= 42;
		z -= 52;
		
		x /= 17.5;
		z /= 17.5;
		
		if (x > 16 || z > 16)
			return;
		// Geen idee, je weet maar nooit met jabajaba
		if (x < 0 || z < 0)
			return;
		
		if (!g_jogl_cube.cube_bool[(int)x][0][(int)z])
			g_jogl_cube.cube_bool[(int)x][0][(int)z] = true;
		else
			g_jogl_cube.cube_bool[(int)x][0][(int)z] = false;
	}

	public int getLayer() {
		return layer;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}