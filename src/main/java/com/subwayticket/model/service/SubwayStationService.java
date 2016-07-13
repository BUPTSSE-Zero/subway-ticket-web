package com.subwayticket.model.service;

import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.SubwayStation;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by shenqipingguo on 16-7-11.
 */

@ManagedBean(name="subwayStationService", eager = true)
@ApplicationScoped
public class SubwayStationService {
    @EJB
    SystemDBHelperBean systemDBHelperBean;
    private List<SubwayStation> subwayStations;

    @PostConstruct
    public void init(){
        subwayStations = systemDBHelperBean.findAll(SubwayStation.class);
    }

    public List<SubwayStation> getSubwayStations() {
        return subwayStations;
    }
}