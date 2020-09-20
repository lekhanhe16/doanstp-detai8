package com.kl.doanstp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CityDistrict implements Serializable {
    private ArrayList<String> hanoiCity = new ArrayList<>();
    public  ArrayList<String> hcmCity = new ArrayList<>();
    public  ArrayList<String> hueCity = new ArrayList<>();

    public CityDistrict(){
        hanoiCity.add("Tat ca");
        hanoiCity.add("Hoan Kiem");
        hanoiCity.add("Ba Dinh");
        hanoiCity.add("Dong Da");
        hanoiCity.add("Cau Giay");
        hanoiCity.add("Ha Dong");
        hanoiCity.add("Long Bien");

    }

    public ArrayList<String> getHanoiCity() {
        return hanoiCity;
    }
}
