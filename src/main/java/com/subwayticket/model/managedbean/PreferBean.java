package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.*;
import com.subwayticket.util.BundleUtil;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

/**
 * Created by zhou-shengyun on 7/15/16.
 */

@ManagedBean(name = "preferBean")
@ViewScoped
public class PreferBean implements Serializable{
    @EJB
    private SystemDBHelperBean systemDBHelperBean;
    private Account user;

    @PostConstruct
    public void init(){
        findUser();
    }

    public void findUser(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
        if(user != null)
            user = (Account)systemDBHelperBean.find(Account.class, user.getPhoneNumber());
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

    public boolean isRoutePrefer(SubwayStation startStation, SubwayStation endStation){
        if(user == null)
            return false;
        for(PreferRoute pr : user.getPreferRouteList()){
            if(pr.getStartStationId() == startStation.getSubwayStationId() && pr.getEndStationId() == endStation.getSubwayStationId())
                return true;
        }
        return false;
    }

    public void addPreferSubwayStation(SubwayStation ss){
        if(user == null)
            return;
        if(isStationPrefer(ss))
            return;
        systemDBHelperBean.create(new PreferSubwayStation(user.getPhoneNumber(), ss.getSubwayStationId()));
    }

    public void removePreferSubwayStation(SubwayStation ss){
        if(user == null)
            return;
        PreferSubwayStation pss = (PreferSubwayStation) systemDBHelperBean.find(PreferSubwayStation.class,
                new PreferSubwayStationPK(user.getPhoneNumber(), ss.getSubwayStationId()));
        if(pss != null)
            systemDBHelperBean.remove(pss);
    }

    public void addPreferSubwayRoute(SubwayStation startStation, SubwayStation endStation){
        if(user == null)
            return;
        if(isRoutePrefer(startStation, endStation))
            return;
        systemDBHelperBean.create(new PreferRoute(user.getPhoneNumber(), startStation.getSubwayStationId(), endStation.getSubwayStationId()));
    }

    public void removePreferSubwayRoute(SubwayStation startStation, SubwayStation endStation){
        if(user == null)
            return;
        PreferRoute pr = (PreferRoute)systemDBHelperBean.find(PreferRoute.class, new PreferRoutePK(user.getPhoneNumber(),
                startStation.getSubwayStationId(), endStation.getSubwayStationId()));
        if(pr != null)
            systemDBHelperBean.remove(pr);
    }

    public List<HistoryRoute> getHistoryRouteList(){
        return user.getHistoryRouteList();
    }

    public Map<City, List<PreferSubwayStation>> getPreferSubwayStationMap(){
        if(user == null)
            return null;
        Map<City, List<PreferSubwayStation>> result = new HashMap<>();
        for(PreferSubwayStation pss : user.getPreferSubwayStationList()){
            if(!result.containsKey(pss.getSubwayStation().getSubwayLine().getCity())){
                List<PreferSubwayStation> stationList = new ArrayList<>();
                stationList.add(pss);
                result.put(pss.getSubwayStation().getSubwayLine().getCity(), stationList);
            }else{
                List<PreferSubwayStation> stationList = result.get(pss.getSubwayStation().getSubwayLine().getCity());
                stationList.add(pss);
            }
        }
        return result;
    }

    private SubwayStation preferStation;

    public SubwayStation getPreferStation() {
        return preferStation;
    }

    public void setPreferStation(SubwayStation preferStation) {
        this.preferStation = preferStation;
    }

    public void addPreferSubwayStation(){
        addPreferSubwayStation(preferStation);
        RequestContext.getCurrentInstance().addCallbackParam("result_code", 0);
    }

    private SubwayStation preferRouteStartStation;

    public SubwayStation getPreferRouteStartStation() {
        return preferRouteStartStation;
    }

    public void setPreferRouteStartStation(SubwayStation preferRouteStartStation) {
        this.preferRouteStartStation = preferRouteStartStation;
    }

    private SubwayStation preferRouteEndStation;

    public SubwayStation getPreferRouteEndStation() {
        return preferRouteEndStation;
    }

    public void setPreferRouteEndStation(SubwayStation preferRouteEndStation) {
        this.preferRouteEndStation = preferRouteEndStation;
    }

    public void addPreferSubwayRoute(){
        if(preferRouteStartStation.getSubwayStationId() == preferRouteEndStation.getSubwayStationId()){
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, BundleUtil.getString(request, "TipStartStationEqualEndStation"), ""));
            return;
        }
        addPreferSubwayRoute(preferRouteStartStation, preferRouteEndStation);
        RequestContext.getCurrentInstance().addCallbackParam("result_code", 0);
    }

    public Map<City, List<PreferRoute>> getPreferRouteMap(){
        if(user == null)
            return null;
        Map<City, List<PreferRoute>> result = new HashMap<>();
        for(PreferRoute pr : user.getPreferRouteList()){
            if(!result.containsKey(pr.getStartStation().getSubwayLine().getCity())){
                List<PreferRoute> preferRoutes = new ArrayList<>();
                preferRoutes.add(pr);
                result.put(pr.getStartStation().getSubwayLine().getCity(), preferRoutes);
            }else{
                List<PreferRoute> preferRoutes = result.get(pr.getStartStation().getSubwayLine().getCity());
                preferRoutes.add(pr);
            }
        }
        return result;
    }
}
