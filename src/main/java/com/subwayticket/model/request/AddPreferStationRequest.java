package com.subwayticket.model.request;

/**
 * Created by zhou-shengyun on 7/25/16.
 */
public class AddPreferStationRequest {
    private Integer stationId;

    public AddPreferStationRequest(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }
}
