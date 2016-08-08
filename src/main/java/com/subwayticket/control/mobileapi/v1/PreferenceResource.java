package com.subwayticket.control.mobileapi.v1;

import com.subwayticket.control.mobileapi.CheckException;
import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.*;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.AddPreferRouteRequest;
import com.subwayticket.model.request.AddPreferStationRequest;
import com.subwayticket.model.result.HistoryRouteListResult;
import com.subwayticket.model.result.PreferRouteListResult;
import com.subwayticket.model.result.PreferStationListResult;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.BundleUtil;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * RESTful API-常用设置
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */

@Path("/v1/preference")
public class PreferenceResource {
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    @EJB
    private SystemDBHelperBean dbBean;

    @GET
    @Path("/history_route")
    public HistoryRouteListResult getHistoryRoutes(){
        Account user = AccountResource.authCheck(request);
        user = (Account)dbBean.find(Account.class, user.getPhoneNumber());
        if(user.getHistoryRouteList() == null || user.getHistoryRouteList().isEmpty()) {
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.PREFER_RECORD_NOT_FOUND, BundleUtil.getString(request, "TipNoHistoryRoute")));
        }
        return new HistoryRouteListResult(PublicResultCode.SUCCESS, "", user.getHistoryRouteList());
    }

    @GET
    @Path("/prefer_station")
    public PreferStationListResult getPreferStationList(){
        Account user = AccountResource.authCheck(request);
        user = (Account)dbBean.find(Account.class, user.getPhoneNumber());
        if(user.getPreferSubwayStationList() == null || user.getPreferSubwayStationList().isEmpty()) {
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.PREFER_RECORD_NOT_FOUND, BundleUtil.getString(request, "TipNoPreferSubwayStation")));
        }
        return new PreferStationListResult(PublicResultCode.SUCCESS, "", user.getPreferSubwayStationList());
    }

    @POST
    @Path("/prefer_station/add")
    @Consumes("application/json")
    public Response addPreferStation(AddPreferStationRequest apsr){
        Account user = AccountResource.authCheck(request);
        if(dbBean.find(PreferSubwayStation.class, new PreferSubwayStationPK(user.getPhoneNumber(), apsr.getStationId())) != null){
            throw new CheckException(new Result(PublicResultCode.PREFER_RECORD_EXISTED, BundleUtil.getString(request, "TipPreferStationExisted")));
        }
        dbBean.create(new PreferSubwayStation(user.getPhoneNumber(), apsr.getStationId()));
        return Response.status(Response.Status.CREATED).entity(new Result(PublicResultCode.SUCCESS, BundleUtil.getString(request, "TipAddPreferStationSuccess"))).
                type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @DELETE
    @Path("/prefer_station/remove/{stationId}")
    public Result removePreferStation(@PathParam("stationId") int stationId){
        Account user = AccountResource.authCheck(request);
        PreferSubwayStation pss = (PreferSubwayStation)dbBean.find(PreferSubwayStation.class, new PreferSubwayStationPK(user.getPhoneNumber(), stationId));
        if(pss == null){
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.PREFER_RECORD_NOT_EXIST, BundleUtil.getString(request, "TipPreferStationNotExist")));
        }
        dbBean.remove(pss);
        return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(request, "TipRemovePreferStationSuccess"));
    }

    @GET
    @Path("/prefer_route")
    public PreferRouteListResult getPreferRouteList(){
        Account user = AccountResource.authCheck(request);
        user = (Account)dbBean.find(Account.class, user.getPhoneNumber());
        if(user.getPreferRouteList() == null || user.getPreferRouteList().isEmpty()) {
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.PREFER_RECORD_NOT_FOUND, BundleUtil.getString(request, "TipNoPreferRoute")));
        }
        return new PreferRouteListResult(PublicResultCode.SUCCESS, "", user.getPreferRouteList());
    }

    @POST
    @Path("/prefer_route/add")
    @Consumes("application/json")
    public Response addPreferRoute(AddPreferRouteRequest aprr){
        if(aprr.getStartStationId().equals(aprr.getEndStationId())){
            throw new CheckException(new Result(PublicResultCode.PREFER_ROUTE_STARTSTATION_EQUAL_ENDSTATION,
                    BundleUtil.getString(request, "TipStartStationEqualEndStation")));
        }
        Account user = AccountResource.authCheck(request);
        if(dbBean.find(PreferRoute.class, new PreferRoutePK(user.getPhoneNumber(), aprr.getStartStationId(), aprr.getEndStationId())) != null){
            throw new CheckException(new Result(PublicResultCode.PREFER_RECORD_EXISTED, BundleUtil.getString(request, "TipPreferRouteExisted")));
        }
        dbBean.create(new PreferRoute(user.getPhoneNumber(), aprr.getStartStationId(), aprr.getEndStationId()));
        return Response.status(Response.Status.CREATED).entity(new Result(PublicResultCode.SUCCESS, BundleUtil.getString(request, "TipAddPreferRouteSuccess"))).
                type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @DELETE
    @Path("/prefer_route/remove/{startStationId}/{endStationId}")
    public Result removePreferRoute(@PathParam("startStationId") int startStationId, @PathParam("endStationId") int endStationId){
        Account user = AccountResource.authCheck(request);
        PreferRoute pr = (PreferRoute) dbBean.find(PreferRoute.class, new PreferRoutePK(user.getPhoneNumber(), startStationId, endStationId));
        if(pr == null){
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.PREFER_RECORD_NOT_EXIST, BundleUtil.getString(request, "TipPreferRouteNotExist")));
        }
        dbBean.remove(pr);
        return new Result(PublicResultCode.SUCCESS, BundleUtil.getString(request, "TipRemovePreferRoute"));
    }
}
