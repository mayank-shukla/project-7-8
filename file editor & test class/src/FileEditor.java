import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileEditor {
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Chinwei\\Desktop\\bomen.txt");
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		BufferedReader dis = null;
		Integer[][] map = new Integer[162][67];

		for (int a = 0;a < 162;a++) {
			for (int b = 0;b < 67;b++) {
				map[a][b] = 0;
				try {
					
					
					// Here BufferedInputStream is added for fast reading.
					
					//dis = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file))));
					dis = new BufferedReader(new FileReader(file));
					String temp = dis.readLine();
					int x,y;
					while(temp != null){
					if(temp.length() == 12)
					{ x = Integer.parseInt(temp.substring(1,5));y = Integer.parseInt(temp.substring(6,11));}
					else
					{x = Integer.parseInt(temp.substring(1,6));y = Integer.parseInt(temp.substring(7,12));}
					
					//System.out.println("read:\n  x:"+a+"\n  y:"+b+"\n  count:"+count);
					
						//System.out.println("x: "+x+" y: "+y);
						if (inRange(a*25+6050,b*25+42901,x,y)) {
							map[a][b] += 1;
						}
						//out.write();
						//out.newLine();
						
						
						temp = dis.readLine();
					}
					System.out.println("read:\n  x:"+a+"\n  y:"+b);
					// dispose all the resources after using them.
					dis.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			
	
			}

		}
		try {
			FileWriter f = new FileWriter("C:\\Users\\Chinwei\\Desktop\\out.txt");
			BufferedWriter out = new BufferedWriter(f);
			for (int y = 0;y < 67;y++) {
				for (int x = 0;x < 162;x++) {
					String temp = map[x][y].toString();
					if (temp.length() == 0) {
						temp = "0000" + temp;
					}
					else if (temp.length() == 1) {
						temp = "000" + temp;
					}
					else if (temp.length() == 2) {
						temp = "00" + temp;
					}
					else if (temp.length() == 3) {
						temp = "0" + temp;
					}
					out.write(temp);
				}
				out.newLine();
			}
		}
		catch (IOException e) {}
	}

	public static boolean inRange(int orX, int orY, int tarX, int tarY) {
		int x = orX - tarX,y = orY - tarY;
		if (Math.sqrt(x * x + y * y) < 175) {
			return true;
		}
		return false;
	}
}
/*
 * File file = new File("C:\\Users\\Jimmy\\Desktop\\bomen.txt"); FileInputStream
 * fis = null; BufferedInputStream bis = null; BufferedReader dis = null;
 * int[][] map = new int[2020][838]; for (int i = 0;i < 2020;i++) { for (int j =
 * 0;j < 838;j++) { map[i][j] = 0; } } try {
 * 
 * fis = new FileInputStream(file); // Here BufferedInputStream is added for
 * fast reading. bis = new BufferedInputStream(fis); dis = new
 * BufferedReader(new InputStreamReader(bis));
 * 
 * FileWriter file1stop1 = new FileWriter("C:\\Users\\Jimmy\\Desktop\\1.1.txt");
 * BufferedWriter out = new BufferedWriter(file1stop1);
 * 
 * String temp = dis.readLine(); while (temp != null) {
 * 
 * temp = dis.readLine(); } // dispose all the resources after using them.
 * fis.close(); bis.close(); dis.close(); out.close(); } catch (Exception e) {
 * e.printStackTrace(); }
 */