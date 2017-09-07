package com.location.home.homeUtilsTests;

import com.location.home.domain.calculatehomelocation.utils.ProbableLocation;
import com.location.home.domain.model.Approximation;
import com.location.home.domain.model.HomeLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ProbableLocationTest {

    private ProbableLocation probableLocation;
    private ArrayList<HomeLocation> locations;

    @Before
    public void setUp() throws Exception{

        probableLocation = new ProbableLocation();

        addPointsToList();

    }

    @Test
    public void testCheckDominant(){

        Approximation approximation = probableLocation.checkIfDominantExists(locations);

        assertEquals(approximation.getLat(), 25.4569, 0);

        assertEquals(approximation.getLon(), 23.3458, 0);

    }

    @Test
    public void testNullScenario(){

        locations = new ArrayList<>();

        locations.add(new HomeLocation(45.4569, 23.3458, 1));

        assertNull(probableLocation.checkIfDominantExists(locations));

    }

    private void addPointsToList(){

        locations = new ArrayList<>();

        HomeLocation home = new HomeLocation(45.4569, 23.3458, 10);

        locations.add(home);

        home = new HomeLocation(46.4569, 26.3458, 1);

        locations.add(home);

        home = new HomeLocation(25.4569, 23.3458, 11);

        locations.add(home);

        home = new HomeLocation(45.5000, 23.4000, 5);

        locations.add(home);

    }

}
