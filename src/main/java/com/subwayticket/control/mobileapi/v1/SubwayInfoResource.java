package com.subwayticket.control.mobileapi.v1;

import com.subwayticket.control.mobileapi.CheckException;
import com.subwayticket.database.control.SubwayInfoDBHelperBean;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.*;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.result.*;
import com.subwayticket.util.BundleUtil;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * RESTful API-查询地铁信息
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */

@Path("/v1/subway")
public class SubwayInfoResource {
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    @EJB
    private SystemDBHelperBean dbBean;
    @EJB
    private SubwayInfoDBHelperBean subwayInfoDBBean;

    @GET
    @Path("/city")
    @Produces("application/json")
    public CityListResult getCityList(){
        List<City> cityList = dbBean.findAll(City.class);
        if(cityList == null || cityList.isEmpty())
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.RESULT_NOT_FOUND, BundleUtil.getString(request, "TipResultNotFound")));
        return new CityListResult(PublicResultCode.SUCCESS, "", cityList);
    }

    @GET
    @Path("/line/{cityId}")
    @Produces("application/json")
    public SubwayLineListResult getSubwayLineList(@PathParam("cityId") int cityId){
        List<SubwayLine> subwayLineList = subwayInfoDBBean.getSubwayLineList(cityId);
        if(subwayLineList == null || subwayLineList.isEmpty())
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.RESULT_NOT_FOUND, BundleUtil.getString(request, "TipResultNotFound")));
        return new SubwayLineListResult(PublicResultCode.SUCCESS, "", subwayLineList);
    }

    @GET
    @Path("/station/{subwayLineId}")
    @Produces("application/json")
    public SubwayStationListResult getSubwayStationList(@PathParam("subwayLineId") int subwayLineId){
        List<SubwayStation> subwayStationList = subwayInfoDBBean.getSubwayStationList(subwayLineId);
        if(subwayStationList == null || subwayStationList.isEmpty())
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.RESULT_NOT_FOUND, BundleUtil.getString(request, "TipResultNotFound")));
        return new SubwayStationListResult(PublicResultCode.SUCCESS, "", subwayStationList);
    }

    @GET
    @Path("/ticket_price/{startStationId}/{endStationId}")
    @Produces("application/json")
    public TicketPriceResult getTicketPrice(@PathParam("startStationId") int startStationId, @PathParam("endStationId") int endStationId){
        TicketPrice ticketPrice = subwayInfoDBBean.getTicketPrice(startStationId, endStationId);
        if(ticketPrice == null)
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.RESULT_NOT_FOUND, BundleUtil.getString(request, "TipResultNotFound")));
        return new TicketPriceResult(PublicResultCode.SUCCESS, "", ticketPrice.getPrice());
    }
}
