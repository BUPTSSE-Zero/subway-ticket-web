package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.control.TicketOrderDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.request.RefundOrderRequest;
import com.subwayticket.model.result.Result;
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
import java.util.ArrayList;
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

    private Account user;
    private List<TicketOrder> notPayOrders;
    private List<TicketOrder> notExtractTicketOrders;
    private List<TicketOrder> historyTicketOrders;
    private List<TicketOrder> currentTicketOrders;
    private TicketOrder selectedNotExtractTicketOrder;
    private SimpleDateFormat sdf;
    private Date startDate;
    private Date endDate;

    @PostConstruct
    private void init(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        sdf = new SimpleDateFormat("yyyy-MM-dd", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
    }

    public List<TicketOrder> getNotPayOrders(){
        if(notPayOrders == null)
            refreshNotPayOrders();
        currentTicketOrders = notPayOrders;
        return notPayOrders;
    }

    private void sendOrderOperResult(Result result){
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("result_code", result.getResultCode());
        requestContext.addCallbackParam("result_description", result.getResultDescription());
    }

    public void payOrder(){
        FacesContext context = FacesContext.getCurrentInstance();
        String orderId = context.getExternalContext().getRequestParameterMap().get("orderId");
        Result result = TicketOrderControl.payOrder((ServletRequest)context.getExternalContext().getRequest(),
                        systemDBHelperBean, user, new PayOrderRequest(orderId));
        sendOrderOperResult(result);
    }

    public void cancelOrder(){
        FacesContext context = FacesContext.getCurrentInstance();
        String orderId = context.getExternalContext().getRequestParameterMap().get("orderId");
        Result result = TicketOrderControl.cancelOrder((ServletRequest)context.getExternalContext().getRequest(),
                systemDBHelperBean, user, orderId);
        sendOrderOperResult(result);
    }

    public void refreshNotPayOrders(){
        notPayOrders = ticketOrderDBHelperBean.getAllOrderByStatus(TicketOrder.ORDER_STATUS_NOT_PAY, user);
    }

    public List<TicketOrder> getNotExtractTicketOrders() {
        if(notExtractTicketOrders == null)
            refreshNotExtractTicketOrders();
        currentTicketOrders = notExtractTicketOrders;
        return notExtractTicketOrders;
    }

    public void refreshNotExtractTicketOrders(){
        notExtractTicketOrders = ticketOrderDBHelperBean.getAllOrderByStatus(TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET, user);
    }

    public void refundOrder(){
        FacesContext context = FacesContext.getCurrentInstance();
        String orderId = context.getExternalContext().getRequestParameterMap().get("orderId");
        Result result = TicketOrderControl.refundOrder((ServletRequest)context.getExternalContext().getRequest(),
                systemDBHelperBean, user, new RefundOrderRequest(orderId));
        sendOrderOperResult(result);
    }

    public void selectNotExtractTicketOrder(){
        FacesContext context = FacesContext.getCurrentInstance();
        String orderId = context.getExternalContext().getRequestParameterMap().get("orderId");
        if(currentTicketOrders == null || currentTicketOrders.isEmpty()){
            selectedNotExtractTicketOrder = null;
            return;
        }
        for(TicketOrder to : currentTicketOrders){
            if(to.getTicketOrderId().equals(orderId)){
                selectedNotExtractTicketOrder = to;
                return;
            }
        }
        selectedNotExtractTicketOrder = null;
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
        currentTicketOrders = historyTicketOrders;
        return historyTicketOrders;
    }

    public void refreshHistoryTicketOrders() {
        if(startDate == null || endDate == null){
            historyTicketOrders = null;
            return;
        }
        historyTicketOrders = ticketOrderDBHelperBean.getAllOrderByDate(user, startDate, endDate);
    }
}
