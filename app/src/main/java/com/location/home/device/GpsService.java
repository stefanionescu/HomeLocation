package com.location.home.device;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

import com.location.home.app.HomeLocationApplication;
import com.location.home.app.di.components.DaggerServiceComponent;

import javax.inject.Inject;

public class GpsService extends Service {

    @Inject
    Context context;

    LocationListener[] mLocationListeners;

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

        DaggerServiceComponent.builder()
                .applicationComponent(((HomeLocationApplication) getApplication()).getApplicationComponent())
                .build()
                .inject(this);

        mLocationListeners = new LocationListener[]{
                new LocationListener(LocationManager.GPS_PROVIDER, context),
                new LocationListener(LocationManager.NETWORK_PROVIDER, context)
        };

        initializeLocationManager();

        getLocationFromGPS();

        getLocationFromNetwork();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (mLocationManager != null) {

            for (int i = 0; i < mLocationListeners.length; i++) {
                try {

                    mLocationManager.removeUpdates(mLocationListeners[i]);

                } catch (Exception ex) {

                }
            }
        }
    }

    private void initializeLocationManager() {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void getLocationFromGPS(){

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 60 * 1000, 30,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {}

        catch (IllegalArgumentException ex) {}

    }

    private void getLocationFromNetwork(){

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 60 * 1000, 30,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {}

        catch (IllegalArgumentException ex) {}

    }


}
