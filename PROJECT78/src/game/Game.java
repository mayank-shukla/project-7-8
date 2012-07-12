package game;

import java.awt.event.KeyEvent;
import display.CubeObject;
import display.Display;
import display.Led;

public class Game {
	Display update;
	int x = 4;
	int z = 4;
	int y = 4;

	public Game(Display display) {
		update = display;
		try {
			CubeObject temp = new CubeObject(update);
			//temp.addLed(new s_led(x, y, z, 255, 255));
			for (int i = 0;i < 2;i++) {
				for (int j = 0;j < 2;j++) {
					temp.addLed(new Led(x + i,y,z + j,255,255));
					temp.addLed(new Led(x + i,y + 1,z + j,255,255));
				}
			}
			update.addObject(temp);
			temp = new CubeObject(update);
			//temp.addLed(new s_led(x, y, z, 255, 255));
			temp.addLed(new Led(2,2,4,255,0));
			temp.addLed(new Led(2,5,5,255,0));
			temp.addLed(new Led(9,9,9,255,0));
			update.addObject(temp);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkGamingControls(int key) {
		int tx = 0;
		int ty = 0;
		int tz = 0;
		CubeObject[] object = update.getObjects();
		try {
			//update.setRed(0, x, y, z);
			//update.setGreen(0, x, y, z);
			switch(key) {
				case KeyEvent.VK_W:
					++ty;
					break;
				case KeyEvent.VK_S:
					--ty;
					break;
				case KeyEvent.VK_UP:
					++tz;
					break;
				case KeyEvent.VK_DOWN:
					--tz;
					break;
				case KeyEvent.VK_LEFT:
					++tx;
					break;
				case KeyEvent.VK_RIGHT:
					--tx;
					break;
			}
			try {
				object[0].moveObject(tx,ty,tz);
			}
			catch (Exception e) {}
			//update.setRed(255, x, y, z);
			//update.setGreen(255, x, y, z);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
