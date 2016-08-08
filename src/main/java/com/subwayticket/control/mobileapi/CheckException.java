package com.subwayticket.control.mobileapi;

import com.subwayticket.model.result.Result;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class CheckException extends MobileAPIBaseException {
    public CheckException(Result result){
        super(422, result);
    }

    public CheckException(int statusCode, Result result){
        super(statusCode, result);
    }
}
