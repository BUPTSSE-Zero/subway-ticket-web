package com.subwayticket.control.mobileapi;

import com.subwayticket.model.result.Result;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by shengyun-zhou on 6/9/16.
 */
public class CheckException extends MobileAPIBaseException {
    public CheckException(Result result){
        super(422, result);
    }

    public CheckException(int statusCode, Result result){
        super(statusCode, result);
    }
}
