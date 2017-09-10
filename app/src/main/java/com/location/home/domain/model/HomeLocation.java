package com.location.home.domain.model;

public class HomeLocation {

    private double lat, lon;

    private int points;

    private int weekends;

    public HomeLocation(double lat, double lon, int points, int weekends){

        this.points = points;
        this.lat = lat;
        this.lon = lon;
        this.weekends = weekends;

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getWeekends() {
        return weekends;
    }

    public void setWeekends(int weekends) {
        this.weekends = weekends;
    }

}
