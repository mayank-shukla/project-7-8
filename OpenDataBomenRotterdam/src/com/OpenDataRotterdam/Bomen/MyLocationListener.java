package com.OpenDataRotterdam.Bomen;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {
	private OpenDataBomenRotterdamActivity main;

	public MyLocationListener(OpenDataBomenRotterdamActivity main) {
		this.main = main;
	}

	public void onLocationChanged(Location loc) {
		main.locChange(loc.getLongitude(),loc.getLatitude());
	}

	public void onProviderDisabled(String arg0) {}

	public void onProviderEnabled(String arg0) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}
}
