package com.subwayticket.model.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.subwayticket.control.AccountControl;
import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.*;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.result.PayOrderResult;
import com.subwayticket.model.result.Result;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.SubmitOrderResult;
import com.subwayticket.util.BundleUtil;
import org.primefaces.context.RequestContext;

/**
 * Created by shenqipingguo on 16-7-2.
 */

@ManagedBean
@ViewScoped
public class BuyTicketBean implements Serializable{
    @EJB
    private SubwayInfoDBHelperBean subwayInfoDBHelperBean;
    @EJB
    private SystemDBHelperBean systemDBHelperBean;
    private Account user;
    private HttpServletRequest request;
    private List<City> cities;
    private City selectedCity;
    private Map<City, Map<SubwayLine, List<SubwayStation>>> cityStationMap = new HashMap<>();
    private SubwayStation startStation;
    private SubwayStation endStation;
    private TicketPrice ticketPrice;

    @PostConstruct
    public void init(){
        cities = systemDBHelperBean.findAll(City.class);
        selectedCity = cities.get(0);
        getCityStationMap();
        updateUser();
    }

    public void updateUser(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
        if(user != null)
            user = (Account)systemDBHelperBean.find(Account.class, user.getPhoneNumber());
    }

    public List<City> getCities() {
        return cities;
    }

    private void getCityStationMap(){
        if(cityStationMap.containsKey(selectedCity))
            return;
        Map<SubwayLine, List<SubwayStation>> stationMap = new HashMap<>();
        for(SubwayLine sl : selectedCity.getSubwayLineList()){
            stationMap.put(sl, sl.getSubwayStationList());
        }
        cityStationMap.put(selectedCity, stationMap);
    }

    public List<SubwayStation> searchStation(String queryString){
        List<SubwayStation> result = new ArrayList<>();
        if(queryString == null || queryString.isEmpty()){
            if(user == null)
                return result;
            for(PreferSubwayStation pss : user.getPreferSubwayStationList()){
                if(pss.getSubwayStation().getSubwayLine().getCity().getCityId() == selectedCity.getCityId())
                    result.add(pss.getSubwayStation());
            }
            return result;
        }
        for(Map.Entry<SubwayLine, List<SubwayStation>> e : cityStationMap.get(selectedCity).entrySet()){
            for(SubwayStation ss : e.getValue()){
                if(ss.getDisplayName().toLowerCase().contains(queryString.toLowerCase()))
                    result.add(ss);
                else if(ss.getSubwayStationAbbrName().toLowerCase().contains(queryString.toLowerCase()))
                    result.add(ss);
                else if(ss.getSubwayStationEnglishName().toLowerCase().contains(queryString.toLowerCase()))
                    result.add(ss);
            }
        }
        return result;
    }

    public void setStartStationId(String stationIdStr) {
        int stationId = Integer.valueOf(stationIdStr);
        for(Map.Entry<SubwayLine, List<SubwayStation>> e : cityStationMap.get(selectedCity).entrySet()){
            for(SubwayStation ss : e.getValue()){
                if(ss.getSubwayStationId() == stationId){
                    startStation = ss;
                    return;
                }
            }
        }
    }

    public void setEndStationId(String stationIdStr){
        int stationId = Integer.valueOf(stationIdStr);
        for(Map.Entry<SubwayLine, List<SubwayStation>> e : cityStationMap.get(selectedCity).entrySet()){
            for(SubwayStation ss : e.getValue()){
                if(ss.getSubwayStationId() == stationId){
                    endStation = ss;
                    return;
                }
            }
        }
    }

    public boolean isStationPrefer(SubwayStation ss){
        if(user == null)
            return false;
        for(PreferSubwayStation pss : user.getPreferSubwayStationList()){
            if(pss.getStationId() == ss.getSubwayStationId())
                return true;
        }
        return false;
    }

    public String getStartStationId() {
        return null;
    }

    public String getEndStationId() {
        return null;
    }

    public SubwayStation getStartStation() {
        return startStation;
    }

    public SubwayStation getEndStation() {
        return endStation;
    }

    public void searchTicketPrice(){
        ticketPrice = subwayInfoDBHelperBean.getTicketPrice(startStation, endStation);
        if(ticketPrice == null){
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    BundleUtil.getString(req, "NoSearchResult"), ""));
        }
    }

    public TicketPrice getTicketPrice() {
        return ticketPrice;
    }

    public void addPreferSubwayStation(){
        int stationId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("stationId"));
        systemDBHelperBean.create(new PreferSubwayStation(user.getPhoneNumber(), stationId));
        systemDBHelperBean.refresh(user);
    }
}