import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class g_interface extends g_drawlines implements MouseMotionListener, ActionListener
{	
	private static final long serialVersionUID = 1L;
	protected JButton enableFace_X, enableFace_Y, enableFace_Z;
	int mouse_x, mouse_y, choice;
	
	public g_interface()
	{	
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g)
	{	
		checkMouseBoundaries();
		
		// Achtergronden
		g_effects.setRGB(g, "white");
		g.fillRect(0,0,720,600);
		g_effects.setRGB(g, "black");
		g.fillRect(0,0,420,420);
		g_effects.setRGB(g, 150, 150, 150);
		g.fillRect(440,10,255,420);
		g_effects.setRGB(g, 222, 222, 222);
		g.fillRect(444,25,247,90);
		g_effects.setRGB(g, 100, 100, 100);
		g.fillRect(0,420,720,200);
		
		// GUI
		g.setFont(new Font("Verdana", Font.BOLD, 10));
		g_effects.setRGB(g, "white");
		g.fillRect(0,420,720,20);
		g.fillRect(420,0,360,20);
		g.drawString("3D cube view", 5, 15);
		g.drawString("frame", 380, 400);
		g.drawString("1 / 10", 380, 410);
		g.drawString("x: " + mouse_x, 5, 400);
		g.drawString("y: " + mouse_y, 5, 410);
		
		g_effects.setRGB(g, "black");
		g.drawString("Cube manipulation", 445, 15);
		g.drawString("Animation control", 5, 435);
		
		g.drawString("Show x-axis gridlines", 540, 43);
		g.drawString("Show y-axis gridlines", 540, 73);
		g.drawString("Show z-axis gridlines", 540, 103);
		addGridControlButtons(g);
		paintLines(g);
	}
	
	private void addGridControlButtons(Graphics g) 
	{	
		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		enableFace_X = new JButton("Toggle");
		enableFace_Y = new JButton("Toggle");
		enableFace_Z = new JButton("Toggle");
		
		enableFace_X.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
			checkToggle(0);}});  
		enableFace_Y.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
			checkToggle(1);}});  
		enableFace_Z.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
			checkToggle(2);}});  
        
		add(enableFace_X);
		add(enableFace_Y);
		add(enableFace_Z);
		add(buttonPanel, BorderLayout.LINE_START);
		setBorder(BorderFactory.createEmptyBorder(21,548,600,300));
	}

	private void checkMouseBoundaries() 
	{		
		if (mouse_x >= 420 || mouse_y >= 420)
		{
			mouse_x = 420;
			mouse_y = 420;
		}
	}
	
	private void checkToggle(int value)
	{
		if (value == 0)
		{
			if (!g_drawlines.x_face){g_drawlines.x_face = true; return;}
			g_drawlines.x_face = false;
		}
		if (value == 1)
		{
			if (!g_drawlines.y_face){g_drawlines.y_face = true; return;}
			g_drawlines.y_face = false;
		}
		if (value == 2)
		{
			if (!g_drawlines.z_face){g_drawlines.z_face = true; return;}
			g_drawlines.z_face = false;
		}
	}

	public void mouseDragged(MouseEvent e){}

	public void mouseMoved(MouseEvent e) 
	{
		mouse_x = e.getX();
		mouse_y = e.getY();
	}

	public void actionPerformed(ActionEvent e){}
}
