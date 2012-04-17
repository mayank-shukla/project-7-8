import com.atmel.atusbhidjni.AtUsbHidJni;

/* s_main.java
 * 
 * Bij de .java bestanden wordt er een voorvoegsel gebruikt dat aangeeft bij welke
 * category het hoort.
 * 
 * s_ -> systeem gerelateerd
 * g_ -> grafisch
 * 
 * waneer je naar een jar file exporteerd moet library handeling op package required libraries into generated jar, en alle files die in de JOGL map in dit project moeten met een archive manager(winrar/7zip etc) in de jar worden geplaatst
 */

public class s_main extends Thread
{
	private static AtUsbHidJni usb;

	public static void main(String args[]) throws Throwable
	{
		new s_main().start();
		new s_thread().start();
		
		usb = new AtUsbHidJni();
		System.loadLibrary("AtUsbHid");
		usb.loadLibraryUsbHid();
		if(usb.findHidDevice(0x03EB, 0x2013)!=1)
			return;

		System.out.println(usb.getOutputReportLength());

		byte[] b = new byte[usb.getOutputReportLength()];
		for (int i = 0; i < b.length; ++i) {
			b[i] = 0;
		}

		b[1] = (byte) 51;

		b[0] = 49;

		double start = System.nanoTime();;
		for(int i=0;i<1000/usb.getOutputReportLength();i++)
			usb.writeData(b);
		double stop = System.nanoTime();
		System.out.println((stop-start)/1000000000.0);

		Thread.sleep(1000);

		b[0] = 48;

		usb.writeData(b);

		usb.UnloadloadLibraryUsbHid();
	}

	public void run()
	{
		g_window window = new g_window();
		window.createWindow();
	}
}