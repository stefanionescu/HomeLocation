package com.location.home.device;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

import com.location.home.app.di.components.DaggerServiceComponent;
import com.location.home.app.di.modules.LocateUseCaseModule;
import com.location.home.domain.calculatehomelocation.LocateHome;

import javax.inject.Inject;

public class GpsService extends Service {

    LocationListener[] locationListeners;

    @Inject
    LocateHome locateHome;

    Context context;

    private com.location.home.device.NotificationManager notificationManager;

    private LocationManager mLocationManager = null;

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

        setupListeners();

        notificationManager = new com.location.home.device.NotificationManager(context);

        notificationManager.startNotification();

        initializeLocationManager();

        getLocationFromGPS();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if (mLocationManager != null) {

            for (int i = 0; i < locationListeners.length; i++) {
                try {

                    mLocationManager.removeUpdates(locationListeners[i]);

                } catch (Exception ex) {

                }
            }
        }

        notificationManager.stopNotification();

    }

    private void initializeLocationManager() {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void getLocationFromGPS() {

        try {

            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 60 * 1000, 25,
                    locationListeners[0]);

        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }

    }

    private void prepareVariables() {

        context = this.getApplicationContext();

        DaggerServiceComponent.builder()
                .locateUseCaseModule(new LocateUseCaseModule(context))
                .build()
                .inject(this);

    }

    private void setupListeners(){

        locationListeners = new LocationListener[]{
                new LocationListener(LocationManager.GPS_PROVIDER, context, locateHome)};

    }

}
