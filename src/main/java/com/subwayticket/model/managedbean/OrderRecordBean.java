package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.control.TicketOrderDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.ExtractTicketRequest;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.request.RefundOrderRequest;
import com.subwayticket.model.result.PayOrderResult;
import com.subwayticket.model.result.RefundOrderResult;
import com.subwayticket.model.result.Result;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shenqipingguo on 16-7-3.
 */

@ManagedBean
@ViewScoped
public class OrderRecordBean implements Serializable {
    private static final char HISTORY_ORDER = 'Z';
    @EJB
    private TicketOrderDBHelperBean ticketOrderDBHelperBean;
    @EJB
    private SystemDBHelperBean systemDBHelperBean;
    private HttpServletRequest request;
    private Account user;
    private List<TicketOrder> NotPayTicketOrderListByDate = new ArrayList<>();
    private List<TicketOrder> NotDrawTicketOrderListByDate = new ArrayList<>();
    private List<TicketOrder> FinishTicketOrderListByDate = new ArrayList<>();
    private List<TicketOrder> RefundTicketOrderListByDate = new ArrayList<>();
    private List<TicketOrder> ticketOrderList = new ArrayList<>();
    private Date startDate;
    private Date endDate;
    private TicketOrder ticketOrder;
    private char orderStatus;
    private String orderId = "";
    private String extractCode = "";
    private int extractAmount;
    private float refundAmount;


    @PostConstruct
    public void init(){
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        user = (Account)request.getSession(false).getAttribute(AccountControl.SESSION_ATTR_USER);
    }

    public List<TicketOrder> getTicketOrderList() {
        return ticketOrderList;
    }

    public List<TicketOrder> getNotDrawTicketOrderListByDate() {
        return NotDrawTicketOrderListByDate;
    }

    public List<TicketOrder> getNotPayTicketOrderListByDate() {
        return NotPayTicketOrderListByDate;
    }

    public List<TicketOrder> getFinishTicketOrderListByDate() {
        return FinishTicketOrderListByDate;
    }

    public List<TicketOrder> getRefundTicketOrderListByDate() {
        return RefundTicketOrderListByDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public TicketOrder getTicketOrder() {
        return ticketOrder;
    }

    public void setTicketOrder(TicketOrder ticketOrder) {
        this.ticketOrder = ticketOrder;
    }

    public float getRefundAmount() {
        return refundAmount;
    }

    public int getExtractAmount() {
        return extractAmount;
    }

    public void setExtractAmount(int extractAmount) {
        this.extractAmount = extractAmount;
    }

    public String getExtractCode() {
        return extractCode;
    }

    public void onDateChange() {
        switch(orderStatus){
            case TicketOrder.ORDER_STATUS_NOT_PAY:NotPayTicketOrderListByDate = ticketOrderDBHelperBean.getAllOrderByStatusAndDate(TicketOrder.ORDER_STATUS_NOT_PAY, user, startDate, endDate);break;
            case TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET:NotDrawTicketOrderListByDate = ticketOrderDBHelperBean.getAllOrderByStatusAndDate(TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET, user, startDate, endDate);break;
            case TicketOrder.ORDER_STATUS_FINISHED:FinishTicketOrderListByDate = ticketOrderDBHelperBean.getAllOrderByStatusAndDate(TicketOrder.ORDER_STATUS_FINISHED, user, startDate, endDate);break;
            case TicketOrder.ORDER_STATUS_REFUNDED:RefundTicketOrderListByDate = ticketOrderDBHelperBean.getAllOrderByStatusAndDate(TicketOrder.ORDER_STATUS_REFUNDED, user, startDate, endDate);break;
            case HISTORY_ORDER: ticketOrderList = ticketOrderDBHelperBean.getAllOrderByDate(user, startDate, endDate);break;
        }
    }

    public void onNotPayStatusClick(){
        endDate = new Date();
        startDate = new Date();
        startDate.setDate(startDate.getDay() - 10);
        NotPayTicketOrderListByDate = ticketOrderDBHelperBean.getAllOrderByStatusAndDate(TicketOrder.ORDER_STATUS_NOT_PAY, user, startDate, endDate);
        orderStatus = TicketOrder.ORDER_STATUS_NOT_PAY;
    }

    public void onNotDrawStatusClick(){
        endDate = new Date();
        startDate = new Date();
        startDate.setDate(startDate.getDay() - 10);
        NotDrawTicketOrderListByDate = ticketOrderDBHelperBean.getAllOrderByStatusAndDate(TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET, user, startDate, endDate);
        orderStatus = TicketOrder.ORDER_STATUS_NOT_EXTRACT_TICKET;
    }

    public void onFinishStatusClick(){
        endDate = new Date();
        startDate = new Date();
        startDate.setDate(startDate.getDay() - 10);
        FinishTicketOrderListByDate = ticketOrderDBHelperBean.getAllOrderByStatusAndDate(TicketOrder.ORDER_STATUS_FINISHED, user, startDate, endDate);
        orderStatus = TicketOrder.ORDER_STATUS_FINISHED;
    }

    public void onRefundStatusClick(){
        endDate = new Date();
        startDate = new Date();
        startDate.setDate(startDate.getDay() - 10);
        RefundTicketOrderListByDate = ticketOrderDBHelperBean.getAllOrderByStatusAndDate(TicketOrder.ORDER_STATUS_REFUNDED, user, startDate, endDate);
        orderStatus = TicketOrder.ORDER_STATUS_REFUNDED;
    }

    public void onHistoryClick(){
        endDate = new Date();
        startDate = user.getRegisterDate();
        ticketOrderList = ticketOrderDBHelperBean.getAllOrderByDate(user, startDate, endDate);
        orderStatus = HISTORY_ORDER;
    }

    public void search(){
        ticketOrder = ticketOrderDBHelperBean.getOrderByOrderID(orderId, user);
    }

    public boolean pay(){
        Result result = TicketOrderControl.payOrder(request, systemDBHelperBean, user, new PayOrderRequest(orderId));
        if (result.getResultCode() == PublicResultCode.SUCCESS) {
            PayOrderResult payOrderResult = (PayOrderResult)result;
            extractCode = payOrderResult.getExtractCode();
            return true;
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    result.getResultDescription(), ""));
            return false;
        }
    }

    public boolean cancel(){
        Result result = TicketOrderControl.cancelOrder(request, systemDBHelperBean, user, orderId);
        if (result.getResultCode() == PublicResultCode.SUCCESS) {
            return true;
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    result.getResultDescription(), ""));
            return false;
        }
    }

    public boolean refund(){
        Result result = TicketOrderControl.refundOrder(request, systemDBHelperBean, user, new RefundOrderRequest(orderId));
        if (result.getResultCode() == PublicResultCode.SUCCESS) {
            RefundOrderResult refundOrderResult = (RefundOrderResult) result;
            refundAmount = refundOrderResult.getRefundAmount();
            return true;
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    result.getResultDescription(), ""));
            return false;
        }
    }

    public boolean extract(){
        Result result = TicketOrderControl.extractTicket(request, ticketOrderDBHelperBean, new ExtractTicketRequest(extractCode, extractAmount));
        if (result.getResultCode() == PublicResultCode.SUCCESS) {
            return true;
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    result.getResultDescription(), ""));
            return false;
        }
    }
}
