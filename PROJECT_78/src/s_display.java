//TODO standaard objecten toevoegen bijvoorbeeld kubus ergens invoegen
public class s_display {

	private int[][][] cube_red;
	private int[][][] cube_green;

	private s_object[] objects;

	public s_display() {
		cube_red = new int [16][16][16];
		cube_green = new int [16][16][16];
		objects = new s_object[0];
		
		for(int x=0;x<16;x++) {
			for(int y=0;y<16;y++) {
				for(int z=0;z<16;z++) {
					cube_green[x][y][z] = 0;
					cube_red[x][y][z] = 0;
				}
			}
		}
	}

	public int[][][] getLedsGreen() {
		int[][][] green = new int[16][16][16];
		for(int x=0;x<16;x++) {
			for(int y=0;y<16;y++) {
				for(int z=0;z<16;z++) {
					green[x][y][z] = cube_green[x][y][z];
				}
			}
		}

		for(int i=0;i<objects.length;i++) {
			s_object tempobj = objects[i];
			s_led[] leds = tempobj.getLeds();
			
			for(int j=0;j<leds.length;j++) {
				green[leds[j].getX()][leds[j].getY()][leds[j].getZ()] = leds[j].getGreen(); 
			}
		}
		return green;
	}

	public int[][][] getLedsRed() {
		int[][][] red = new int[16][16][16];
		for(int x=0;x<16;x++) {
			for(int y=0;y<16;y++) {
				for(int z=0;z<16;z++) {
					red[x][y][z] = cube_red[x][y][z];
				}
			}
		}

		for(int i=0;i<objects.length;i++) {
			s_object tempobj = objects[i];
			s_led[] leds = tempobj.getLeds();
			
			for(int j=0;j<leds.length;j++) {
				red[leds[j].getX()][leds[j].getY()][leds[j].getZ()] = leds[j].getRed(); 
			}
		}
		return red;
	}

	public void setGreen(int green, int x, int y, int z) throws Exception {
		if(green<0 || green >255 || x<0 || y<0 || z<0 || x>15 || y>15 || z>15)
			throw new Exception("invalid value");
		cube_green[x][y][z] = green;
	}

	public void setRed(int red, int x, int y, int z) throws Exception {
		if(red<0 || red >255 || x<0 || y<0 || z<0 || x>15 || y>15 || z>15)
			throw new Exception("invalid value");
		cube_red[x][y][z] = red;
	}

	public void addObject(s_object obj) {
		s_object[] tempobj = new s_object[objects.length+1];
		int i;
		for(i=0;i<=objects.length;i++) {
			tempobj[i] = objects[i];
		}
		i++;
		tempobj[i] = obj;
		objects = tempobj;
	}


	public s_object[] getObjects() {
		return objects;
	}


	public void setObjects(s_object[] objects) {
		this.objects = objects;
	}
}