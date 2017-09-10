package com.location.home.device;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {

    private SharedPreferences prefs;
    private Context context;

    public SharedPrefsManager(Context context) {

        this.context = context;

        prefs = context.getSharedPreferences(
                "com.location.home.device", Context.MODE_PRIVATE);


    }

    public void writeWeekend(String key, String day) {

        prefs.edit().putString(key, day).apply();

    }

    //Everyone likes Christmas. Right?

    public String getWeekend(String key){

        return prefs.getString(key, "Christmas");

    }

}
