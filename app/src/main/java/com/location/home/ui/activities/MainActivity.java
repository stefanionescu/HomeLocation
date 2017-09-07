package com.location.home.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.location.home.R;
import com.location.home.app.HomeLocationApplication;
import com.location.home.app.di.components.DaggerMainComponent;
import com.location.home.app.di.modules.MainPresenterModule;
import com.location.home.device.GpsService;
import com.location.home.ui.presenters.MainPresenter;
import com.location.home.ui.utils.AskForPermission;
import com.location.home.ui.utils.CheckServiceRunning;
import com.location.home.ui.views.MainView;
import com.melnykov.fab.FloatingActionButton;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.home_coordinate_north)
    TextView homeNorth;

    @BindView(R.id.home_coordinate_east)
    TextView homeEast;

    @BindView(R.id.get_coordinates)
    FloatingActionButton getCoordinates;

    @Inject
    MainPresenter presenter;

    AskForPermission askPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }

    @Override
    public void onStart() {

        super.onStart();

        askPermission = new AskForPermission(this);

        askPermission.askGpsPermission();

    }

    @Override
    public void onResume() {

        super.onResume();

        inject();

        boolean serviceRunning = new CheckServiceRunning(this)
                .isMyServiceRunning(GpsService.class);

        if (serviceRunning) changeButtonToStop();

        else changeButtonToStart();

        presenter.onResumeView();

        presenter.startFetchingData();

    }

    @Override
    public void onPause() {

        super.onPause();

        presenter.stopFetchingData();

        askPermission = null;

    }

    @Override
    public void onStop() {

        askPermission = null;

        presenter.stopFetchingData();

        super.onStop();

    }

    @Override
    public void updateHomeLocation(String lat, String lon) {

        homeNorth.setText(lat + " N");
        homeEast.setText(lon + " E");

    }

    private void inject() {

        DaggerMainComponent.builder()
                .applicationComponent(((HomeLocationApplication) getApplication()).getApplicationComponent())
                .mainPresenterModule(new MainPresenterModule(this, getApplicationContext()))
                .build()
                .inject(this);

    }

    @OnClick(R.id.get_coordinates)
    public void clickedGetCoordinates() {

        if (!presenter.checkForProviders()) {

            askForProviders();

        } else {

            changeButtonState();

        }

    }

    @Override
    public void askForProviders() {

        new LovelyStandardDialog(this)

                .setTopColorRes(R.color.blue)
                .setButtonsColorRes(R.color.blue_light)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.please_enable_gps)
                .setMessage(R.string.enable_gps_message)

                .setPositiveButton(R.string.done, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!presenter.checkForProviders())
                            askForProviders();

                    }
                })

                .show();

    }

    @Override
    public void showToast(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

    }

    @Override
    public void changeButtonToStop() {

        getCoordinates.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));

    }

    @Override
    public void changeButtonToStart(){

        getCoordinates.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {

                for (int i = 0; i < grantResults.length; i++) {

                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                        exitWithoutPermissions();

                    }

                }

                if (grantResults.length == 0) {

                    exitWithoutPermissions();

                }

            }

        }
    }

    private void changeButtonState() {

        Drawable drawable = getCoordinates.getDrawable();

        try {

            if (drawable.getConstantState().
                    equals(getResources().getDrawable(R.drawable.ic_action_play)
                            .getConstantState())) {

                getCoordinates.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));

                startService(new Intent(this, GpsService.class));


            } else {

                getCoordinates.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));

                stopService(new Intent(this, GpsService.class));

            }

        } catch (NullPointerException e) {

            Log.e("drawable", e.getMessage());

        }

    }

    private void exitWithoutPermissions() {

        Toast.makeText(getApplicationContext(),
                "Sorry, we need these permissions to continue",
                Toast.LENGTH_LONG).show();

        finish();

    }

}
