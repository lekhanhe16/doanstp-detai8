package com.kl.doanstp.model;

import java.io.Serializable;

public class TeamLocation implements Serializable {
    private int id;
    private String street, district, city;
    private double latitude, longtitude;

    public TeamLocation() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public TeamLocation(int id, String street, String district, String city, double latitude, double longtitude) {
        this.id = id;
        this.street = street;
        this.district = district;
        this.city = city;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
