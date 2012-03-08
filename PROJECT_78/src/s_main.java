/* s_main.java
 * 
 * Bij de .java bestanden wordt er een voorvoegsel gebruikt dat aangeeft bij welke
 * category het hoort.
 * 
 * s_ -> systeem gerelateerd
 * g_ -> grafisch
 * 
 */

public class s_main extends Thread
{
	public static void main(String args[]) throws Throwable
	{
		new s_main().start();
	}
	
	public void run()
	{
		g_window window = new g_window();
		window.createWindow();
		
		try 
		{
			while(true)
			{
				window.updateFrame();
				Thread.sleep(100);
			}
		} 
		catch (Throwable t){}
	}
}
