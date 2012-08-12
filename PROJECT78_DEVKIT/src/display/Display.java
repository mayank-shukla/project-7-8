package display;
public class Display {
	private int[][][] cube_red;
	private int[][][] cube_green;
	private CubeObject[] objects;

	/**
	 * initialiseert een nieuwe frame
	 */
	public Display() {
		cube_red = new int[16][16][16];
		cube_green = new int[16][16][16];
		objects = new CubeObject[0];
		resetAllLeds();
	}

	public int[][][] getLedsGreen() {
		int[][][] green = new int[16][16][16];
		for (int x = 0;x < 16;x++) {
			for (int y = 0;y < 16;y++) {
				for (int z = 0;z < 16;z++) {
					green[x][y][z] = cube_green[x][y][z];
				}
			}
		}
		for (int i = 0;i < objects.length;i++) {
			CubeObject tempobj = objects[i];
			Led[] leds = tempobj.getLeds();
			for (int j = 0;j < leds.length;j++) {
				green[leds[j].getX()][leds[j].getY()][leds[j].getZ()] = leds[j].getGreen();
			}
		}
		return green;
	}

	public int[][][] getLedsRed() {
		int[][][] red = new int[16][16][16];
		for (int x = 0;x < 16;x++) {
			for (int y = 0;y < 16;y++) {
				for (int z = 0;z < 16;z++) {
					red[x][y][z] = cube_red[x][y][z];
				}
			}
		}
		for (int i = 0;i < objects.length;i++) {
			CubeObject tempobj = objects[i];
			Led[] leds = tempobj.getLeds();
			for (int j = 0;j < leds.length;j++) {
				red[leds[j].getX()][leds[j].getY()][leds[j].getZ()] = leds[j].getRed();
			}
		}
		return red;
	}

	public void setGreen(int green, int x, int y, int z) throws Exception {
		if (green < 0 || green > 255 || x < 0 || y < 0 || z < 0 || x > 15 || y > 15 || z > 15)
			throw new Exception("invalid value");
		cube_green[x][y][z] = green;
	}

	public void setRed(int red, int x, int y, int z) throws Exception {
		if (red < 0 || red > 255 || x < 0 || y < 0 || z < 0 || x > 15 || y > 15 || z > 15)
			throw new Exception("invalid value");
		cube_red[x][y][z] = red;
	}

	/**
	 * voegt een object toe aan de display
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void addObject(CubeObject obj) throws Exception {
		if (objects.length == 255)
			throw new Exception("niet meer dan 255 objecten");
		CubeObject[] tempobj = new CubeObject[objects.length + 1];
		int i;
		for (i = 0;i < objects.length;i++) {
			tempobj[i] = objects[i];
		}
		tempobj[i] = obj;
		i++;
		objects = tempobj;
	}

	public CubeObject[] getObjects() {
		return objects;
	}

	public byte[] toByte() {
		byte[] data = new byte[1024];
		for (int i = 0;i < 1024;i++) {
			data[i] = 0x00;
		}
		int x,y,z,cor;
		for (x = 0;x < 2;x++) {
			for (y = 0;y < 16;y++) {
				for (z = 0;z < 16;z++) {
					cor = y * 64 + x * 32 + z * 2;
					if (cube_red[x * 8][y][z] > 0) {
						data[cor] = (byte)(data[cor] | 0x01);
					}
					if (cube_red[x * 8 + 1][y][z] > 0) {
						data[cor] = (byte)(data[cor] | 0x02);
					}
					if (cube_red[x * 8 + 2][y][z] > 0) {
						data[cor] = (byte)(data[cor] | 0x04);
					}
					if (cube_red[x * 8 + 3][y][z] > 0) {
						data[cor] = (byte)(data[cor] | 0x08);
					}
					if (cube_red[x * 8 + 4][y][z] > 0) {
						data[cor] = (byte)(data[cor] | 0x10);
					}
					if (cube_red[x * 8 + 5][y][z] > 0) {
						data[cor] = (byte)(data[cor] | 0x20);
					}
					if (cube_red[x * 8 + 6][y][z] > 0) {
						data[cor] = (byte)(data[cor] | 0x40);
					}
					if (cube_red[x * 8 + 7][y][z] > 0) {
						data[cor] = (byte)(data[cor] | 0x80);
					}
					if (cube_green[x * 8][y][z] > 0) {
						data[cor + 1] = (byte)(data[cor] | 0x01);
					}
					if (cube_green[x * 8 + 1][y][z] > 0) {
						data[cor + 1] = (byte)(data[cor] | 0x02);
					}
					if (cube_green[x * 8 + 2][y][z] > 0) {
						data[cor + 1] = (byte)(data[cor] | 0x04);
					}
					if (cube_green[x * 8 + 3][y][z] > 0) {
						data[cor + 1] = (byte)(data[cor] | 0x08);
					}
					if (cube_green[x * 8 + 4][y][z] > 0) {
						data[cor + 1] = (byte)(data[cor] | 0x10);
					}
					if (cube_green[x * 8 + 5][y][z] > 0) {
						data[cor + 1] = (byte)(data[cor] | 0x20);
					}
					if (cube_green[x * 8 + 6][y][z] > 0) {
						data[cor + 1] = (byte)(data[cor] | 0x40);
					}
					if (cube_green[x * 8 + 7][y][z] > 0) {
						data[cor + 1] = (byte)(data[cor] | 0x80);
					}
				}
			}
		}
		return data;
	}

	public void fromByte(byte[] data) throws Exception {
		if (data.length != 1024) {
			throw new Exception("corupt data");
		}
		int x,y,z,cor;
		for (x = 0;x < 2;x++) {
			for (y = 0;y < 16;y++) {
				for (z = 0;z < 16;z++) {
					cor = y * 64 + x * 32 + z * 2;
					if ((byte)(data[cor] & (byte)0x01) == (byte)0x01) {
						cube_red[x * 8][y][z] = 255;
					}
					if ((byte)(data[cor] & (byte)0x02) == (byte)0x02) {
						cube_red[x * 8 + 1][y][z] = 255;
					}
					if ((byte)(data[cor] & (byte)0x04) == (byte)0x04) {
						cube_red[x * 8 + 2][y][z] = 255;
					}
					if ((byte)(data[cor] & (byte)0x08) == (byte)0x08) {
						cube_red[x * 8 + 3][y][z] = 255;
					}
					if ((byte)(data[cor] & (byte)0x10) == (byte)0x10) {
						cube_red[x * 8 + 4][y][z] = 255;
					}
					if ((byte)(data[cor] & (byte)0x20) == (byte)0x20) {
						cube_red[x * 8 + 5][y][z] = 255;
					}
					if ((byte)(data[cor] & (byte)0x40) == (byte)0x40) {
						cube_red[x * 8 + 6][y][z] = 255;
					}
					if ((byte)(data[cor] & (byte)0x80) == (byte)0x80) {
						cube_red[x * 8 + 7][y][z] = 255;
					}
					if ((byte)(data[cor + 1] & (byte)0x01) == (byte)0x01) {
						cube_green[x * 8][y][z] = 255;
					}
					if ((byte)(data[cor + 1] & (byte)0x02) == (byte)0x02) {
						cube_green[x * 8 + 1][y][z] = 255;
					}
					if ((byte)(data[cor + 1] & (byte)0x04) == (byte)0x04) {
						cube_green[x * 8 + 2][y][z] = 255;
					}
					if ((byte)(data[cor + 1] & (byte)0x08) == (byte)0x08) {
						cube_green[x * 8 + 3][y][z] = 255;
					}
					if ((byte)(data[cor + 1] & (byte)0x10) == (byte)0x10) {
						cube_green[x * 8 + 4][y][z] = 255;
					}
					if ((byte)(data[cor + 1] & (byte)0x20) == (byte)0x20) {
						cube_green[x * 8 + 5][y][z] = 255;
					}
					if ((byte)(data[cor + 1] & (byte)0x40) == (byte)0x40) {
						cube_green[x * 8 + 6][y][z] = 255;
					}
					if ((byte)(data[cor + 1] & (byte)0x80) == (byte)0x80) {
						cube_green[x * 8 + 7][y][z] = 255;
					}
				}
			}
		}
	}

	public void resetAllLeds() {
		for (int x = 0;x < 16;x++) {
			for (int y = 0;y < 16;y++) {
				for (int z = 0;z < 16;z++) {
					cube_green[x][y][z] = 0;
					cube_red[x][y][z] = 0;
				}
			}
		}
	}
}