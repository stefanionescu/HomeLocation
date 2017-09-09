package com.location.home.ui.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;

public class CheckAvailableProviders {

    public boolean checkGps(Context context) {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean gpsEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        return gpsEnabled;

    }

    public boolean checkNetwork(Context context) {

        final ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnectedOrConnecting() || mobile.isConnectedOrConnecting())
            return true;

        return false;

    }

}
