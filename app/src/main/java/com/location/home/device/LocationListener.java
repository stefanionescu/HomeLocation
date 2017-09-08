package com.location.home.device;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.location.home.R;
import com.location.home.domain.calculatehomelocation.LocateHome;

import java.text.DecimalFormat;

public class LocationListener implements android.location.LocationListener {

    private String provider;
    private LocateHome locateHome;
    private Location mLastLocation;
    private Context context;
    private GpsService service;
    private Notification.Builder builder;
    private CountDownTimer countDown;
    private int isNetworkListenerEnabled = 0;
    private android.app.NotificationManager manageNotifications;

    private final double samePointAllowance = 35.00;

    public LocationListener(String provider,
                            Context context,
                            GpsService service,
                            LocateHome locateHome,
                            Notification.Builder builder,
                            android.app.NotificationManager manageNotifications) {

        this.locateHome = locateHome;

        this.context = context;

        this.builder = builder;

        this.provider = provider;

        this.service = service;

        this.manageNotifications = manageNotifications;

        if (provider.equals(LocationManager.GPS_PROVIDER)){

            setUpCountDown();

            countDown.start();

        }

        mLastLocation = new Location(provider);

    }

    @Override
    public void onLocationChanged(Location location) {

        restartCountDown();

        if (provider.equals(LocationManager.GPS_PROVIDER)) {

            isNetworkListenerEnabled = 0;

            service.removeListenerUpdates(1);

        }

        if (Double.parseDouble(String.valueOf(location.getAccuracy())) > samePointAllowance) {

            mLastLocation.set(location);

            return;

        }

        changeNotificationText(location);

        processNewLocation(location);

    }

    @Override
    public void onProviderDisabled(String provider) {

        if (checkNetwork() || checkGps()) return;

        cleanResources();

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private void cleanResources(){

        clearVariables();

        stopService();

    }

    private void clearVariables(){

       // if (provider.equals(LocationManager.GPS_PROVIDER))

       // countDown.cancel();

        countDown = null;

        service = null;

    }

    private void stopService(){

        Intent sendIntent = new Intent();

        sendIntent.setAction("com.location.home.device.STOPPED_GETTING_LOCATION");

        context.sendBroadcast(sendIntent);

        context.stopService(new Intent(context, GpsService.class));

    }

    private void changeNotificationText(Location location){

        DecimalFormat precision = new DecimalFormat("0.000");

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        builder.setContentText(
                context.getResources().getString(R.string.notification_text) +
                String.valueOf(precision.format(lat)) +
                " N " +
                String.valueOf(precision.format(lon)) +
                " E");

        manageNotifications.notify(0, builder.build());

    }

    private boolean checkGps(){

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean gpsEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        return gpsEnabled;

    }

    private boolean checkNetwork(){

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean networkEnabled = false;

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return networkEnabled;

    }

    private void processNewLocation(Location location){

        String newLocation = String.valueOf(location.getLatitude())
                + " "
                + String.valueOf(location.getLongitude());

        locateHome.execute(LocateHome.Params.forLocation(newLocation));

        mLastLocation.set(location);

    }

    private void setUpCountDown(){

        countDown = new CountDownTimer(61000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {

                if (isNetworkListenerEnabled == 0){

                    service.getLocationFromNetwork();

                    isNetworkListenerEnabled = 1;

                }

                restartCountDown();

            }

        };

    }

    private void restartCountDown(){

        if (provider.equals(LocationManager.GPS_PROVIDER)){

            countDown.cancel();

            setUpCountDown();

            countDown.start();

        }

    }

}
