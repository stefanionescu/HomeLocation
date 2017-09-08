package com.location.home.device;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.location.home.R;
import com.location.home.domain.calculatehomelocation.LocateHome;

public class LocationListener implements android.location.LocationListener {

    private LocateHome locateHome;
    private Location mLastLocation;
    private Context context;
    private Notification.Builder builder;
    private android.app.NotificationManager manageNotifications;

    public LocationListener(String provider,
                            Context context,
                            LocateHome locateHome,
                            Notification.Builder builder,
                            android.app.NotificationManager manageNotifications) {

        mLastLocation = new Location(provider);

        this.locateHome = locateHome;

        this.context = context;

        this.builder = builder;

        this.manageNotifications = manageNotifications;

    }

    @Override
    public void onLocationChanged(Location location) {

        changeNotificationText(location);

        String newLocation = String.valueOf(location.getLatitude())
                + " "
                + String.valueOf(location.getLongitude());

        locateHome.execute(LocateHome.Params.forLocation(newLocation));

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

    private void changeNotificationText(Location location){

        builder.setContentTitle(context.getString(R.string.notification_text) +
                String.valueOf(location.getLatitude()) +
                " N " +
                String.valueOf(location.getLongitude()) +
                " E");

        manageNotifications.notify(0, builder.build());

    }

}
