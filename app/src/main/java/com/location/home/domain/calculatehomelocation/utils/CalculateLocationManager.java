package com.location.home.domain.calculatehomelocation.utils;

import android.content.Context;

import com.location.home.device.FileManager;
import com.location.home.domain.model.HomeLocation;

import java.util.ArrayList;

public class CalculateLocationManager {

    private ConvertData convert;
    private FileManager manager;

    public CalculateLocationManager(){

        manager = new FileManager();

        convert = new ConvertData();

    }

    public ArrayList<HomeLocation> getLocationsList(String newLocation, Context context){

        ArrayList<HomeLocation> homeLocations = new ArrayList<>();

        String currentLocations = manager.read();

        if (newLocation != " ") {

            homeLocations = updateLocationsList(currentLocations, newLocation, context);

            rewriteData(homeLocations);

        } else {

            homeLocations = convertToList(currentLocations);

        }

        return homeLocations;

    }

    private void rewriteData(ArrayList<HomeLocation> homeLocations) {

        manager.deleteFile();

        manager.write(convert.convertListToString(homeLocations));

    }

    private ArrayList<HomeLocation> updateLocationsList(String currentLocations, String params, Context context) {

        UpdateLocationsList updateLocationsList =
                new UpdateLocationsList(params, convertToList(currentLocations));

        return updateLocationsList.getApproximation(context);

    }

    private ArrayList<HomeLocation> convertToList(String currentLocations) {

        if (currentLocations != null && currentLocations.length() > 0)

            return convert.convertStringToList(currentLocations);

        return new ArrayList<HomeLocation>();

    }

}
