package com.location.home.ui.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.util.Log;

import com.location.home.domain.calculatehomelocation.LocateHome;
import com.location.home.domain.model.Approximation;
import com.location.home.ui.views.MainView;

import javax.inject.Inject;

public class MainPresenterImp implements MainPresenter {

    private BroadcastReceiver locationBroadcast;
    private MainView view;
    private Context context;
    private int registered = 0;
    private LocateHome locateHome;

    @Inject
    public MainPresenterImp(MainView view, Context context, LocateHome locateHome) {

        this.view = view;
        this.context = context;
        this.locateHome = locateHome;

    }

    @Override
    public void startFetchingData() {

        setupBroadcast();

        context.registerReceiver(locationBroadcast, new IntentFilter("com.location.home.device.CALCULATE"));

        registered = 1;

    }

    @Override
    public void stopFetchingData() {

        if (registered == 1) {

            context.unregisterReceiver(locationBroadcast);
            registered = 0;

        }

    }

    @Override
    public boolean checkForProviders() {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled)
            return false;

        return true;

    }

    private void calculateHouseLocation(String s) {

        locateHome.execute(new LocateHome.Callback() {
            @Override
            public void onHomeLocation(Approximation location) {

                try{

                    view.updateHomeLocation(String.valueOf(location.getLat()),
                                            String.valueOf(location.getLon()));

                } catch (Exception e){}

            }

            @Override
            public void onError() {}

        }, s);

    }

    private void setupBroadcast(){

        locationBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction() == "com.location.home.device.CALCULATE") {

                    double latitude = intent.getDoubleExtra("latitude", 0);
                    double longitude = intent.getDoubleExtra("longitude", 0);

                    calculateHouseLocation(String.valueOf(latitude) + " " + String.valueOf(longitude));

                }

            }
        };

    }

    @Override
    public void onResumeView() {

        calculateHouseLocation(" ");

    }
}
