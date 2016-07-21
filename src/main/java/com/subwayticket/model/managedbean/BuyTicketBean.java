package com.subwayticket.model.managedbean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.subwayticket.control.AccountControl;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.model.*;
import com.subwayticket.util.BundleUtil;

/**
 * Created by shenqipingguo on 16-7-2.
 */

@ManagedBean
@ViewScoped
public class BuyTicketBean implements Serializable{
    @EJB
    private SubwayInfoDBHelperBean subwayInfoDBHelperBean;
    private Account user;
    private SubwayStation startStation;
    private SubwayStation endStation;
    private TicketPrice ticketPrice;

    @PostConstruct
    public void init(){
        findUser();
        //startStation = (SubwayStation) subwayInfoDBHelperBean.find(SubwayStation.class, 151);
        //endStation = (SubwayStation) subwayInfoDBHelperBean.find(SubwayStation.class, 1131);
        //ticketPrice = subwayInfoDBHelperBean.getTicketPrice(startStation, endStation);
    }

    public void findUser(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
        if(user != null)
            user = (Account)subwayInfoDBHelperBean.find(Account.class, user.getPhoneNumber());
    }

    public void refreshUser(){
        subwayInfoDBHelperBean.refresh(user);
    }

    public void setStartStationId(String stationIdStr) {
        int stationId = Integer.valueOf(stationIdStr);
        startStation = (SubwayStation) subwayInfoDBHelperBean.find(SubwayStation.class, stationId);
    }

    public void setEndStationId(String stationIdStr){
        int stationId = Integer.valueOf(stationIdStr);
        endStation = (SubwayStation) subwayInfoDBHelperBean.find(SubwayStation.class, stationId);
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

    public TicketPrice searchTicketPrice(SubwayStation startStation, SubwayStation endStation){
        if(startStation == null || endStation == null)
            return null;
        return subwayInfoDBHelperBean.getTicketPrice(startStation, endStation);
    }

    public TicketPrice getTicketPrice() {
        return ticketPrice;
    }

}