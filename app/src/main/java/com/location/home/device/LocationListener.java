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
import com.location.home.domain.calculatehomelocation.CalculateHome;
import com.location.home.ui.utils.CheckAvailableProviders;
import com.location.home.ui.utils.CheckServiceRunning;

import java.text.DecimalFormat;

public class LocationListener implements android.location.LocationListener {

    private final double noiseAllowance = 30.00;
    private String provider;
    private CalculateHome calculateHome;
    private Location mLastLocation;
    private Context context;
    private GpsService service;
    private Notification.Builder builder;
    private CountDownTimer countDown;
    private android.app.NotificationManager manageNotifications;

    public LocationListener(String provider,
                            Context context,
                            GpsService service,
                            CalculateHome calculateHome,
                            Notification.Builder builder,
                            android.app.NotificationManager manageNotifications) {

        this.calculateHome = calculateHome;

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

        if (Double.parseDouble(String.valueOf(location.getAccuracy())) > noiseAllowance) {

            return;

        }

        if (provider.equals(LocationManager.GPS_PROVIDER)) {

            gotPositionFromGps();

        }

        changeNotificationText(location);

        processNewLocation(location);

    }

    @Override
    public void onProviderDisabled(String provider) {

        CheckAvailableProviders providers = new CheckAvailableProviders();

        if (providers.checkGps(context) == false) {

            cleanResources();

        }

    }

    @Override
    public void onProviderEnabled(String provider) {

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

        calculateHome = null;

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

        manageNotifications.notify(1, builder.build());

    }

    private void gotPositionFromGps() {

        restartCountDown();

        service.removeListenerUpdates(1);

    }

    private void processNewLocation(Location location) {

        String newLocation = String.valueOf(location.getLatitude())
                + " "
                + String.valueOf(location.getLongitude());

        calculateHome.execute(CalculateHome.Params.forLocation(newLocation));

        mLastLocation.set(location);

    }

    private void setUpCountDown() {

        countDown = new CountDownTimer(62000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                if (new CheckServiceRunning(context).isMyServiceRunning(GpsService.class)) {

                    if (new CheckAvailableProviders().checkNetwork(context)) {

                        service.removeListenerUpdates(1);

                        service.getLocationFromNetwork(90 * 1000, 20);

                    } else
                        showToastEnableWifi();

                    restartCountDown();

                }

            }

        };

    }

    private void showToastEnableWifi() {

        Toast.makeText(context,
                String.valueOf(context.getResources().getString(R.string.not_able_to_get_location)),
                Toast.LENGTH_LONG).show();

    }

    private void restartCountDown() {

        if (provider.equals(LocationManager.GPS_PROVIDER)) {

            countDown.cancel();

            setUpCountDown();

            countDown.start();

        }

    }

}
