package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhou-shengyun on 7/15/16.
 */

@ManagedBean(name = "subwayInfoBean")
@ViewScoped
public class SubwayInfoBean implements Serializable {
    @EJB
    private SubwayInfoDBHelperBean subwayInfoDBHelperBean;

    private Account user;
    private List<City> cities;
    private City selectedCity;
    private Map<City, Map<SubwayLine, List<SubwayStation>>> cityStationMap = new HashMap<>();

    @PostConstruct
    private void init(){
        cities = subwayInfoDBHelperBean.findAll(City.class);
        selectedCity = cities.get(0);
        getCityStationMap();
        findUser();
    }

    public void findUser(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
        if(user != null)
            user = (Account)subwayInfoDBHelperBean.find(Account.class, user.getPhoneNumber());
    }

    public void refreshUser(){
        subwayInfoDBHelperBean.refresh(user);
    }

    public List<City> getCities() {
        return cities;
    }

    public int getSelectedCityId() {
        return selectedCity.getCityId();
    }

    public void setSelectedCityId(int cityId) {
        for(City c : cities){
            if(c.getCityId() == cityId)
                selectedCity = c;
        }
    }

    public void onSelectedCityIdChange(){
        getCityStationMap();
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
}
