import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Engine extends JFrame implements MouseMotionListener, MouseWheelListener{

	private static final long serialVersionUID = 1L;

	private static MyJoglCanvas jogl;

	private int width = 500;
	private int height = 500;

	public Engine() {
		GLCapabilities capabilities = new GLCapabilities(null);
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		jogl = new MyJoglCanvas(width, height, capabilities);
		jogl.addGLEventListener(jogl);
		addMouseMotionListener(this);
		jogl.addMouseMotionListener(this);
		addMouseWheelListener(this);
		jogl.addMouseWheelListener(this);
		setFocusable(true);
		//add(jogl);

		setVisible(true);
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}

	public static void main(String[] args) {
		new Engine();
	}

	public void mouseDragged(MouseEvent e) {
		System.exit(0);
	}

	public void mouseMoved(MouseEvent e) {
		int aanzicht = jogl.getAanzicht();
		int hoogte = jogl.getHoogte();

		if(e.getX()<500) aanzicht++;
		else if(e.getX()>500) aanzicht--;

		if(e.getY()<500) hoogte++;
		else if(e.getY()>500) hoogte--;

		if(aanzicht<0) aanzicht = 360;
		else if(aanzicht>360) aanzicht = 0;

		if(hoogte>89) hoogte = 89;
		else if(hoogte<-89) hoogte = -89;

		jogl.setAanzicht(aanzicht);
		jogl.setHoogte(hoogte);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int afstand = jogl.getAfstand();
		e.getWheelRotation();
		if(e.getWheelRotation()>0) afstand++;
		else if(e.getWheelRotation()<0) afstand--;
		jogl.setAfstand(afstand);
	}
}