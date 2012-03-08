import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.gl2.GLUgl2;

import com.jogamp.opengl.util.FPSAnimator;

public class MyJoglCanvas extends GLCanvas implements GLEventListener {

	private static final long serialVersionUID = 1L;
	private FPSAnimator animator;
	private GLUgl2 glu;
	private int afstand = 100, aanzicht = 0, hoogte = 0;//aanzicht van 0 t/m 360

	public MyJoglCanvas(int width, int height, GLCapabilities capabilities) {
		super(capabilities);
		setSize(width, height);
	}

	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		setCamera(gl, glu, 0, 0, 0, afstand, aanzicht, hoogte);

		// Draw QUADS.
		gl.glColor3f(0.9f, 0.5f, 0.2f);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex3f(20, 20, -20);
		gl.glVertex3f(-20, 20, -20);
		gl.glVertex3f(-20, 20, 20);
		gl.glVertex3f(20, 20, 20);

		gl.glColor3f(0.2f, 0.5f, 0.2f);
		gl.glVertex3f(20, -20, 20);
		gl.glVertex3f(-20, -20, 20);
		gl.glVertex3f(-20, -20, -20);
		gl.glVertex3f(20, -20, -20);

		gl.glColor3f(0.9f, 0.9f, 0.2f);
		gl.glVertex3f(20, 20, 20);
		gl.glVertex3f(-20, 20, 20);
		gl.glVertex3f(-20, -20, 20);
		gl.glVertex3f(20, -20, 20);

		gl.glColor3f(0.9f, 0.5f, 0.8f);
		gl.glVertex3f(20, -20, -20);
		gl.glVertex3f(-20, -20, -20);
		gl.glVertex3f(-20, 20, -20);
		gl.glVertex3f(20, 20, -20);

		gl.glColor3f(1f, 0.5f, 0.2f);
		gl.glVertex3f(-20, 20, 20);
		gl.glVertex3f(-20, 20, -20);
		gl.glVertex3f(-20, -20, -20);
		gl.glVertex3f(-20, -20, 20);

		gl.glColor3f(0.9f, 0f, 0.2f);
		gl.glVertex3f(20, 20, -20);
		gl.glVertex3f(20, 20, 20);
		gl.glVertex3f(20, -20, 20);
		gl.glVertex3f(20, -20, -20);
		gl.glEnd();
	}

	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		drawable.setGL(new DebugGL2(gl));

		// Global settings.
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glClearColor(0f, 0f, 0f, 1f);

		// Start animator.
		animator = new FPSAnimator(this, 60);
		animator.start();

		glu = new GLUgl2();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}

	/**
	 * deze methode bepaaldt de waardes hoe naar een meegegeven punt X Y Z wordt gekeken, aan de hand van de afstand, aanzicht en hoogte. als niet aan de voorwaarde wordt voldaan wordt de camera op een standaard positie gezet
	 * 
	 * @param gl
	 * @param glu
	 * @param X X coordinaat waar naar gekeken wordt
	 * @param Y Y coordinaat waar naar gekeken wordt
	 * @param Z Z coordinaat waar naar gekeken wordt
	 * @param afstand de afstand tussen het oog en het punt waarnaar gekeken wordt, deze waarde moet tussen 1 en 1000 zijn
	 * @param aanzicht een waarde van 0 t/m 360 die de hoek waarnaar het object gekeken wordt bepaaldt
	 * @param hoogte een waarde van -89 t/m 89 die de hoogte van het oog bepaaldt 
	 */
	private int[] setCamera(GL2 gl, GLUgl2 glu, int X, int Y, int Z,int afstand, int aanzicht, int hoogte) {
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);

		// Calculate the point where the eye is
		float eyeX = 0, eyeY = 0, eyeZ = 30;
		eyeZ = (float) (Math.cos(Math.toRadians(aanzicht))*afstand);
		eyeX = (float) (Math.sin(Math.toRadians(aanzicht))*afstand);
		float h;
		if(hoogte<0) h = hoogte*-1;
		else h = hoogte;
		eyeX *= ((90-h)/90);
		eyeZ *= ((90-h)/90);
		eyeY = (float) Math.sqrt((afstand*afstand)-((eyeX*eyeX)+(eyeZ*eyeZ)));
		if(hoogte<0) eyeY*=-1;

		eyeX+=X;
		eyeY+=Y;
		eyeZ+=Z;

		if(afstand<1 || afstand>1000 || aanzicht<0 || aanzicht>360 || hoogte<-89 || hoogte>89) {
			eyeX = X;
			eyeY = Y;
			eyeZ = Z+30;
		}

		glu.gluLookAt(eyeX, eyeY, eyeZ, X, Y, Z, 0, 1, 0);

		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		int[] integer = new int[3];
		integer[0] = (int) eyeX;
		integer[1] = (int) eyeY;
		integer[2] = (int) eyeZ;
		return integer;
	}

	public void dispose(GLAutoDrawable drawable) {}

	public int getAfstand() {
		return afstand;
	}

	public void setAfstand(int afstand) {
		this.afstand = afstand;
	}

	public int getAanzicht() {
		return aanzicht;
	}

	public void setAanzicht(int aanzicht) {
		this.aanzicht = aanzicht;
	}

	public int getHoogte() {
		return hoogte;
	}

	public void setHoogte(int hoogte) {
		this.hoogte = hoogte;
	}
}