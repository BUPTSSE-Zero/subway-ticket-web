package com.subwayticket.model.result;

import com.subwayticket.database.model.City;

import java.util.List;

/**
 * Created by shengyun-zhou on 6/16/16.
 */
public class CityListResult extends Result {
    private List<City> cityList;

    public CityListResult(){}

    public CityListResult(int resultCode, String resultDescription, List<City> cityList) {
        super(resultCode, resultDescription);
        this.cityList = cityList;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
