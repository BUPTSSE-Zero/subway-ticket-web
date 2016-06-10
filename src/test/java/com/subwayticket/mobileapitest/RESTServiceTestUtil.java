package com.subwayticket.mobileapitest;

import com.subwayticket.control.mobileapi.GsonProvider;
import com.subwayticket.model.result.Result;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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

    public static Response post(String url, Object jsonEntity){
        WebTarget target = getWebTarget(url);
        return target.request().buildPost(Entity.json(jsonEntity)).invoke();
    }

    public static Response put(String url, Object jsonEntity){
        WebTarget target = getWebTarget(url);
        return target.request().buildPut(Entity.json(jsonEntity)).invoke();
    }

    public static void showResponse(Response response){
        System.out.println("Status Code:" + response.getStatus());
        Result result = response.readEntity(Result.class);
        System.out.println("Result Code:" + result.getResultCode());
        System.out.println("Result Description:" + result.getResultDescription());
    }
}
