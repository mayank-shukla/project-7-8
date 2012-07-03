package com.opendata.bomen;
/**
 * blak met kleur in het midden groen balletje dat er omheen draait
 * 
 * hoe meer groen hoe sneller het balletje draait
 * 
 * de balk in het midden is groener als er meer bomen zijn en roder als er
 * minder bomen zijn
 * 
 * @author Jimmy
 * 
 */
public class Bomen {
	int percent;
	int graden;
	private Main main;
	private Sphere sphere;

	public Bomen(Main main) {
		sphere = new Sphere(main);
		this.main = main;
		graden = 0;
		
		Threadg g = new Threadg();
		Threadw w = new Threadw();
		g.start();
		w.start();
	}

	public void berekenSphere() throws ArrayIndexOutOfBoundsException {
		boolean[][][][] data = new boolean[7][7][7][3];
		for (int x = 0;x < 7;x++) {
			for (int y = 0;y < 7;y++) {
				for (int z = 0;z < 7;z++) {
					for (int i = 0;i < 3;i++) {
						data[x][y][z][i] = false;
					}
				}
			}
		}
		//verdeel in 5 delen
		double hoog = 0.07 * percent;
		if (percent < 20) {
			//kleur rood
			for (int i = 0;i < hoog;i++) {
				for (int x = 2;x < 5;x++) {
					for (int y = 2;y < 5;y++) {
						data[x][y][i][Sphere.RED] = true;
					}
				}
			}
		}
		else if (percent < 40) {
			//kleur rood+blauw
			for (int i = 0;i < hoog;i++) {
				for (int x = 2;x < 5;x++) {
					for (int y = 2;y < 5;y++) {
						data[x][y][i][Sphere.RED] = true;
						data[x][y][i][Sphere.BLUE] = true;
					}
				}
			}
		}
		else if (percent < 60) {
			//kleur blauw
			for (int i = 0;i < hoog;i++) {
				for (int x = 2;x < 5;x++) {
					for (int y = 2;y < 5;y++) {
						data[x][y][i][Sphere.BLUE] = true;
					}
				}
			}
		}
		else if (percent < 80) {
			//kleur blauw + groen
			for (int i = 0;i < hoog;i++) {
				for (int x = 2;x < 5;x++) {
					for (int y = 2;y < 5;y++) {
						data[x][y][i][Sphere.GREEN] = true;
						data[x][y][i][Sphere.BLUE] = true;
					}
				}
			}
		}
		else {
			//kleur groen
			for (int i = 0;i < hoog;i++) {
				for (int x = 2;x < 5;x++) {
					for (int y = 2;y < 5;y++) {
						data[x][y][i][Sphere.GREEN] = true;
					}
				}
			}
		}
		//bolletje om balk
		switch(graden % 22) {
			case 0:
				data[0][2][3][Sphere.GREEN] = true;
				break;
			case 1:
				data[0][3][3][Sphere.GREEN] = true;
				break;
			case 2:
				data[0][4][3][Sphere.GREEN] = true;
				break;
			case 3:
				data[1][5][3][Sphere.GREEN] = true;
				break;
			case 4:
				data[2][6][3][Sphere.GREEN] = true;
				break;
			case 5:
				data[3][6][3][Sphere.GREEN] = true;
				break;
			case 6:
				data[4][6][3][Sphere.GREEN] = true;
				break;
			case 7:
				data[5][5][3][Sphere.GREEN] = true;
				break;
			case 8:
				data[6][4][3][Sphere.GREEN] = true;
				break;
			case 9:
				data[6][3][3][Sphere.GREEN] = true;
				break;
			case 10:
				data[6][2][3][Sphere.GREEN] = true;
				break;
			case 11:
				data[5][1][3][Sphere.GREEN] = true;
				break;
			case 12:
				data[4][0][3][Sphere.GREEN] = true;
				break;
			case 13:
				data[3][0][3][Sphere.GREEN] = true;
				break;
			case 14:
				data[2][0][3][Sphere.GREEN] = true;
				break;
			case 15:
				data[1][1][3][Sphere.GREEN] = true;
				break;
		}
		sphere.setSphere(data);
	}

	public void disableBluetooth() {
		sphere.disableBluetooth();
	}

	public void enableBluetooth() {
		sphere.enableBluetooth();
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public void deconstruct() {
		sphere.deconstruct();
	}

	private class Threadg extends Thread {
		public void run() {
			boolean con = true;
			while (con) {
				try {
					Thread.sleep(1000 - (250 * percent / 100));
				}
				catch (InterruptedException e) {}
				graden += 10;
				if (graden == 360)
					graden = 0;
			}
		}
	}

	private class Threadw extends Thread {
		public void run() {
			boolean con = true;
			while (con) {
				try {
					Thread.sleep(66);
				}
				catch (InterruptedException e) {}
				sphere.write();
			}
		}
	}
}