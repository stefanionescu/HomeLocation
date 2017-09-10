package com.location.home.ui.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;

import com.location.home.domain.gethomelocation.FetchHome;
import com.location.home.domain.model.Approximation;
import com.location.home.executor.reactive.DefaultObserver;
import com.location.home.ui.views.MainView;

import javax.inject.Inject;

public class MainPresenterImp implements MainPresenter {

    private BroadcastReceiver locationBroadcast;
    private MainView view;
    private Context context;
    private int registered = 0;
    private FetchHome fetchHome;

    @Inject
    public MainPresenterImp(MainView view, Context context, FetchHome fetchHome) {

        this.view = view;
        this.context = context;
        this.fetchHome = fetchHome;

    }

    @Override
    public void startFetchingData() {

        setupBroadcast();

        context.registerReceiver(locationBroadcast, prepareFilter());

        registered = 1;

    }

    @Override
    public void stopFetchingData() {

        if (registered == 1) {

            context.unregisterReceiver(locationBroadcast);
            registered = 0;

        }

    }

    private void getHouseLocation() {

        fetchHome.execute(new UserListObserver(), null);

    }

    @Override
    public void onResumeView() {

        getHouseLocation();

    }

    private void setupBroadcast() {

        locationBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals("com.location.home.device.GET_LOCATION")) {

                    getHouseLocation();

                } else if (intent.getAction().equals("com.location.home.device.STOPPED_GETTING_LOCATION")) {

                    try {

                        view.changeButtonToStart();

                    } catch (Exception e) {
                    }

                }

            }
        };

    }

    private final class UserListObserver extends DefaultObserver<Approximation> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(Approximation homeLocation) {

            if (homeLocation.getLat() != Math.PI) {

                try {

                    view.updateHomeLocation(String.valueOf(homeLocation.getLat()),
                            String.valueOf(homeLocation.getLon()));

                } catch (Exception e) {
                }

            }

        }
    }

    private IntentFilter prepareFilter(){

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.location.home.device.GET_LOCATION");
        filter.addAction("com.location.home.device.STOPPED_GETTING_LOCATION");

        return filter;

    }

}
