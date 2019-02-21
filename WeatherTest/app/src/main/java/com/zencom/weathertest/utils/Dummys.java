package com.zencom.weathertest.utils;

import com.zencom.weathertest.pojo.City;

import java.util.ArrayList;
import java.util.List;

public class Dummys {

    public static final List<City> cityList = new ArrayList<>();

    static {
        cityList.add(new City(Cons.CORDOBA_NAME, Cons.CORDOBA[0], Cons.CORDOBA[1]));
        cityList.add(new City(Cons.BUENOS_AIRES_NAME, Cons.BUENOS_AIRES[0], Cons.BUENOS_AIRES[1]));
        cityList.add(new City(Cons.LONDRES_NAME, Cons.LONDRES[0], Cons.LONDRES[1]));
        cityList.add(new City(Cons.NEW_YORK_NAME, Cons.NEW_YORK[0], Cons.NEW_YORK[1]));
        cityList.add(new City(Cons.MOSCU_NAME, Cons.MOSCU[0], Cons.MOSCU[1]));
    }

}
