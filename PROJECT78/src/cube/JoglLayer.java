package cube;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.FPSAnimator;
import display.Display;

public class JoglLayer extends GLCanvas implements GLEventListener,MouseListener,MouseMotionListener,KeyListener {
	private static final long serialVersionUID = 1L;
	private boolean enable;
	private int mode;
	private int layer;
	private int x;
	private int y;
	private JoglCube jogl;
	private int[][] cpyred;
	private int[][] cpygreen;
	private FPSAnimator animator;
	private GLUgl2 glu;

	public JoglLayer(int width,int height,GLCapabilities capabilities,JoglCube jogl) {
		super(capabilities);
		enable = true;
		mode = 2;
		setSize(width,height);
		layer = 0;
		x = 0;
		y = 0;
		this.jogl = jogl;
		cpyred = new int[16][16];
		cpygreen = new int[16][16];
		for (int x = 0;x < 16;x++) {
			for (int z = 0;z < 16;z++) {
				cpyred[x][z] = 0;
				cpygreen[x][z] = 0;
			}
		}
	}

	/**
	 * bepaalt aan de hand van de positie van de muis welke vakje in de cube aan moet
	 * 
	 * @param e
	 */
	public void checkMouseAction(MouseEvent e) {
		if (!enable)
			return;
		double x = e.getX();
		double z = e.getY();
		// Only allow right/left mouse button (to prevent OpenGL crashes)
		if ((e.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK)
			return;
		// Fix for OpenGL crash
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK && (x > 325 || z > 332))
			return;
		if (x >= 369 && z >= 49 && x <= 384 && z <= 332) {
			z -= 49;
			z /= 17.5;
			layer = 15 - (int)z;
			// Cap the layer, it caused crashes!
			if (layer < 0)
				layer = 0;
			if (layer > 15)
				layer = 15;
			return;
		}
		x -= 42;
		z -= 52;
		x /= 17.5;
		z /= 17.5;
		if (x > 16 || z > 16 || x < 0 || z < 0)
			return;
		Display display = jogl.getDisplay();
		//met mode == 0 rood, mode == 1 groen, mode == 2 geel
		try {
			if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
				display.setGreen(0,(int)x,layer,(int)z);
				display.setRed(0,(int)x,layer,(int)z);
			}
			else if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
				if (mode == 0) {
					display.setRed(255,(int)x,layer,(int)z);
					display.setGreen(0,(int)x,layer,(int)z);
				}
				else if (mode == 1) {
					display.setRed(0,(int)x,layer,(int)z);
					display.setGreen(255,(int)x,layer,(int)z);
				}
				else if (mode == 2) {
					display.setRed(255,(int)x,layer,(int)z);
					display.setGreen(255,(int)x,layer,(int)z);
				}
			}
		}
		catch (Exception e1) {}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		checkMouseAction(e);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		try {
			int value1 = 18;
			int value2 = 16;
			GL2 gl = drawable.getGL().getGL2();
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			setCamera(gl,glu,450);
			gl.glBegin(GL2.GL_QUADS);
			Display display = jogl.getDisplay();
			for (int x = 0;x < 16;x++) {
				for (int y = 0;y < 16;y++) {
					gl.glColor3f(display.getLedsRed()[x][layer][15 - y] / 255f,display.getLedsGreen()[x][layer][15 - y] / 255f,0f);
					if (display.getLedsRed()[x][layer][15 - y] / 255f == 0 && display.getLedsGreen()[x][layer][15 - y] / 255f == 0) {
						gl.glColor3f(0.9f,0.9f,0.9f);
					}
					gl.glVertex3f(0 + value1 * x,value2 + value1 * y - 32,0);
					gl.glVertex3f(value2 + value1 * x,value2 + value1 * y - 32,0);
					gl.glVertex3f(value2 + value1 * x,0 + value1 * y - 32,0);
					gl.glVertex3f(0 + value1 * x,0 + value1 * y - 32,0);
					gl.glColor3f(0.9f,0.9f,0.9f);
					if (x == 15) {
						if (y == layer) {
							gl.glColor3f(0f,200 / 255f,200 / 255f);
						}
						gl.glVertex3f(0 + value1 * x + 60,value2 + value1 * y - 32,0);
						gl.glVertex3f(value2 + value1 * x + 60,value2 + value1 * y - 32,0);
						gl.glVertex3f(value2 + value1 * x + 60,0 + value1 * y - 32,0);
						gl.glVertex3f(0 + value1 * x + 60,0 + value1 * y - 32,0);
						gl.glColor3f(0.9f,0.9f,0.9f);
					}
				}
			}
			gl.glEnd();
		}
		catch (ArrayIndexOutOfBoundsException e) {};
	}

	/**
	 * bepaald het camera punt aan de hand van distance
	 * 
	 * @param gl
	 * @param glu2
	 * @param distance
	 */
	private void setCamera(GL2 gl, GLUgl2 glu2, float distance) {
		// Change to projection matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		// Perspective.
		float widthHeightRatio = (float)getWidth() / (float)getHeight();
		glu.gluPerspective(45,widthHeightRatio,1,1000);
		glu.gluLookAt(167,117,distance,167,117,0,0,1,0);
		// Change back to model view matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		drawable.setGL(new DebugGL2(gl));
		// Global settings.
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT,GL.GL_NICEST);
		gl.glClearColor(0.5f,0.5f,0.5f,1f);
		// Start animator (which should be a field).
		try {
			animator = new FPSAnimator(this,60);
			animator.start();
		}
		catch (GLException e) {}
		glu = new GLUgl2();
	}

	public void stopAnimator() {
		animator.stop();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0,0,width,height);
	}

	public int getLayer() {
		return layer;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		Display display = jogl.getDisplay();
		if (jogl.getGamemode())
			return;
		switch(key) {
			case KeyEvent.VK_UP:
				layer++;
				if (layer > 15)
					layer = 0;
				break;
			case KeyEvent.VK_DOWN:
				layer--;
				if (layer < 0)
					layer = 15;
				break;
			case KeyEvent.VK_BACK_SPACE:
				for (int x = 0;x < 16;x++) {
					for (int z = 0;z < 16;z++) {
						try {
							display.setGreen(0,x,layer,z);
							display.setRed(0,x,layer,z);
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				break;
			case KeyEvent.VK_DELETE:
				for (int x = 0;x < 16;x++) {
					for (int y = 0;y < 16;y++) {
						for (int z = 0;z < 16;z++) {
							try {
								display.setGreen(0,x,y,z);
								display.setRed(0,x,y,z);
							}
							catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				break;
			case KeyEvent.VK_C:
				for (int x = 0;x < 16;x++) {
					for (int z = 0;z < 16;z++) {
						cpyred[x][z] = display.getLedsRed()[x][layer][z];
						cpygreen[x][z] = display.getLedsGreen()[x][layer][z];
					}
				}
				break;
			case KeyEvent.VK_V:
				for (int x = 0;x < 16;x++) {
					for (int z = 0;z < 16;z++) {
						try {
							display.setGreen(cpygreen[x][z],x,layer,z);
							display.setRed(cpygreen[x][z],x,layer,z);
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		checkMouseAction(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void dispose(GLAutoDrawable arg0) {}
}