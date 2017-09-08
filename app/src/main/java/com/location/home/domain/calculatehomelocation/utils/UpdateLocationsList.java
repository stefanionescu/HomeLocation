package com.location.home.domain.calculatehomelocation.utils;

import com.location.home.domain.model.HomeLocation;

import java.util.ArrayList;

/**
 * Adapted from http://www.movable-type.co.uk/scripts/latlong.html
 */

public class UpdateLocationsList {

    private final int EARTH_RADIUS = 6371000; // < -- meters
    private double minimumDistance = 6401000.00;
    private final int samePointAllowance = 35;

    private ArrayList<HomeLocation> locations;
    private double newLat, newLong;

    public UpdateLocationsList(String newLocation, ArrayList<HomeLocation> locations) {

        this.locations = locations;

        String[] coordinates = newLocation.split(" ");

        newLat = Double.parseDouble(coordinates[0]);

        newLong = Double.parseDouble(coordinates[1]);

    }

    public ArrayList<HomeLocation> getApproximation() {

        int similarPoint = getSimilarPoint();

        if (similarPoint != -1) {

            locations.get(similarPoint).setPoints(locations.get(similarPoint).getPoints() + 1);

        } else
            locations.add(new HomeLocation(newLat, newLong, 1));

        return locations;

    }

    private int getSimilarPoint(){

        int point = -1;

        for (int i = 0; i < locations.size(); i++) {

            double currentDistance = haversineDistance(locations.get(i).getLat(),
                    locations.get(i).getLon(),
                    newLat,
                    newLong);

            if (currentDistance < minimumDistance && currentDistance <= samePointAllowance) {

                minimumDistance = currentDistance;

                point = i;

            }

        }

        return point;

    }

    public double haversineDistance(double startLat, double startLong,
                                    double endLat, double endLong) {

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversineFormula(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversineFormula(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- distance in meters

    }

    private double haversineFormula(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

}
