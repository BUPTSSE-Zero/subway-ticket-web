package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemMessageDBHelperBean;
import com.subwayticket.database.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

/**
 * Created by zhou-shengyun on 7/15/16.
 */

@ManagedBean(name = "subwayInfoBean")
@ViewScoped
public class SubwayInfoBean implements Serializable {
    @EJB
    private SubwayInfoDBHelperBean subwayInfoDBHelperBean;
    @EJB
    private SystemMessageDBHelperBean sysMsgDBBean;

    private Account user;
    private List<City> cities;
    private City selectedCity;
    private Map<City, Map<SubwayLine, List<SubwayStation>>> cityStationMap = new HashMap<>();

    @PostConstruct
    private void init(){
        cities = subwayInfoDBHelperBean.findAll(City.class);
        findUser();
        if(user == null || selectedCity == null) {
            selectedCity = cities.get(0);
            generateCityStationMap();
        }
    }

    public void findUser(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
        if(user != null) {
            user = (Account) subwayInfoDBHelperBean.find(Account.class, user.getPhoneNumber());
            if(!user.getHistoryRouteList().isEmpty()){
                if(selectedCity == null || user.getHistoryRouteList().get(0).getStartStation().getSubwayLine().getCity().getCityId() != selectedCity.getCityId()) {
                    selectedCity = user.getHistoryRouteList().get(0).getStartStation().getSubwayLine().getCity();
                    generateCityStationMap();
                }
            }
        }
    }

    public List<City> getCities() {
        return cities;
    }

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }

    public void onSelectedCityChange(){
        generateCityStationMap();
    }

    private void generateCityStationMap(){
        if(cityStationMap.containsKey(selectedCity))
            return;
        Map<SubwayLine, List<SubwayStation>> stationMap = new LinkedHashMap<>();
        for(SubwayLine sl : selectedCity.getSubwayLineList()){
            stationMap.put(sl, sl.getSubwayStationList());
        }
        cityStationMap.put(selectedCity, stationMap);
    }

    public Map<City, Map<SubwayLine, List<SubwayStation>>> getCityStationMap() {
        return cityStationMap;
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

    public List<SystemMessage> getSystemMessageList(){
        return sysMsgDBBean.getLatestMessage(10);
    }

    private SystemMessage selectedSystemMessage;

    public SystemMessage getSelectedSystemMessage() {
        return selectedSystemMessage;
    }

    public void setSelectedSystemMessage(SystemMessage selectedSystemMessage) {
        this.selectedSystemMessage = selectedSystemMessage;
    }
}
