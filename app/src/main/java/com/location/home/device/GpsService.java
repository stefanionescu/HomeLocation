package com.location.home.device;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

import com.location.home.app.di.components.DaggerServiceComponent;
import com.location.home.app.di.modules.LocateUseCaseModule;
import com.location.home.app.di.modules.ServiceModule;
import com.location.home.domain.calculatehomelocation.CalculateHome;

import javax.inject.Inject;

public class GpsService extends Service {

    @Inject
    CalculateHome calculateHome;
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

        getLocationFromGPS(60 * 1000, 15);

    }

    @Override
    public void onDestroy() {

        removeAllListeners();

        locationManager = null;

        notificationManager.stopNotification();

        locationListeners = null;

        super.onDestroy();


    }

    public void removeListenerUpdates(int i) {

        try{

            locationManager.removeUpdates(locationListeners[i]);

            } catch(Exception e){}

    }

    private void initializeLocationManager() {

        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }

    }

    public void getLocationFromGPS(int time, int distance) {

        try {

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, time, distance,
                    locationListeners[0]);

        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }

    }

    public void getLocationFromNetwork(int time, int distance) {

        try {

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, time, distance,
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
                        calculateHome,
                        builder,
                        manageNotifications),

                new LocationListener(LocationManager.NETWORK_PROVIDER,
                        context,
                        this,
                        calculateHome,
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
