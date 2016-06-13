package com.subwayticket.control.mobileapi;

import com.subwayticket.model.result.Result;
import com.subwayticket.util.BundleUtil;
import com.subwayticket.util.LoggerUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by shengyun-zhou on 6/10/16.
 */

@Provider
public class MobileAPIExceptionMapper implements ExceptionMapper<Exception> {
    private static final Logger logger = LoggerUtil.getLogger("Mobile API", "MobileAPI.log");
    @Context
    private HttpServletRequest request;


    @Override
    public Response toResponse(Exception exception) {
        exception.printStackTrace();
        logger.error("", exception);
        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        Result result = new Result(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), BundleUtil.getString(request, "TipServerInternalError"));
        if(exception instanceof BadRequestException){
            statusCode = Response.Status.BAD_REQUEST.getStatusCode();
            result.setResultCode(Response.Status.BAD_REQUEST.getStatusCode());
            result.setResultDescription(BundleUtil.getString(request, "TipBadRequest"));
        }else if(exception instanceof NotFoundException){
            statusCode = Response.Status.NOT_FOUND.getStatusCode();
            result.setResultCode(Response.Status.NOT_FOUND.getStatusCode());
            result.setResultDescription(BundleUtil.getString(request, "TipAPINotFound"));
        }else if(exception instanceof MobileAPIBaseException){
            statusCode = ((MobileAPIBaseException) exception).getStatusCode();
            result = ((MobileAPIBaseException) exception).getErrorResult();
        }else if(exception instanceof WebApplicationException){
            return ((WebApplicationException) exception).getResponse();
        }
        return Response.status(statusCode).entity(result).type(MediaType.APPLICATION_JSON).build();
    }
}
