package com.subwayticket.control.mobileapi.v1;

import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.control.mobileapi.CheckException;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.PayOrderRequest;
import com.subwayticket.model.request.RefundOrderRequest;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.Result;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by zhou-shengyun on 7/4/16.
 */

@Path("/v1/ticket_order")
public class TicketOrderResource {
    @Context
    private HttpServletRequest request;
    @EJB
    private SystemDBHelperBean dbBean;
    @EJB
    private SubwayInfoDBHelperBean subwayInfoDBBean;


    @POST
    @Path("/submit")
    @Consumes("application/json")
    public Response submitOrder(SubmitOrderRequest submitOrderRequest){
        Account user = AccountResource.authCheck(request);
        Result result = TicketOrderControl.submitOrder(request, dbBean, subwayInfoDBBean, user, submitOrderRequest);
        if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @DELETE
    @Path("/cancel/{orderId}")
    @Consumes("application/json")
    public Response cancelOrder(@PathParam("orderId") String orderId){
        Account user = AccountResource.authCheck(request);
        Result result = TicketOrderControl.cancelOrder(request, dbBean, user, orderId);
        if(result.getResultCode() == PublicResultCode.ORDER_CANCEL_ORDER_NOT_EXIST)
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(), result);
        else if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @PUT
    @Path("/pay")
    @Consumes("application/json")
    public Response payOrder(PayOrderRequest payOrderRequest){
        Account user = AccountResource.authCheck(request);
        Result result = TicketOrderControl.payOrder(request, dbBean, user, payOrderRequest);
        if(result.getResultCode() == PublicResultCode.ORDER_PAY_ORDER_NOT_EXIST)
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(), result);
        else if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @PUT
    @Path("/refund")
    @Consumes("application/json")
    public Response refundOrder(RefundOrderRequest refundOrderRequest){
        Account user = AccountResource.authCheck(request);
        Result result = TicketOrderControl.refundOrder(request, dbBean, user, refundOrderRequest);
        if(result.getResultCode() == PublicResultCode.ORDER_REFUND_ORDER_NOT_EXIST)
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(), result);
        else if(result.getResultCode() != PublicResultCode.SUCCESS)
            throw new CheckException(result);
        return Response.status(Response.Status.CREATED).entity(result).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
