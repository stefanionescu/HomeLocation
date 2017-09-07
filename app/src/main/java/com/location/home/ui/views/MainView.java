package com.location.home.ui.views;

public interface MainView extends View{

    void updateHomeLocation(String lat, String lon);

    void askForProviders();

    void showToast(String s);

    void changeButtonToStart();

    void changeButtonToStop();

}
