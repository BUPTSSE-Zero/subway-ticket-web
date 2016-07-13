package com.subwayticket.model.service;

import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.City;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by shenqipingguo on 16-7-11.
 */

@ManagedBean(name="cityService", eager = true)
@ApplicationScoped
public class CityService {
    @EJB
    SystemDBHelperBean systemDBHelperBean;
    private List<City> cities;

    @PostConstruct
    public void init(){
        cities = systemDBHelperBean.findAll(City.class);
    }

    public List<City> getCities() {
        return cities;
    }
}
