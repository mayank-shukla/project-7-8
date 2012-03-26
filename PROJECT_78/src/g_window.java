import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

/* g_window.java
 *
 * Hier wordt het venster geïnitialiseerd dat voor heel het programma
 * gebruikt zal worden.
 * 
 */
public class g_window implements MouseListener
{
	JFrame frame = new JFrame("3D LED Cube Simulator/Editor " + s_version.getVersion());
	private g_jogl_cube jogl;

	public void createWindow()
	{
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 800;
		int height = 660;
		int x = (screen.width-width)/2;
		int y = (screen.height-height)/2-20;
		frame.setBounds(x,y,width,height);

		frame.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		});
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GLCapabilities capabilities = new GLCapabilities(null);
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		jogl = new g_jogl_cube(370, 370, capabilities);
		jogl.addGLEventListener(jogl);
		jogl.setFocusable(true);
		jogl.addMouseMotionListener(jogl);
		jogl.addMouseWheelListener(jogl);
		frame.addMouseListener(this);

		Container pane = frame.getContentPane();
		pane.setLayout(null);

		pane.add(jogl);

		Dimension size = jogl.getPreferredSize();
		jogl.setBounds(0, 0, size.width, size.height);
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("X:" + e.getX() + " Y:" + e.getY());
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}
}