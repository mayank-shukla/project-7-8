package com.opendata.bomen;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.widget.TextView;

public class Sphere {
	private int REQUEST_ENABLE_BT = 1;
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	private Activity main;
	private BluetoothDevice device;
	private BluetoothSocket socket;
	private OutputStream out;
	private InputStream in;
	private boolean[][][][] sphere;

	public Sphere(Activity main) {
		this.main = main;
		sphere = new boolean[7][7][7][3];
		for (int x = 0;x < 7;x++) {
			for (int y = 0;y < 7;y++) {
				for (int z = 0;z < 7;z++) {
					try {
						checkSphereCordinate(x,y,z);
						for (int i = 0;i < 3;i++) {
							sphere[x][y][z][i] = false;
						}
					}
					catch (Exception e) {}
				}
			}
		}
	}

	public void deconstruct() {
		this.disableBluetooth();
		sphere = null;
		main = null;
	}

	public void enableBluetooth() {
		//TODO moet debuggen werkt niet op mobiel
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			TextView debug = (TextView)main.findViewById(R.id.debug);
			debug.setText("geen bluetooth gevonden");
			return;
		}
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice temp:pairedDevices) {
				if (temp.getAddress() == "00:06:66:46:5B:89" || temp.getAddress() == "000666465B89") {
					device = temp;
				}
			}
		}
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		main.registerReceiver(mReceiver,filter);
		// If no valid paired device found
		if (device == null) {
			while (mBluetoothAdapter.startDiscovery() != true) {}
		}
		// device discovery voor 30 seconds
		for (int i = 0;i < 30;i++) {
			if (device != null) {
				mBluetoothAdapter.cancelDiscovery();
				break;
			}
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {}
		}
		if (device == null) {
			TextView debug = (TextView)main.findViewById(R.id.debug);
			debug.setText("kan geen verbinding maken niet binnen bereik");
			return;
		}
		//device found
		//general UUID 00001101-0000-1000-8000-00805F9B34FB
		try {
			//Create a Socket connection: need the server's UUID number of registered
			socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			socket.connect();
			in = socket.getInputStream();
			out = socket.getOutputStream();
		}
		catch (IOException e) {}
		finally {
			if (socket != null) {
				try {
					socket.close();
					return;
				}
				catch (IOException e) {}
			}
		}
		TextView debug = (TextView)main.findViewById(R.id.debug);
		debug.setText("bluetooth aan");
	}

	public void disableBluetooth() {
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (socket != null) {
				if (socket.isConnected()) {
					socket.close();
				}
			}
		}
		catch (IOException e) {}
		socket = null;
		out = null;
		in = null;
		device = null;
		BluetoothAdapter.getDefaultAdapter().disable();
		TextView debug = (TextView)main.findViewById(R.id.debug);
		debug.setText("bluetooth uit");
	}

	public void write() {
		byte[] data = new byte[105];
		for (int c = 0;c < 3;c++) {
			for (int y = 0;y < 7;y++) {
				for (int x = 0;x < 5;x++) {
					byte temp = 0x00;
					switch(x) {
						case 0:
							if (sphere[2][0][y][c])
								temp |= 0x01;
							if (sphere[3][0][y][c])
								temp |= 0x02;
							if (sphere[4][0][y][c])
								temp |= 0x04;
							if (sphere[1][1][y][c])
								temp |= 0x08;
							if (sphere[2][1][y][c])
								temp |= 0x10;
							if (sphere[3][1][y][c])
								temp |= 0x20;
							if (sphere[4][1][y][c])
								temp |= 0x40;
							if (sphere[5][1][y][c])
								temp |= 0x80;
							break;
						case 1:
							if (sphere[0][2][y][c])
								temp |= 0x01;
							if (sphere[1][2][y][c])
								temp |= 0x02;
							if (sphere[2][2][y][c])
								temp |= 0x04;
							if (sphere[3][2][y][c])
								temp |= 0x08;
							if (sphere[4][2][y][c])
								temp |= 0x10;
							if (sphere[5][2][y][c])
								temp |= 0x20;
							if (sphere[6][2][y][c])
								temp |= 0x40;
							break;
						case 2:
							if (sphere[0][3][y][c])
								temp |= 0x01;
							if (sphere[1][3][y][c])
								temp |= 0x02;
							if (sphere[2][3][y][c])
								temp |= 0x04;
							if (sphere[3][3][y][c])
								temp |= 0x08;
							if (sphere[4][3][y][c])
								temp |= 0x10;
							if (sphere[5][3][y][c])
								temp |= 0x20;
							if (sphere[6][3][y][c])
								temp |= 0x40;
							break;
						case 3:
							if (sphere[0][4][y][c])
								temp |= 0x01;
							if (sphere[1][4][y][c])
								temp |= 0x02;
							if (sphere[2][4][y][c])
								temp |= 0x04;
							if (sphere[3][4][y][c])
								temp |= 0x08;
							if (sphere[4][4][y][c])
								temp |= 0x10;
							if (sphere[5][4][y][c])
								temp |= 0x20;
							if (sphere[6][4][y][c])
								temp |= 0x40;
							break;
						case 4:
							if (sphere[0][4][y][c])
								temp |= 0x01;
							if (sphere[1][4][y][c])
								temp |= 0x02;
							if (sphere[2][4][y][c])
								temp |= 0x04;
							if (sphere[3][4][y][c])
								temp |= 0x08;
							if (sphere[4][4][y][c])
								temp |= 0x10;
							if (sphere[5][4][y][c])
								temp |= 0x20;
							if (sphere[6][4][y][c])
								temp |= 0x40;
							break;
						case 5:
							if (sphere[1][5][y][c])
								temp |= 0x01;
							if (sphere[2][5][y][c])
								temp |= 0x02;
							if (sphere[3][5][y][c])
								temp |= 0x04;
							if (sphere[4][5][y][c])
								temp |= 0x08;
							if (sphere[5][5][y][c])
								temp |= 0x10;
							if (sphere[2][6][y][c])
								temp |= 0x20;
							if (sphere[3][6][y][c])
								temp |= 0x40;
							if (sphere[4][6][y][c])
								temp |= 0x80;
							break;
					}
					data[c * 35 + y * 5 + x] = temp;
				}
			}
		}
		try {
			out.write(data);
		}
		catch (IOException e) {}
	}

	public boolean[][][][] getSphere() {
		return sphere;
	}

	public void setSphere(boolean[][][][] data) {
		for (int x = 0;x < 7;x++) {
			for (int y = 0;y < 7;y++) {
				for (int z = 0;z < 7;z++) {
					try {
						checkSphereCordinate(x,y,z);
						for (int i = 0;i < 3;i++) {
							sphere[x][y][z][i] = data[x][y][z][i];
						}
					}
					catch (Exception e) {}
				}
			}
		}
	}

	private void checkSphereCordinate(int x, int y, int z) throws Exception {
		if (x < 0 || x > 6 || y < 0 || y > 6 || z < 0 || z > 6) {
			throw new Exception("invalid coordinate");
		}
		if (y == 2 || y == 3 || y == 4) {
			if (x == 0 || x == 6) {
				if (z != 2 && z != 3 && z != 4) {
					throw new Exception("invalid coordinate");
				}
			}
			else if (x == 1 || x == 5) {
				if (z == 0 || z == 6) {
					throw new Exception("invalid coordinate");
				}
			}
		}
		else if (y == 1 || y == 5) {
			if (x == 0 || x == 6 || z == 0 || z == 6) {
				throw new Exception("invalid coordinate");
			}
			else if (x == 1 || x == 5) {
				if (z == 1 || z == 5) {
					throw new Exception("invalid coordinate");
				}
			}
		}
		else if (z != 2 && z != 3 && z != 4 && x != 2 && x != 3 && x != 4) {
			throw new Exception("invalid coordinate");
		}
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice temp = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView
				if (temp.getAddress() == "00:06:66:46:5B:89" || temp.getAddress() == "000666465B89") {
					device = temp;
				}
			}
		}
	};
}