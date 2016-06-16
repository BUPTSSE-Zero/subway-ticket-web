package com.subwayticket.control.mobileapi.v1;

import com.subwayticket.control.mobileapi.CheckException;
import com.subwayticket.database.control.SubwayTicketDBHelperBean;
import com.subwayticket.database.model.City;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.result.CityListResult;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.BundleUtil;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by shengyun-zhou on 6/16/16.
 */

@Path("/v1/subway")
public class SubwayInfoResource {
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    @EJB
    private SubwayTicketDBHelperBean dbBean;

    @GET
    @Path("/city")
    @Produces("application/json")
    public CityListResult getCityList(){
        List<City> cityList = dbBean.findAll(City.class);
        if(cityList == null || cityList.isEmpty())
            throw new CheckException(Response.Status.NOT_FOUND.getStatusCode(),
                    new Result(PublicResultCode.CITY_LIST_NOT_FOUND, BundleUtil.getString(request, "TipResultNotFound")));
        return new CityListResult(PublicResultCode.SUCCESS, "", cityList);
    }
}
