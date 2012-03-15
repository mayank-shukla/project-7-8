import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.gl2.GLUgl2;

import com.jogamp.opengl.util.FPSAnimator;

public class g_jogl_cube extends GLCanvas implements GLEventListener {

	private static final long serialVersionUID = 1L;
	private FPSAnimator animator;
	private GLUgl2 glu;
	private int afstand, aanzicht, hoogte;//aanzicht van 0 t/m 360
	boolean[][][] cube_bool;
	boolean[][][] cube_bool_draw;
	int[][][] cube_red;
	int[][][] cube_green;
	
	
	public g_jogl_cube(int width, int height, GLCapabilities capabilities) {
		super(capabilities);
		setSize(width, height);
		afstand = 125;
		aanzicht = 90;
		
		/*	TODO
		 * bij aanzicht 0 de hoogste z waarde tekenen
		 * bij aanzicht 90 de hoogste x waarde tekenen
		 * bij aanzicht 180 de laagst z waarde tekenen
		 * bij aanzicht 270 de laagst x waarde tekenen
		*/
		
		hoogte = 0;
		cube_red = new int[16][16][16];
		cube_green = new int[16][16][16];
		cube_bool = new boolean[16][16][16];
		cube_bool_draw = new boolean[16][16][16];
		
		for(int x=0;x<16;x++) {
			for(int y=0;y<16;y++) {
				for(int z=0;z<16;z++) {
					cube_red[x][y][z]=255;
					cube_green[x][y][z]=255;
					cube_bool[x][y][z] = true;
					cube_bool_draw[x][y][z] = true;
				}
			}
		}
	}

	/**
	 * teken 16*16*16 cubes aan de hand van fields, het heeft een rood groen waarde
	 */
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		final int cube_size=3;

		setCamera(gl, glu, cube_size*8, cube_size*8, cube_size*8, afstand, aanzicht, hoogte);

		renderFilter();

		gl.glBegin(GL2.GL_QUADS);
		for(int x=0;x<16;x++) {
			for(int y=0;y<16;y++) {
				for(int z=0;z<16;z++) {
					if(cube_bool_draw[x][y][z]) {
						//TODO aanpassen zodat alleen het gedeelte van de cube die je ziet wordt gerenderd
						gl.glColor3f(cube_red[x][y][z]/255f, cube_green[x][y][z]/255f, 0f);

						gl.glVertex3f(cube_size+cube_size*x, cube_size+cube_size*y, 0);
						gl.glVertex3f(0, cube_size+cube_size*y, 0);
						gl.glVertex3f(0, cube_size+cube_size*y, cube_size+cube_size*z);
						gl.glVertex3f(cube_size+cube_size*x, cube_size+cube_size*y, cube_size+cube_size*z);

						gl.glVertex3f(cube_size+cube_size*x, 0, cube_size+cube_size*z);
						gl.glVertex3f(0, 0, cube_size+cube_size*z);
						gl.glVertex3f(0, 0, 0);
						gl.glVertex3f(cube_size+cube_size*x, 0, 0);

						gl.glVertex3f(cube_size+cube_size*x, cube_size+cube_size*y, cube_size+cube_size*z);
						gl.glVertex3f(0, cube_size+cube_size*y, cube_size+cube_size*z);
						gl.glVertex3f(0, 0, cube_size+cube_size*z);
						gl.glVertex3f(cube_size+cube_size*x, 0, cube_size+cube_size*z);

						gl.glVertex3f(cube_size+cube_size*x, 0, 0);
						gl.glVertex3f(0, 0, 0);
						gl.glVertex3f(0, cube_size+cube_size*y, 0);
						gl.glVertex3f(cube_size+cube_size*x, cube_size+cube_size*y, 0);

						gl.glVertex3f(0, cube_size+cube_size*y, cube_size+cube_size*z);
						gl.glVertex3f(0, cube_size+cube_size*y, 0);
						gl.glVertex3f(0, 0, 0);
						gl.glVertex3f(0, 0, cube_size+cube_size*z);

						gl.glVertex3f(cube_size+cube_size*x, cube_size+cube_size*y, 0);
						gl.glVertex3f(cube_size+cube_size*x, cube_size+cube_size*y, cube_size+cube_size*z);
						gl.glVertex3f(cube_size+cube_size*x, 0, cube_size+cube_size*z);
						gl.glVertex3f(cube_size+cube_size*x, 0, 0);
					}
				}
			}
		}
		gl.glEnd();//*/
	}

	private void renderFilter() {
		for(int x=0;x<16;x++) {
			for(int y=0;y<16;y++) {
				for(int z=0;z<16;z++) {
					cube_bool_draw[x][y][z] = cube_bool[x][y][z];
				}
			}
		}

		if(aanzicht==0) {
			if(hoogte==0) {
				for(int x=0;x<16;x++) {
					for(int y=0;y<16;y++) {
						boolean teken=true;
						for(int z=15;z>=0;z--) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
			else if(hoogte>0) {
				for(int x=0;x<16;x++) {
					for(int y=1;y<16;y++) {
						boolean teken=true;
						for(int z=15;z>=0;z--) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
			else {
				for(int x=0;x<16;x++) {
					for(int y=0;y<15;y++) {
						boolean teken=true;
						for(int z=15;z>=0;z--) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
		}

		else if(aanzicht == 180) {
			if(hoogte==0) {
				for(int x=0;x<16;x++) {
					for(int y=0;y<16;y++) {
						boolean teken=true;
						for(int z=0;z<16;z++) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
			else if(hoogte>0) {
				for(int x=0;x<16;x++) {
					for(int y=1;y<16;y++) {
						boolean teken=true;
						for(int z=0;z<16;z++) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
			else {
				for(int x=0;x<16;x++) {
					for(int y=0;y<15;y++) {
						boolean teken=true;
						for(int z=0;z<16;z++) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
		}

		else if(aanzicht==90) {
			if(hoogte==0) {
				for(int y=0;y<16;y++) {
					for(int z=0;z<16;z++) {
						boolean teken=true;
						for(int x=15;x>=0;x--) {
							if(!teken) {
								cube_bool_draw[x]
										[y]
												[z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
			else if(hoogte>0) {
				for(int y=1;y<16;y++) {
					for(int z=0;z<16;z++) {
						boolean teken=true;
						for(int x=15;x>=0;x--) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
			else {
				for(int y=0;y<15;y++) {
					for(int z=0;z<16;z++) {
						boolean teken=true;
						for(int x=15;x>=0;x--) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
		}

		else if(aanzicht==270) {
			if(hoogte==0) {
				for(int y=0;y<16;y++) {
					for(int z=0;z<16;z++) {
						boolean teken=true;
						for(int x=0;x<16;x++) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
			else if(hoogte>0) {
				for(int y=1;y<16;y++) {
					for(int z=0;z<16;z++) {
						boolean teken=true;
						for(int x=0;x<16;x++) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
			else {
				for(int y=0;y<15;y++) {
					for(int z=0;z<16;z++) {
						boolean teken=true;
						for(int x=0;x<16;x++) {
							if(!teken) {
								cube_bool_draw[x][y][z]=false;
							}
							else if(cube_bool_draw[x][y][z]==true) {
								teken=false;
							}
						}
					}
				}
			}
		}

		else if(aanzicht>=1 || aanzicht <=89) {
			//TODO hier verder methode maken die bepaalt welke cubes moeten worden gerenderd
		}
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

	public void setAanzicht(int aanzicht) throws Exception {
		if(aanzicht<0 || aanzicht > 359)
			throw new Exception("this value should be between 0 & 359");
		this.aanzicht = aanzicht;
	}

	public int getHoogte() {
		return hoogte;
	}

	public void setHoogte(int hoogte) throws Exception {
		if(hoogte<-89 || hoogte>89)
			throw new Exception("this value should be between -89 & 89");
		this.hoogte = hoogte;
	}

	public boolean getCube_bool(int x, int y, int z) {
		return cube_bool[x][y][z];
	}


	public void setCube_bool(int x, int y, int z, boolean cube_bool) {
		this.cube_bool[x][y][z] = cube_bool;
	}


	public int getCube_red(int x, int y, int z) {
		return cube_red[x][y][z];
	}


	public void setCube_red(int x, int y, int z, int cube_red) {
		this.cube_red[x][y][z] = cube_red;
	}


	public int getCube_green(int x, int y, int z) {
		return cube_green[x][y][z];
	}


	public void setCube_green(int x, int y, int z, int cube_green) {
		this.cube_green[x][y][z] = cube_green;
	}
}