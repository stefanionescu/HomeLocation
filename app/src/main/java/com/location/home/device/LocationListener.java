package com.location.home.device;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

public class LocationListener implements android.location.LocationListener {

    private Location mLastLocation;
    private FileManager manager;
    private Context context;

    public LocationListener(String provider, Context context) {

        mLastLocation = new Location(provider);

        manager = new FileManager();

        this.context = context;

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation.set(location);

        Intent sendIntent = new Intent();
        sendIntent.setAction("com.location.home.device.CALCULATE");
        sendIntent.putExtra("latitude", location.getLatitude());
        sendIntent.putExtra("longitude", location.getLongitude());

        context.sendBroadcast(sendIntent);

    }

    @Override
    public void onProviderDisabled(String provider) {

        Intent sendIntent = new Intent();
        sendIntent.setAction("com.location.home.device.STOPPED_GETTING_LOCATION");

        context.sendBroadcast(sendIntent);

        context.stopService(new Intent(context, GpsService.class));

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}
