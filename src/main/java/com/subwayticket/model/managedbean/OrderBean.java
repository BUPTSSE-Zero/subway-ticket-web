package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.control.TicketOrderDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.TicketOrder;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.result.Result;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
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

    @PostConstruct
    private void init(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
    }

    public List<TicketOrder> getNotPayOrders(){
        if(notPayOrders == null)
            refreshNotPayOrders();
        return notPayOrders;
    }

    public void payOrder(){
        FacesContext context = FacesContext.getCurrentInstance();
        String orderId = context.getExternalContext().getRequestParameterMap().get("orderId");
        Result result = TicketOrderControl.payOrder((ServletRequest)context.getExternalContext().getRequest(),
                        systemDBHelperBean, user, new PayOrderRequest(orderId));
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("result_code", result.getResultCode());
        requestContext.addCallbackParam("result_description", result.getResultDescription());
        refreshNotPayOrders();
    }

    public void cancelOrder(){
        FacesContext context = FacesContext.getCurrentInstance();
        String orderId = context.getExternalContext().getRequestParameterMap().get("orderId");
        Result result = TicketOrderControl.cancelOrder((ServletRequest)context.getExternalContext().getRequest(),
                systemDBHelperBean, user, orderId);
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("result_code", result.getResultCode());
        requestContext.addCallbackParam("result_description", result.getResultDescription());
        refreshNotPayOrders();
    }

    public void refreshNotPayOrders(){
        notPayOrders = ticketOrderDBHelperBean.getAllOrderByStatus(TicketOrder.ORDER_STATUS_NOT_PAY, user);
    }
}
