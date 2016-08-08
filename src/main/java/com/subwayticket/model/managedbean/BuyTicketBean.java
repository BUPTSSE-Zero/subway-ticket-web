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
 * @author shenqipingguo
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
    }

    public void findUser(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
        if(user != null)
            user = (Account)subwayInfoDBHelperBean.find(Account.class, user.getPhoneNumber());
    }

    public void onSelectedCityChange(){
        SubwayInfoBean subwayInfoBean = (SubwayInfoBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("subwayInfoBean");
        if(startStation != null && subwayInfoBean != null &&
                subwayInfoBean.getSelectedCity().getCityId() == startStation.getSubwayLine().getCity().getCityId()){
            return;
        }
        startStation = endStation = null;
    }

    public SubwayStation getStartStation() {
        return startStation;
    }

    public void setStartStation(SubwayStation startStation) {
        this.startStation = startStation;
    }

    public SubwayStation getEndStation() {
        return endStation;
    }

    public void setEndStation(SubwayStation endStation) {
        this.endStation = endStation;
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