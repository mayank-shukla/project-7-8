package com.opendata.bomen;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {
	private Main main;

	public MyLocationListener(Main main) {
		this.main = main;
	}

	public void onLocationChanged(Location loc) {
		main.locChange(loc.getLongitude(),loc.getLatitude());
	}

	public void onProviderDisabled(String e) {}

	public void onProviderEnabled(String e) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}
}