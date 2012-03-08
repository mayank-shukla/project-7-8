import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/* g_window.java
 *
 * Hier wordt het venster geïnitialiseerd dat voor heel het programma
 * gebruikt zal worden.
 * 
 */
public class g_window
{
	JFrame frame = new JFrame("3D LED Cube Simulator/Editor " + s_version.getVersion());
	
	public void createWindow()
	{		
		frame.setLocation(200, 50);
		frame.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		});
        frame.setSize(new Dimension(720, 600));
        frame.setResizable(false);
        frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new g_interface());
	}

	public void updateFrame() 
	{
		System.out.println("LAL");
		frame.repaint();
	}
}
