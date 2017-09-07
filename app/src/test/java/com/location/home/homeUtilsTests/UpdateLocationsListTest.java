package com.location.home.homeUtilsTests;

import android.test.suitebuilder.annotation.LargeTest;

import com.location.home.domain.calculatehomelocation.utils.UpdateLocationsList;
import com.location.home.domain.model.HomeLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
@LargeTest
public class UpdateLocationsListTest {

    UpdateLocationsList distanceToPoint;

    ArrayList<HomeLocation> locations;

    @Before
    public void setUp() throws Exception{

        String newPoint = "45.4567 23.3456";

        locations = new ArrayList<>();

        addPointsToList();

        distanceToPoint = new UpdateLocationsList(newPoint, locations);

    }

    @Test
    public void testEuclidianDistance(){

        //Medium distance

        double distance = distanceToPoint.haversineDistance(45.4569, 23.3456, 45.4567, 23.3456);

        assertEquals(22, distance, 1);

        //Fairly medium

        distance = distanceToPoint.haversineDistance(45.4569, 23.3458, 45.4567, 23.3456);

        assertEquals(27, distance, 1);

        //Extremely small distance

        distance = distanceToPoint.haversineDistance(45.4569001, 23.3458, 45.4569002, 23.3458);

        assertEquals(0, distance, 1);

        //Huge distance

        distance = distanceToPoint.haversineDistance(46.4569, 26.3458, 45.4567, 23.3456);

        assertEquals(257188, distance, 1);

    }

    @Test
    public void testGetApproximation(){

        ArrayList<HomeLocation> homeLocations = distanceToPoint.getApproximation();

        assertEquals(homeLocations.size(), 3, 0);

        //See if the new point got added only to the first position in the list

        assertEquals(homeLocations.get(0).getPoints(), 10);

        assertEquals(homeLocations.get(1).getPoints(), 1);

        assertEquals(homeLocations.get(2).getPoints(), 5);

    }

    private void addPointsToList(){

        HomeLocation home = new HomeLocation(45.4569, 23.3458, 9);

        locations.add(home);

        home = new HomeLocation(46.4569, 26.3458, 1);

        locations.add(home);

        home = new HomeLocation(45.5000, 23.4000, 5);

        locations.add(home);

    }

}
