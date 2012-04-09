public class s_display {

	/* manier om gegevens naar led cube te streamen:
	 * geef een mode bit per frame mee om te bepalen welke lampjes niet gedfineerd moeten worden:
	 * 		bijvoorbeeld het grootste aantal soorten lampjes is die uit staan, dan geef je code 0. dan zet je per default alle lampjes uit tenzij anders gedefineerd in de gegevens
	 * voor elke lampje heb je 2 byte nodig:
	 * 		0000 0000 0000 0000
	 * 		  ^^ ^^^^ ^^^^ ^^^^^
	 * 		  || |||| |||| |||||
	 * 		  ||worden voor de positie gebruikt
	 * 		  ||
	 * 		  worden voor de kleurcode gebruikt
	 * 
	 * met eerder genoemde methode hoofd maximaal 75% van alle lampjes worden verstuurd
	 * 16^3=4096
	 * 4096*0.75 = 3072
	 * 3072*2 = 6144
	 * 6144+1 = 6145 bytes per frame
	 */

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

	public String generate5CubeText() {
		int len=0;
		for(int z=0;z<5;z++) {
			for(int y=0;y<5;y++) {
				for(int x=0;x<5;x++) {
					if(cube_red[x][y][z] == 255 || cube_green[x][y][z] == 255)
						//code = code + (x+5*y+5*5*z+1) + ",";
						len++;
				}
			}
		}

		String code;
		if(len==0)
			code = "for(int i=0;i<100;i++) {";
		else if(len<=100)
			code = "for(int i=0;i<"+100/len+";i++) {";
		else
			code = "for(int i=0;i<"+(100/len+1)+";i++) {";

		for(int z=0;z<5;z++) {
			for(int y=0;y<5;y++) {
				for(int x=0;x<5;x++) {
					if(cube_red[x][y][z] == 255 || cube_green[x][y][z] == 255)
						code = code + "led" + (x+5*y+5*5*z+1) + "();";
				}
			}
		}
		code=code+"}";
		
		return code;
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

	public void addObject(s_object obj) throws Exception {
		if(objects.length==255)
			throw new Exception("niet meer dan 255 objecten");
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

	public byte[] displayToByte() {
		byte[] data = new byte[8192];
		for(int x=0;x<16;x++) {
			for(int y=0;y<16;y++) {
				for(int z=0;z<16;z++) {
					data[z+y*16+x*256] = (byte) cube_red[x][y][z];
					data[z+y*16+x*256+4096] = (byte) cube_green[x][y][z];
				}
			}
		}
		return data;
	}

	public void byteToDisplay(byte[] data) {
		for(int x=0;x<16;x++) {
			for(int y=0;y<16;y++) {
				for(int z=0;z<16;z++) {
					cube_red[x][y][z] = data[z+y*16+x*256] & 0xff;
					cube_green[x][y][z] = data[z+y*16+x*256+4096] & 0xff;
				}
			}
		}
	}
}