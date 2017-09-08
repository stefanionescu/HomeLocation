package com.location.home.device;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

import com.location.home.R;
import com.location.home.app.HomeLocationApplication;
import com.location.home.app.di.components.DaggerServiceComponent;
import com.location.home.ui.activities.MainActivity;

import javax.inject.Inject;

public class GpsService extends Service {

    @Inject
    Context context;

    private LocationListener[] mLocationListeners;

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

        startNotification(createNotificationBuilder(getPendingIntent()));

        mLocationListeners = new LocationListener[]{
                new LocationListener(LocationManager.GPS_PROVIDER, context),
                new LocationListener(LocationManager.NETWORK_PROVIDER, context)};

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

        stopNotification();

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

    private void startNotification(Notification notification){

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);

    }

    private PendingIntent getPendingIntent(){

        Intent showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                0);

        return contentIntent;

    }

    private Notification createNotificationBuilder(PendingIntent contentIntent){

        return new Notification.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();

    }

    private void stopNotification(){

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.cancel(0);

    }


}
