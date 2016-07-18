package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Created by zhou-shengyun on 7/15/16.
 */

@ManagedBean(name = "preferBean")
@ViewScoped
public class PreferBean {
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

    public void refreshUser(){
        systemDBHelperBean.refresh(user);
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
}
