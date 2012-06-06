/**
 * deze classe wordt gebruikt om meerdere ledjes tegelijk aan te sturen
 * @author Jimmy
 * 
 */
public class s_object 
{
	private s_led[] leds;
	private s_display parent;

	public s_object(s_display parent) 
	{
		leds = new s_led[0];
		this.parent = parent;
	}
	
	private s_object(s_led[] leds)
	{
		this.leds = leds;
	}

	public void addLed(s_led led) throws Exception 
	{
		if(leds.length==255)
			throw new Exception("het maximum aantal leds in dit object is bereikt");
		s_led[] templeds = new s_led[leds.length+1];
		int i;
		for(i=0;i<leds.length;i++) 
		{
			templeds[i] = leds[i];
		}
		templeds[i] = led;
		i++;
		leds = templeds;
	}

	public s_led[] getLeds() 
	{
		return leds;
	}

	public void reset() 
	{
		leds = new s_led[0];
	}
	
	/**
	 * controleert of een nieuwe versie van dit object geen collision heeft met andere objecten in de cube
	 * @param obj nieuwe versie van dit object
	 * @return true als er colision is anders false
	 */
	public boolean checkCollision(s_object obj) 
	{
		s_object[] objarr = parent.getObjects();
		
		for(int i=0;i<objarr.length;i++) 
		{
			if(objarr[i] != this) 
			{
				s_led[] toCompare = objarr[i].getLeds();
				s_led[] compare = obj.getLeds();
				
				for(int k=0;k<toCompare.length;k++) 
				{
					for(int l=0;l<compare.length;l++) 
					{
						if(toCompare[k].getX()==compare[l].getX() && toCompare[k].getZ()==compare[l].getZ() && toCompare[k].getY()==compare[l].getY())
							return true;
					}
				}
			}
		}
		return false;
	}
	
	public void moveObject(int x, int y, int z) throws Exception
	{
		s_led led[] = new s_led[leds.length];
		
		for(int i=0;i<led.length;i++) {
			led[i] = new s_led(leds[i]);
		}
		
		s_object temp = new s_object(led);

		for (int i = 0; i < led.length; i++) {
			led[i].setX((led[i].getX()+x));
			led[i].setY((led[i].getY()+y));
			led[i].setZ((led[i].getZ()+z));
		}
		if (!checkCollision(temp))
			this.leds = led;
	}
}