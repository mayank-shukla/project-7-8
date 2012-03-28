import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
//TODO max 10 fps op display
/* g_window.java
 *
 * Hier wordt het venster ge�nitialiseerd dat voor heel het programma
 * gebruikt zal worden.
 * 
 */
public class g_window implements MouseListener, KeyListener
{
	private JFrame frame = new JFrame("3D LED Cube Simulator and Editor " + s_version.getVersion());
	private g_jogl_cube jogl;
	private g_jogl_cube_layer layer;

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
		
		layer = new g_jogl_cube_layer(415, 370, capabilities);
		layer.addGLEventListener(layer);
		layer.setFocusable(true);
		layer.addMouseListener(layer);
		
		frame.addMouseListener(this);
		layer.addKeyListener(layer);

		Container pane = frame.getContentPane();
		pane.setLayout(null);

		pane.add(jogl);
		pane.add(layer);

		Dimension size = jogl.getPreferredSize();
		jogl.setBounds(0, 0, size.width, size.height);
		
		size = layer.getPreferredSize();
		layer.setBounds(380, 0, size.width, size.height);
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

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}