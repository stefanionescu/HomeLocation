package com.location.home.device;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.location.home.R;
import com.location.home.domain.calculatehomelocation.LocateHome;
import com.location.home.ui.utils.CheckAvailableProviders;
import com.location.home.ui.utils.CheckServiceRunning;

import java.text.DecimalFormat;

public class LocationListener implements android.location.LocationListener {

    private final double noiseAllowance = 30.00;
    private String provider;
    private LocateHome locateHome;
    private Location mLastLocation;
    private Context context;
    private GpsService service;
    private Notification.Builder builder;
    private CountDownTimer countDown;
    private int canEnableNetworkListener = 0;
    private android.app.NotificationManager manageNotifications;

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

        if (provider.equals(LocationManager.GPS_PROVIDER)) {

            setUpCountDown();

            countDown.start();

        }

        mLastLocation = new Location(provider);

    }

    @Override
    public void onLocationChanged(Location location) {

        if (provider.equals(LocationManager.GPS_PROVIDER)
                && location.getAccuracy() <= noiseAllowance) {

            gotPositionFromGps(location);

        }

        if (Double.parseDouble(String.valueOf(location.getAccuracy())) > noiseAllowance) {

            return;

        }

        changeNotificationText(location);

        processNewLocation(location);

    }

    @Override
    public void onProviderDisabled(String provider) {

        if (provider.equals(LocationManager.GPS_PROVIDER) && service != null) {

            disablingGps();

        } else if (provider.equals(LocationManager.NETWORK_PROVIDER) && service != null) {

            disablingNetwork();

        }

    }

    @Override
    public void onProviderEnabled(String provider) {

        if (this.provider.equals(LocationManager.GPS_PROVIDER)
                && provider.equals(LocationManager.NETWORK_PROVIDER)
                && canEnableNetworkListener > 0) {

            service.getLocationFromNetwork();

        } else if (this.provider.equals(LocationManager.NETWORK_PROVIDER)
                && provider.equals(LocationManager.GPS_PROVIDER))
            service.getLocationFromGPS();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void cleanResources() {

        stopService();

        clearVariables();

    }

    private void clearVariables() {

        try {
            countDown.cancel();
        } catch (Exception e) {
        }

        countDown = null;

        builder = null;

        locateHome = null;

        context = null;

        manageNotifications = null;

        provider = null;

        service = null;

    }

    private void stopService() {

        Intent sendIntent = new Intent();

        sendIntent.setAction("com.location.home.device.STOPPED_GETTING_LOCATION");

        context.sendBroadcast(sendIntent);

        context.stopService(new Intent(context, GpsService.class));

    }

    private void changeNotificationText(Location location) {

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

    private void disablingGps() {

        if (new CheckAvailableProviders().checkNetwork(context)) service.getLocationFromNetwork();

        service.removeListenerUpdates(0);

        if (!new CheckAvailableProviders().checkNetwork(context)) cleanResources();

        clearVariables();

    }

    private void disablingNetwork() {

        if (new CheckAvailableProviders().checkGps(context)) service.getLocationFromGPS();

        service.removeListenerUpdates(1);

        if (!new CheckAvailableProviders().checkGps(context)) cleanResources();

        clearVariables();

    }

    private void gotPositionFromGps(Location location) {

        if (location.getAccuracy() <= noiseAllowance) {

            restartCountDown();

            canEnableNetworkListener = 0;

            service.removeListenerUpdates(1);

        }

    }

    private void processNewLocation(Location location) {

        String newLocation = String.valueOf(location.getLatitude())
                + " "
                + String.valueOf(location.getLongitude());

        locateHome.execute(LocateHome.Params.forLocation(newLocation));

        mLastLocation.set(location);

    }

    private void setUpCountDown() {

        countDown = new CountDownTimer(65000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                if (new CheckServiceRunning(context).isMyServiceRunning(GpsService.class)) {

                    if (new CheckAvailableProviders().checkNetwork(context)) {

                        service.getLocationFromNetwork();

                    }

                    canEnableNetworkListener++;

                    restartCountDown();

                }

            }

        };

    }

    private void restartCountDown() {

        if (provider.equals(LocationManager.GPS_PROVIDER)) {

            countDown.cancel();

            setUpCountDown();

            countDown.start();

        }

    }

}
