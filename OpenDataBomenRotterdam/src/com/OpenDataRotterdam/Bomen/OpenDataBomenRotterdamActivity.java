package com.OpenDataRotterdam.Bomen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class OpenDataBomenRotterdamActivity extends Activity {
	/* Get Default Adapter */
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	private BluetoothSocket socket;
	private OutputStream out;
	private InputStream in;
	private LocationManager mlocManager;
	private MyLocationListener mlocListener;
	private BluetoothDevice device;
	private Test fdsa;
	private static final int REQUEST_CONNECT = 0x2;
	private static final int REQUEST_ENABLE = 0x1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	/* Enable BT */
	public void onEnableButtonClicked(View view) {
		Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(enabler,REQUEST_ENABLE);
		//enable
		//_bluetooth.enable();
	}

	/* Close BT */
	public void onDisableButtonClicked(View view) {
		_bluetooth.disable();
	}

	/* Client */
	public void onOpenClientSocketButtonClicked(View view) {
		Intent enabler = new Intent(this,ClientSocketActivity.class);
		startActivityForResult(enabler,REQUEST_CONNECT);
	}

	public void onCloseClientSocketButtonClicked(View view) {
		if (socket != null) {
			if (socket.isConnected()) {
				fdsa.con = false;
			}
		}
	}

	/* make anim */
	public void onAnimButtonClicked(View view) {
		if (out != null) {
			try {
				out.write(0xff);
			}
			catch (IOException e) {}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != REQUEST_CONNECT) {
			return;
		}
		if (resultCode != RESULT_OK) {
			return;
		}
		final BluetoothDevice device = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		connect(device);
	}

	protected void connect(BluetoothDevice device) {
		//BluetoothSocket socket = null;
		try {
			//Create a Socket connection: need the server's UUID number of registered
			this.device = device;
			Method m = device.getClass().getMethod("createRfcommSocket",new Class[] {int.class});
			BluetoothSocket tmp = (BluetoothSocket)m.invoke(device,1);
			socket = tmp;
			if (_bluetooth.getState() != BluetoothAdapter.STATE_ON) {
				_bluetooth.enable();
			}
			_bluetooth.cancelDiscovery();
			socket.connect();
			Log.d("EF-BTBee",">>Client connectted");
			in = socket.getInputStream();
			out = socket.getOutputStream();
			makedata();
		}
		catch (Exception e) {
			String stack = "";
			StackTraceElement[] asdf = e.getStackTrace();
			for (int j = 0;j < asdf.length;j++) {
				stack += asdf[j].getFileName() + " " + asdf[j].getMethodName() + " " + asdf[j].getLineNumber() + "\n";
			}
			TextView debug = (TextView)findViewById(R.id.debug);
			debug.setText(e.getMessage() + "\n" + stack);
		}
	}

	private void makedata() {
		fdsa = new Test();
		fdsa.start();
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if (mlocManager != null) {
			mlocListener = new MyLocationListener(this);
			mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,mlocListener);
		}
	}

	public void write(byte[] data) {
		try {
			out.write(data);
		}
		catch (IOException e) {}
	}

	public void locChange(double lo, double la) {
		double longitude = 0.36 * (lo - 52.15517440);
		double latitude = 0.36 * (la - 5.38720621);
		double SomX = (190094.945 * latitude) + (-11832.228 * longitude * latitude) + (-144.221 * Math.pow(longitude,2) * latitude) + (-32.391 * Math.pow(latitude,3)) + (-0.705 * longitude) + (-2.340 * Math.pow(longitude,3) * latitude) + (-0.608 * longitude * Math.pow(latitude,30) + (-0.008 * Math.pow(latitude,2)) + (0.148 * Math.pow(longitude,2) * Math.pow(latitude,3)));
		double SomY = (309056.544 * longitude) + (3638.893 * Math.pow(latitude,2)) + (73.077 * Math.pow(longitude,2)) + (-157.984 * longitude * Math.pow(latitude,2)) + (59.788 * Math.pow(longitude,3)) + (0.433 * latitude) + (-6.439 * Math.pow(longitude,2) * Math.pow(latitude,2)) + (-0.032 * longitude * latitude) + (0.092 * Math.pow(latitude,4)) + (-0.054 * longitude * Math.pow(latitude,4));
		int RDX = (int)(155000 + SomX);
		int RDY = (int)(463000 + SomY);
		int bomen = 0;
		InputStream inS = getResources().openRawResource(R.raw.bomen);
		InputStreamReader inR = new InputStreamReader(inS);
		BufferedReader buffR = new BufferedReader(inR);
		String line;
		try {
			while ((line = buffR.readLine()) != null) {
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
			out.write(bomen * 100 / 400);
		}
		catch (IOException e) {}
	}

	private class Test extends Thread {
		boolean con = true;

		public void run(){
			byte i = 0x00;
			while (con) {
				if (out != null) {
					try {
						socket.connect();
						if(socket.isConnected()) {
							out = socket.getOutputStream();
							out.write(i);
							out.close();
							i++;
						}
					}
					catch (IOException e) {}
				}
			}
			
			if (socket != null) {
				if (socket.isConnected()) {
					try {
						socket.close();
					}
					catch (IOException e) {
					}
				}
			}
		}
	}
}
/*
 * String stack=""; StackTraceElement[] asdf = e.getStackTrace(); for(int
 * j=0;j<asdf.length;j++) { stack +=
 * asdf[j].getFileName()+" "+asdf[j].getMethodName
 * ()+" "+asdf[j].getLineNumber()+"\n"; } TextView debug =
 * (TextView)findViewById(R.id.debug); debug.setText(e.getMessage()+"\n"+stack);
 */