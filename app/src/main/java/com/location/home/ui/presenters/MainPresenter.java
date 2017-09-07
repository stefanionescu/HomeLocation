package com.location.home.ui.presenters;

import com.location.home.ui.views.MainView;

public interface MainPresenter extends Presenter<MainView>{

    void startFetchingData();

    void stopFetchingData();

    boolean checkForProviders();

}
