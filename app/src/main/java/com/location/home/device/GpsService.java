package com.location.home.device;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.location.home.app.di.components.DaggerServiceComponent;
import com.location.home.app.di.modules.LocateUseCaseModule;
import com.location.home.app.di.modules.ServiceModule;
import com.location.home.domain.calculatehomelocation.LocateHome;

import javax.inject.Inject;

public class GpsService extends Service {

    @Inject
    LocateHome locateHome;
    @Inject
    Notification.Builder builder;
    @Inject
    android.app.NotificationManager manageNotifications;
    private LocationListener[] locationListeners;
    private Context context;
    private com.location.home.device.NotificationManager notificationManager;

    private LocationManager locationManager = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;

    }

    @Override
    public void onCreate() {

        prepareVariables();

        notificationManager =
                new com.location.home.device.
                        NotificationManager
                        (context, builder.build(),
                                manageNotifications);

        notificationManager.startNotification();

        setupListeners();

        initializeLocationManager();

        getLocationFromGPS();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        removeAllListeners();

        notificationManager.stopNotification();

    }

    public void removeListenerUpdates(int i) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (locationManager.getAllProviders().size() >= i)
                locationManager.removeUpdates(locationListeners[i]);

        }

    }

    private void initializeLocationManager() {

        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }

    }

    private void getLocationFromGPS() {

        try {

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 60 * 1000, 15,
                    locationListeners[0]);

        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }

    }

    public void getLocationFromNetwork() {

        try {

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 60 * 1000, 15,
                    locationListeners[1]);

        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }


    }

    private void prepareVariables() {

        context = this.getApplicationContext();

        DaggerServiceComponent.builder()
                .locateUseCaseModule(new LocateUseCaseModule(context))
                .serviceModule(new ServiceModule(context))
                .build()
                .inject(this);

    }

    private void setupListeners() {

        locationListeners = new LocationListener[]{
                new LocationListener(LocationManager.GPS_PROVIDER,
                        context,
                        this,
                        locateHome,
                        builder,
                        manageNotifications),

                new LocationListener(LocationManager.NETWORK_PROVIDER,
                        context,
                        this,
                        locateHome,
                        builder,
                        manageNotifications)};

    }

    private void removeAllListeners() {

        if (locationManager != null) {

            for (int i = 0; i < locationListeners.length; i++) {

                try {

                    removeListenerUpdates(i);

                } catch (Exception ex) {

                }

            }
        }

    }

}
