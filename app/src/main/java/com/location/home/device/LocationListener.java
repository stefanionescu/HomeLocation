package com.location.home.device;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.location.home.domain.calculatehomelocation.LocateHome;

public class LocationListener implements android.location.LocationListener {

    private LocateHome locateHome;
    private Location mLastLocation;
    private Context context;

    public LocationListener(String provider, Context context, LocateHome locateHome) {

        mLastLocation = new Location(provider);

        this.locateHome = locateHome;

        this.context = context;

    }

    @Override
    public void onLocationChanged(Location location) {

        String s = String.valueOf(location.getLatitude())
                + " "
                + String.valueOf(location.getLongitude());

        locateHome.execute(LocateHome.Params.forUser(s));

        mLastLocation.set(location);

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
