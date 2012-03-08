import java.awt.Color;
import java.awt.Graphics;

/*
 * g_effects.java
 * 
 * Speciale effecten voor Graphics objecten en anderen
 * 
 */
public class g_effects 
{
	public static Color setRGB(Graphics gfx, int r, int g, int b)
	{
		Color RGB = new Color(r, g, b);
		gfx.setColor(RGB);
		return RGB;
	}
	
	public static void setRGB(Graphics gfx, String colour)
	{
		if (colour.equalsIgnoreCase("black")) {gfx.setColor(setRGB(gfx, 0, 0, 0)); return;}
		if (colour.equalsIgnoreCase("white")) {gfx.setColor(setRGB(gfx, 255, 255, 255)); return;}
		if (colour.equalsIgnoreCase("dark_gray")) {gfx.setColor(setRGB(gfx, 20, 20, 20)); return;}
		if (colour.equalsIgnoreCase("red")) {gfx.setColor(setRGB(gfx, 255, 0, 0)); return;}
		if (colour.equalsIgnoreCase("dark_red")) {gfx.setColor(setRGB(gfx, 100, 0, 0)); return;}
		if (colour.equalsIgnoreCase("green")) {gfx.setColor(setRGB(gfx, 0, 255, 0)); return;}
		if (colour.equalsIgnoreCase("dark_green")) {gfx.setColor(setRGB(gfx, 0, 100, 0)); return;}
		if (colour.equalsIgnoreCase("blue")) {gfx.setColor(setRGB(gfx, 0, 0, 255)); return;}
		if (colour.equalsIgnoreCase("dark_blue")) {gfx.setColor(setRGB(gfx, 0, 0, 100)); return;}
		
		if (colour.equalsIgnoreCase("disco")) 
		{
			int r1 = (int)(Math.random()*255);
			int r2 = (int)(Math.random()*255);
			int r3 = (int)(Math.random()*255);
			
			gfx.setColor(setRGB(gfx, r1, r2, r3)); 
			return;
		}
		
		System.out.println("Couldn't recognize the colour string!");
	}
}
