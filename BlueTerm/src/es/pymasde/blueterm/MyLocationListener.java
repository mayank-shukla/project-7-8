package es.pymasde.blueterm;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {
	private BlueTerm main;

	public MyLocationListener(BlueTerm main) {
		this.main = main;
	}

	public void onLocationChanged(Location loc) {
		main.locChange(loc.getLongitude(),loc.getLatitude());
	}

	public void onProviderDisabled(String arg0) {}

	public void onProviderEnabled(String arg0) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}
}