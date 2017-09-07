package com.location.home.ui.utils;


import android.app.ActivityManager;
import android.content.Context;

public class CheckServiceRunning {

    Context context;

    public CheckServiceRunning(Context context){

        this.context = context;

    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;

            }
        }
        return false;
    }

}
