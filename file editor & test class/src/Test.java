import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
	public static void main(String[] args) throws NumberFormatException, IOException {
		locChange(51.96050295,4.4927579);
	}

	public static void locChange(double lo, double la) {
		System.out.println("locatie GPS: " + la + "," + lo);
		double longitude = 0.36 * (lo - 52.15517440);
		double latitude = 0.36 * (la - 5.38720621);
		double SomX = (190094.945 * latitude) + (-11832.228 * longitude * latitude) + (-144.221 * Math.pow(longitude,2) * latitude) + (-32.391 * Math.pow(latitude,3)) + (-0.705 * longitude) + (-2.340 * Math.pow(longitude,3) * latitude) + (-0.608 * longitude * Math.pow(latitude,30) + (-0.008 * Math.pow(latitude,2)) + (0.148 * Math.pow(longitude,2) * Math.pow(latitude,3)));
		double SomY = (309056.544 * longitude) + (3638.893 * Math.pow(latitude,2)) + (73.077 * Math.pow(longitude,2)) + (-157.984 * longitude * Math.pow(latitude,2)) + (59.788 * Math.pow(longitude,3)) + (0.433 * latitude) + (-6.439 * Math.pow(longitude,2) * Math.pow(latitude,2)) + (-0.032 * longitude * latitude) + (0.092 * Math.pow(latitude,4)) + (-0.054 * longitude * Math.pow(latitude,4));
		int RDX = (int)(155000 + SomX);
		int RDY = (int)(463000 + SomY);
		System.out.println("locatie RD: " + RDX + "," + RDY);
		Integer bomen = 0;
		try {
			File file = new File("C:\\Users\\Jimmy\\Desktop\\bomen.txt");
			FileInputStream fis = new FileInputStream(file);
			// Here BufferedInputStream is added for fast reading.
			BufferedInputStream bis = new BufferedInputStream(fis);
			BufferedReader dis = new BufferedReader(new InputStreamReader(bis));
			String line;
			while ((line = dis.readLine()) != null) {
				int xbegin = 1,xeind,ybegin,yeind,zbegin,zeind = line.length();
				int i = 1;
				while (!String.valueOf(line.charAt(i)).equals(",")) {
					i++;
				}
				xeind = i - 1;
				ybegin = i + 1;
				while (!String.valueOf(line.charAt(i)).equals("]")) {
					i++;
				}
				yeind = i - 1;
				zbegin = i + 2;
				int x = Integer.parseInt(line.substring(xbegin,xeind + 1)),y = Integer.parseInt(line.substring(ybegin,yeind + 1)),z = Integer.parseInt(line.substring(zbegin,zeind));
				//bijde negatief is links onder bijde positief is rechts boven
				double X = x - RDX;
				double Y = y - RDY;
				double d;
				//verdeel berekening in positieve en negatieve kwadaranten
				if (X < 0) {
					if (Y < 0) {
						d = Math.sqrt((x - RDX) * (x - RDX) + (y - RDY) * (y - RDY));
					}
					else {
						d = Math.sqrt((x - RDX) * (x - RDX) + (y - RDY + 20) * (y - RDY + 20));
					}
				}
				else {
					if (Y < 0) {
						d = Math.sqrt((x - RDX + 20) * (x - RDX + 20) + (y - RDY) * (y - RDY));
					}
					else {
						d = Math.sqrt((x - RDX + 20) * (x - RDX + 20) + (y - RDY + 20) * (y - RDY + 20));
					}
				}
				if (d < 100) {
					bomen += z;
				}
				else if (d < 100 + Math.sqrt(800)) {
					d -= 100;
					d /= Math.sqrt(800);
					z = (int)Math.round(d * z);
					bomen += z;
				}
			}
			System.out.println("er zijn " + bomen + " bomen in de buurt");
			Double percent = bomen.doubleValue() / 400.0 * 100.0;
			int perc = percent.intValue();
			System.out.println("het percentage bomen in de buurt is " + perc + "%");
		}
		catch (IOException e) {}
	}
}