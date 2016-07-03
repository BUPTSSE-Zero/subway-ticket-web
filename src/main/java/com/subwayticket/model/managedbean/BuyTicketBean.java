package com.subwayticket.model.managedbean;

/**
 * Created by shenqipingguo on 16-7-2.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;

import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.*;

@ManagedBean
@ViewScoped
public class BuyTicketBean implements Serializable{
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
    private int number = 1;

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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public void onCityIdChange(){
        if(cityId == 0) {
            subwayLineList = new ArrayList<>();
            startSubwayLineId = 0;
            startSubwayStationId = 0;
            endSubwayStationId = 0;
            endSubwayLineId = 0;
            price = 0;
            return;
        }
        price = 0;
        subwayLineList = subwayInfoDBHelperBean.getSubwayLineList(cityId);
    }

    public void onStartSubwayLineIdChange(){
        if(startSubwayLineId == 0){
            startSubwayStationList = new ArrayList<>();
            startSubwayStationId = 0;
            price = 0;
            return;
        }
        price = 0;
        startSubwayStationList = subwayInfoDBHelperBean.getSubwayStationList(startSubwayLineId);
    }

    public void onEndSubwayLineIdChange(){
        if(endSubwayLineId == 0){
            endSubwayStationList = new ArrayList<>();
            endSubwayStationId = 0;
            price = 0;
            return;
        }
        price = 0;
        endSubwayStationList = subwayInfoDBHelperBean.getSubwayStationList(endSubwayLineId);
    }

    public void onSubwayStationIdChange(){
        if(endSubwayStationId!=0 && startSubwayStationId!=0){
            price = subwayInfoDBHelperBean.getTicketPrice(startSubwayStationId, endSubwayStationId).getPrice();
            return;
        }
        price = 0;
    }

    public void confirmAction(ActionEvent actionEvent) {

    }
}