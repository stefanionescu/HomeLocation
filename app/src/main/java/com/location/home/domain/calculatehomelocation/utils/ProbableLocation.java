package com.location.home.domain.calculatehomelocation.utils;

import com.location.home.domain.model.Approximation;
import com.location.home.domain.model.HomeLocation;

import java.util.ArrayList;

public class ProbableLocation {

    private int minimum = -1;
    private int probablePosition = -1;

    public Approximation checkIfDominantExists(ArrayList<HomeLocation> locations){

        for (int i = 0; i < locations.size(); i++){

            if (locations.get(i).getPoints() >= 10 && locations.get(i).getPoints() > minimum){

                probablePosition = i;
                minimum = locations.get(i).getPoints();

            }

        }

        if (probablePosition > -1)
            return new Approximation(locations.get(probablePosition).getLat(),
                        locations.get(probablePosition).getLon());

        return null;

    }

}
