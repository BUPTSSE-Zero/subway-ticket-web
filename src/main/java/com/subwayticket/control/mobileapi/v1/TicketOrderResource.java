package com.subwayticket.control.mobileapi.v1;

import com.subwayticket.control.TicketOrderControl;
import com.subwayticket.control.mobileapi.CheckException;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.SubmitOrderRequest;
import com.subwayticket.model.result.Result;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
}
