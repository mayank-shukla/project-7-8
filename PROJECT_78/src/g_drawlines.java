import java.awt.Graphics;

import javax.swing.JPanel;

/* g_drawlines.java
 *
 * Teken de kubus met behulp van 2D lijnen.
 * 
 */
public class g_drawlines extends JPanel
{
	private static final long serialVersionUID = 1L;
	static boolean x_face = true;
	static boolean y_face = true;
	static boolean z_face = true;

	public void paintLines(Graphics g)
	{
		final int m = 16;
		
		g_effects.setRGB(g, "dark_gray");
		
		// Bovenste vlak
		g.drawLine(4*m,6*m,12*m,12*m);
		g.drawLine(22*m,8*m,12*m,12*m);
		g.drawLine(22*m,8*m,14*m,2*m);
		g.drawLine(4*m,6*m,14*m,2*m);

		// Onderste vlak
		g.drawLine(4*m,18*m,12*m,24*m);
		g.drawLine(22*m,20*m,12*m,24*m);
		g.drawLine(22*m,20*m,14*m,14*m);
		g.drawLine(4*m,18*m,14*m,14*m);

		// De 4 lijnen voor elke hoek
		g.drawLine(4*m,6*m,4*m,18*m);
		g.drawLine(22*m,8*m,22*m,20*m);
		g.drawLine(12*m,12*m,12*m,24*m);
		g.drawLine(14*m,2*m,14*m,14*m);
		
		// Verdeel nu heel de kubus in sectoren (4x4 voor nu)	
		for(int i = 0; i < 17; i++)
		{	
			int j = 10*i;
			int k = 4*i;
			
			int n = 8*i;
			int o = 6*i;
			
			int p = 12*i;
			
			// Bovenste vlak
			if (y_face)
			{
				g_effects.setRGB(g, "dark_red");
				g.drawLine(64+j,96-k,192+j,192-k);
				g.drawLine(352-n,128-o,192-n,192-o);
			}
			if (x_face)
			{
				g_effects.setRGB(g, "dark_green");
				g.drawLine(64+n,96+o,64+n,288+o);
				g.drawLine(64,288-p,192,384-p);
			}
			if (z_face)
			{
				g_effects.setRGB(g, "dark_blue");
				g.drawLine(352,320-p,192,384-p);
				g.drawLine(192+j,192-k,192+j,384-k);
			}
		}
	}
}
