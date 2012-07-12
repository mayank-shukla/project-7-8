package display;
/**
 * deze classe wordt gebruikt om meerdere ledjes tegelijk aan te sturen
 * 
 * @author Jimmy
 * 
 */
public class CubeObject {
	private Led[] leds;
	private Display parent;

	public CubeObject(Display parent) {
		leds = new Led[0];
		this.parent = parent;
	}

	private CubeObject(Led[] leds) {
		this.leds = leds;
	}

	public void addLed(Led led) throws Exception {
		if (leds.length == 255)
			throw new Exception("het maximum aantal leds in dit object is bereikt");
		Led[] templeds = new Led[leds.length + 1];
		int i;
		for (i = 0;i < leds.length;i++) {
			templeds[i] = leds[i];
		}
		templeds[i] = led;
		i++;
		leds = templeds;
	}

	public Led[] getLeds() {
		return leds;
	}

	public void reset() {
		leds = new Led[0];
	}

	/**
	 * controleert of een nieuwe versie van dit object geen collision heeft met andere objecten in de cube
	 * 
	 * @param obj nieuwe versie van dit object
	 * @return true als er colision is anders false
	 */
	public boolean checkCollision(CubeObject obj) {
		CubeObject[] objarr = parent.getObjects();
		for (int i = 0;i < objarr.length;i++) {
			if (objarr[i] != this) {
				Led[] toCompare = objarr[i].getLeds();
				Led[] compare = obj.getLeds();
				for (int k = 0;k < toCompare.length;k++) {
					for (int l = 0;l < compare.length;l++) {
						if (toCompare[k].getX() == compare[l].getX() && toCompare[k].getZ() == compare[l].getZ() && toCompare[k].getY() == compare[l].getY())
							return true;
					}
				}
			}
		}
		return false;
	}

	public void moveObject(int x, int y, int z) throws Exception {
		Led led[] = new Led[leds.length];
		for (int i = 0;i < led.length;i++) {
			led[i] = new Led(leds[i]);
		}
		CubeObject temp = new CubeObject(led);
		for (int i = 0;i < led.length;i++) {
			led[i].setX((led[i].getX() + x));
			led[i].setY((led[i].getY() + y));
			led[i].setZ((led[i].getZ() + z));
		}
		if (!checkCollision(temp))
			this.leds = led;
	}
}