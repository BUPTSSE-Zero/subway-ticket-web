package com.subwayticket.model.managedbean;

/**
 * Created by shenqipingguo on 16-7-2.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.ejb.EJB;

import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.*;

@ManagedBean
@ViewScoped
public class BuyTicketBean {
    @EJB
    private SubwayInfoDBHelperBean subwayInfoDBHelperBean;
    @EJB
    private SystemDBHelperBean systemDBHelperBean;

    private int cityId = 0;
    private int startSubwayLineId = 0;
    private int endSubwayLineId = 0;
    private int startSubwayStationId = 0;
    private int endSubwayStationId = 0;
    private List<City> cities;
    private List<SubwayLine> subwayLineList = new ArrayList<>();
    private List<SubwayStation> startSubwayStationList = new ArrayList<>();
    private List<SubwayStation> endSubwayStationList = new ArrayList<>();
    private float price = 0;

    @PostConstruct
    public void init(){
        cities = systemDBHelperBean.findAll(City.class);
    }

    public List<City> getCities() {
        return cities;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getStartSubwayLineId() {
        return startSubwayLineId;
    }

    public void setStartSubwayLineId(int startSubwayLineId) {
        this.startSubwayLineId = startSubwayLineId;
    }

    public int getEndSubwayLineId() {
        return endSubwayLineId;
    }

    public void setEndSubwayLineId(int endSubwayLineId) {
        this.endSubwayLineId = endSubwayLineId;
    }

    public int getEndSubwayStationId() {
        return endSubwayStationId;
    }

    public void setEndSubwayStationId(int endSubwayStationId) {
        this.endSubwayStationId = endSubwayStationId;
    }

    public int getStartSubwayStationId() {
        return startSubwayStationId;
    }

    public void setStartSubwayStationId(int startSubwayStationId) {
        this.startSubwayStationId = startSubwayStationId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void onCityIdChange(){
        if(cityId == 0) {
            subwayLineList = new ArrayList<>();
            startSubwayLineId = 0;
            startSubwayStationId = 0;
            endSubwayStationId = 0;
            endSubwayLineId = 0;
            return;
        }
        subwayLineList = subwayInfoDBHelperBean.getSubwayLineList(cityId);
    }

    public void onStartSubwayLineIdChange(){
        if(startSubwayLineId == 0){
            startSubwayStationList = new ArrayList<>();
            startSubwayStationId = 0;
            return;
        }
        startSubwayStationList = subwayInfoDBHelperBean.getSubwayStationList(startSubwayLineId);
    }

    public void onEndSubwayLineIdChange(){
        if(endSubwayLineId == 0){
            endSubwayStationList = new ArrayList<>();
            endSubwayStationId = 0;
            return;
        }
        endSubwayStationList = subwayInfoDBHelperBean.getSubwayStationList(endSubwayLineId);
    }

    public void onSubwayStationIdChange(){
        if(endSubwayStationId!=0 && startSubwayStationId!=0){
            price = subwayInfoDBHelperBean.getTicketPrice(startSubwayStationId, endSubwayStationId).getPrice();
        }
    }

    public List<SubwayLine> getSubwayLineList() {
        return subwayLineList;
    }

    public List<SubwayStation> getStartSubwayStationList() {
        return startSubwayStationList;
    }

    public List<SubwayStation> getEndSubwayStationList() {
        return endSubwayStationList;
    }
}
