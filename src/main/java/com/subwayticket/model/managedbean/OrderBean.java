package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.control.TicketOrderDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.SubwayStation;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.database.model.TicketPrice;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.request.RefundOrderRequest;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.Result;
import com.subwayticket.model.result.SubmitOrderResult;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhou-shengyun on 7/9/16.
 */

@ManagedBean(name = "orderBean")
@ViewScoped
public class OrderBean implements Serializable {
    @EJB
    private TicketOrderDBHelperBean ticketOrderDBHelperBean;
    @EJB
    private SystemDBHelperBean systemDBHelperBean;
    @EJB
    private SubwayInfoDBHelperBean subwayInfoDBHelperBean;

    private Account user;
    private List<TicketOrder> notPayOrders;
    private List<TicketOrder> notExtractTicketOrders;
    private List<TicketOrder> historyTicketOrders;
    private TicketOrder selectedNotExtractTicketOrder;
    private SimpleDateFormat sdf;
    private Date startDate;
    private Date endDate;

    private TicketOrder submitedOrder;

    @PostConstruct
    public void init(){
        sdf = new SimpleDateFormat("yyyy-MM-dd", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        findUser();
    }

    public void findUser(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
        if(user != null)
            user = (Account)systemDBHelperBean.find(Account.class, user.getPhoneNumber());
    }

    public List<TicketOrder> getNotPayOrders(){
        if(notPayOrders == null)
            refreshNotPayOrders();
        return notPayOrders;
    }

    private void sendOrderOperResult(Result result){
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("result_code", result.getResultCode());
        requestContext.addCallbackParam("result_description", result.getResultDescription());
    }

    public void payOrder(TicketOrder order){
        FacesContext context = FacesContext.getCurrentInstance();
        Result result = TicketOrderControl.payOrder((ServletRequest)context.getExternalContext().getRequest(),
                        systemDBHelperBean, user, new PayOrderRequest(order.getTicketOrderId()));
        sendOrderOperResult(result);
    }

    public void cancelOrder(TicketOrder order){
        FacesContext context = FacesContext.getCurrentInstance();
        Result result = TicketOrderControl.cancelOrder((ServletRequest)context.getExternalContext().getRequest(),
                systemDBHelperBean, user, order.getTicketOrderId());
        if(submitedOrder != null && submitedOrder.getTicketOrderId().equals(order.getTicketOrderId())
                && result.getResultCode() == PublicResultCode.SUCCESS){
            submitedOrder = null;
        }
        sendOrderOperResult(result);
    }

    public void refreshNotPayOrders(){
        notPayOrders = ticketOrderDBHelperBean.getAllOrderByStatus(TicketOrder.ORDER_STATUS_NOT_PAY, user);
    }

    public List<TicketOrder> getNotExtractTicketOrders() {
        if(notExtractTicketOrders == null)
            refreshNotExtractTicketOrders();
        return notExtractTicketOrders;
    }

    public void refreshNotExtractTicketOrders(){
        notExtractTicketOrders = ticketOrderDBHelperBean.getAllOrderByStatus(TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET, user);
    }

    public void refundOrder(TicketOrder order){
        FacesContext context = FacesContext.getCurrentInstance();
        Result result = TicketOrderControl.refundOrder((ServletRequest)context.getExternalContext().getRequest(),
                systemDBHelperBean, user, new RefundOrderRequest(order.getTicketOrderId()));
        sendOrderOperResult(result);
    }

    public void selectNotExtractTicketOrder(TicketOrder to){
        selectedNotExtractTicketOrder = to;
    }

    public TicketOrder getSelectedNotExtractTicketOrder() {
        return selectedNotExtractTicketOrder;
    }

    private String getCurrentDate(){
        return sdf.format(new Date());
    }

    public String getStartDate(){
        return getCurrentDate();
    }

    public void setStartDate(String dateStr){
        try {
            startDate = sdf.parse(dateStr);
        }catch (ParseException pe){
            pe.printStackTrace();
            startDate = null;
        }
    }

    public String getEndDate(){
        return getCurrentDate();
    }

    public void setEndDate(String dateStr){
        try {
            endDate = sdf.parse(dateStr);
        }catch (ParseException pe){
            pe.printStackTrace();
            endDate = null;
        }
    }

    public List<TicketOrder> getHistoryTicketOrders() {
        return historyTicketOrders;
    }

    public void refreshHistoryTicketOrders() {
        if(startDate == null || endDate == null){
            historyTicketOrders = null;
            return;
        }
        historyTicketOrders = ticketOrderDBHelperBean.getAllOrderByDate(user, startDate, endDate);
    }

    public TicketOrder getSubmitedOrder() {
        return submitedOrder;
    }

    public void setSubmitedOrder(TicketOrder submitedOrder) {
        this.submitedOrder = submitedOrder;
    }

    public void submitOrder(SubwayStation startStation, SubwayStation endStation, TicketPrice tp){
        submitedOrder = null;
        FacesContext context = FacesContext.getCurrentInstance();
        int buyTicketAmount = Integer.valueOf(context.getExternalContext().getRequestParameterMap().get("ticketAmount"));
        Result result = TicketOrderControl.submitOrder((ServletRequest)context.getExternalContext().getRequest(),
                subwayInfoDBHelperBean, user, new SubmitOrderRequest(startStation.getSubwayStationId(), endStation.getSubwayStationId(), buyTicketAmount));
        sendOrderOperResult(result);
        if(result.getResultCode() == PublicResultCode.SUCCESS){
            submitedOrder = ((SubmitOrderResult)result).getTicketOrder();
        }
    }
}
