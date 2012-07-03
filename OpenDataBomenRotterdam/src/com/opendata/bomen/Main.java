package com.opendata.bomen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {
	private Button blueon,blueoff,gpson,gpsoff;
	private TextView debug;
	private LocationManager mlocManager;
	private MyLocationListener mlocListener;
	int[][] map;
	private Bomen bomen;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bomen = new Bomen(this);
		setContentView(R.layout.main);
		blueon = (Button)findViewById(R.id.bluetoothon);
		blueoff = (Button)findViewById(R.id.bluetoothoff);
		gpson = (Button)findViewById(R.id.gpson);
		gpsoff = (Button)findViewById(R.id.gpsoff);
		debug = (TextView)findViewById(R.id.debug);
		blueon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				bluetoothOn();
			}
		});
		blueoff.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				bluetootOff();
			}
		});
		gpson.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gpsOn();
			}
		});
		gpsoff.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gpsOff();
			}
		});
	}

	protected void gpsOff() {
		if (mlocManager != null)
			mlocManager.removeUpdates(mlocListener);
		mlocManager = null;
		mlocListener = null;
		debug.setText("GPS uit");
	}

	protected void gpsOn() {
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if (mlocManager != null) {
			mlocListener = new MyLocationListener(this);
			mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,mlocListener);
			debug.setText("GPS aan");
		}
		else {
			debug.setText("geen GPS");
		}
	}

	protected void bluetootOff() {
		bomen.disableBluetooth();
	}

	protected void bluetoothOn() {
		bomen.enableBluetooth();
	}

	protected void onDestroy() {
		bomen.deconstruct();
		bomen = null;
		gpsOff();
		blueon = null;
		blueoff = null;
		gpson = null;
		gpsoff = null;
		debug = null;
		map = null;
		super.onDestroy();
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
		}
		catch (IOException e) {}
		
		//TODO aan hand van counter groen in cube bepalen
		this.bomen.setPercent(bomen/400);
	}
}