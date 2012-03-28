import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

public class g_jogl_cube_layer extends GLCanvas implements GLEventListener, MouseListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	private FPSAnimator animator;
	private GLUgl2 glu;
	private int layer,x,y,copied_y;

	public g_jogl_cube_layer(int width, int height, GLCapabilities capabilities) 
	{
		super(capabilities);
		setSize(width, height);
		layer = 0;
		x=0;
		y=0;
		copied_y=0;
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
				if (x==15) 
				{
					gl.glColor3f(0.6f, 0.9f, 0.9f);
					if (g_jogl_cube.y_bool[y])
						gl.glColor3f(0.9f, 0.6f, 1f);
					gl.glVertex3f(0+value1*x+60, value2+value1*y-32, 0);
					gl.glVertex3f(value2+value1*x+60, value2+value1*y-32, 0);
					gl.glVertex3f(value2+value1*x+60, 0+value1*y-32, 0);
					gl.glVertex3f(0+value1*x+60, 0+value1*y-32, 0);
					gl.glColor3f(0.9f, 0.9f, 0.9f);
				}
				
				if (g_jogl_cube.cube_bool[x][g_jogl_cube.y_axis][15-y])
					gl.glColor3f(1f, 1f, 0f);
				
				gl.glVertex3f(0+value1*x, value2+value1*y-32, 0);
				gl.glVertex3f(value2+value1*x, value2+value1*y-32, 0);
				gl.glVertex3f(value2+value1*x, 0+value1*y-32, 0);
				gl.glVertex3f(0+value1*x, 0+value1*y-32, 0);
				gl.glColor3f(0.9f, 0.9f, 0.9f);
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
		
		if (x >= 369 && z >= 49 && x <= 384 && z <= 332)
		{
			x -= 369;
			z -= 49;
			
			z /= 17.5;
			g_jogl_cube.y_axis = (int)(16-z);
			
			if (!g_jogl_cube.y_bool[g_jogl_cube.y_axis])
			{
				for (int i = 0; i < 16; i++)
				{
					g_jogl_cube.y_bool[i] = false;
				}
				g_jogl_cube.y_bool[g_jogl_cube.y_axis] = true;
			}
			else
				g_jogl_cube.y_bool[g_jogl_cube.y_axis] = false;
			return;
		}
		
		x -= 42;
		z -= 52;
		
		x /= 17.5;
		z /= 17.5;
		
		if (x >= 16 || z >= 16 || g_jogl_cube.y_axis >= 16)
			return;
		// Geen idee, je weet maar nooit met jabajaba
		if (x < 0 || z < 0 || g_jogl_cube.y_axis < 0)
			return;
		
		if (!g_jogl_cube.cube_bool[(int)x][g_jogl_cube.y_axis][(int)z])
			g_jogl_cube.cube_bool[(int)x][g_jogl_cube.y_axis][(int)z] = true;
		else
			g_jogl_cube.cube_bool[(int)x][g_jogl_cube.y_axis][(int)z] = false;
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
	
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		int y = g_jogl_cube.y_axis;
		
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
		{
			if (key == KeyEvent.VK_UP)
				y++;
			else if (key == KeyEvent.VK_DOWN)
				y--;
			
			if (y < 0)
				y = 15;
			if (y > 15)
				y = 0;
			
			for (int i = 0; i < 16; i++)
			{
				g_jogl_cube.y_bool[i] = false;
			}
			
			g_jogl_cube.y_axis = y;
			g_jogl_cube.y_bool[y] = true;
			return;
		}
		
		if (key == KeyEvent.VK_BACK_SPACE)
		{
			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					g_jogl_cube.cube_bool[x][y][z] = false;
				}
			}
			return;
		}
		
		if (key == KeyEvent.VK_DELETE)
		{
			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					for (int i = 0; i < 16; i++)
					{
						g_jogl_cube.cube_bool[x][i][z] = false;
					}
				}
			}
		}
		
		if (key == KeyEvent.VK_C)
		{
			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					g_jogl_cube.cube_copy[x][y][z] = g_jogl_cube.cube_bool[x][y][z];
					copied_y = y;
				}
			}
		}
		
		if (key == KeyEvent.VK_V)
		{
			for (int x = 0; x < 16; x++)
			{
				for (int z = 0; z < 16; z++)
				{
					g_jogl_cube.cube_bool[x][y][z] = g_jogl_cube.cube_copy[x][copied_y][z];
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
}