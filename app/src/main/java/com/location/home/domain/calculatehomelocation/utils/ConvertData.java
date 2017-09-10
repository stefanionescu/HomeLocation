package com.location.home.domain.calculatehomelocation.utils;

import android.util.Log;

import com.location.home.domain.model.HomeLocation;

import java.util.ArrayList;

public class ConvertData {

    public ArrayList<HomeLocation> convertStringToList(String s){

        String[] points = s.split("\n");

        ArrayList<HomeLocation> locations = new ArrayList<>();

        for (int i = 0; i < points.length; i++){

            String[] data = points[i].split(" ");

            try{

                locations.add(new HomeLocation( Double.parseDouble( data[0] ),
                        Double.parseDouble( data[1] ),
                        Integer.parseInt( data[2] ) ,
                        Integer.parseInt( data[3] ) ));

            } catch (Exception e){

                Log.e("conversion", e.getMessage());

            }

        }

        return locations;

    }

    public String convertListToString(ArrayList<HomeLocation> locations){

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < locations.size(); i++){

            builder.append(String.valueOf(locations.get(i).getLat())
                    + " "
                    + String.valueOf(locations.get(i).getLon())
                    + " "
                    + String.valueOf(locations.get(i).getPoints())
                    + " "
                    + String.valueOf(locations.get(i).getWeekends()));

            if (i < locations.size() - 1)

                builder.append("\n");

        }

        return builder.toString();

    }

}
