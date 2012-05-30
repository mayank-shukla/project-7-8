/* s_main.java
 * 
 * Bij de .java bestanden wordt er een voorvoegsel gebruikt dat aangeeft bij welke
 * category het hoort.
 * 
 * s_ -> systeem gerelateerd
 * g_ -> grafisch
 * e_ -> entertainment (games)
 * 
 * waneer je naar een jar file exporteerd moet library handeling op package required libraries into generated jar, en alle files die in de JOGL map in dit project moeten met een archive manager(winrar/7zip etc) in de jar worden geplaatst
 */

public class s_main extends Thread
{
	public static void main(String args[]) throws Throwable
	{
		new s_main().start();
		new s_thread().start();
	}

	public void run()
	{
		new g_window();
	}
}