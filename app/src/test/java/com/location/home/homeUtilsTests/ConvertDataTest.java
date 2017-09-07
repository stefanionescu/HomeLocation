package com.location.home.homeUtilsTests;

import com.location.home.domain.calculatehomelocation.utils.ConvertData;
import com.location.home.domain.model.HomeLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ConvertDataTest {

    private ConvertData convertData;
    private String locations;
    private ArrayList<HomeLocation> convertedList;

    @Before
    public void setUp() throws Exception{

        convertData = new ConvertData();
        convertedList = new ArrayList<>();

        locations = "45.4569 23.3458 10" +
                "\n" +
                "46.4569 26.3458 1" +
                "\n" +
                "45.5 23.4 5";

        convertedList = convertData.convertStringToList(locations);

    }

    @Test
    public void testConvertStringToList(){

        //First element

        assertEquals(convertedList.get(0).getLat(), 45.4569, 0);
        assertEquals(convertedList.get(0).getLon(), 23.3458, 0);
        assertEquals(convertedList.get(0).getPoints(), 10, 0);

        //Second element

        assertEquals(convertedList.get(1).getLat(), 46.4569, 0);
        assertEquals(convertedList.get(1).getLon(), 26.3458, 0);
        assertEquals(convertedList.get(1).getPoints(), 1, 0);

        //Third element

        assertEquals(convertedList.get(2).getLat(), 45.5000, 0);
        assertEquals(convertedList.get(2).getLon(), 23.4000, 0);
        assertEquals(convertedList.get(2).getPoints(), 5, 0);

    }

    @Test
    public void testListToString(){

        String test = convertData.convertListToString(convertedList);

        assertEquals(test, locations);

    }

}
