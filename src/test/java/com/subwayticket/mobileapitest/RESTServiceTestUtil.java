package com.subwayticket.mobileapitest;

import com.subwayticket.control.mobileapi.GsonProvider;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.SecurityUtil;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * Created by shengyun-zhou on 6/10/16.
 */
public class RESTServiceTestUtil {
    public static final String API_BASE_URL_V1 = "http://localhost:16080/subway-ticket-web/mobileapi/v1";

    public static WebTarget getWebTarget(String url){
        Client client = ClientBuilder.newClient();
        client.register(GsonProvider.class);
        return client.target(url);
    }

    public static Invocation.Builder getRequestBuilder(String url, String token){
        WebTarget target = getWebTarget(url);
        Invocation.Builder builder = target.request();
        if(token != null)
            builder.header(SecurityUtil.HEADER_TOKEN_KEY, token);
        return builder;
    }

    public static Response post(String url, Object jsonEntity, String token){
        return getRequestBuilder(url, token).buildPost(Entity.json(jsonEntity)).invoke();
    }

    public static Response put(String url, Object jsonEntity, String token){
        if(jsonEntity == null)
            return getRequestBuilder(url, token).buildPut(Entity.text("")).invoke();
        return getRequestBuilder(url, token).buildPut(Entity.json(jsonEntity)).invoke();
    }

    public static Result showResponse(Response response){
        return showResponse(response, Result.class);
    }

    public static Result showResponse(Response response, Class<? extends Result> entityClass){
        System.out.println("Status Code:" + response.getStatus());
        if(response.getStatus() == 204)
            return null;
        Result result = response.readEntity(entityClass);
        System.out.println("Result Code:" + result.getResultCode());
        System.out.println("Result Description:" + result.getResultDescription());
        return result;
    }
}
