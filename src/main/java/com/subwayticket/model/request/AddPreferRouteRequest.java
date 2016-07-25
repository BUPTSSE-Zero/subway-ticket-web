package com.subwayticket.model.request;

/**
 * Created by zhou-shengyun on 7/25/16.
 */
public class AddPreferRouteRequest {
    private Integer startStationId;
    private Integer endStationId;

    public AddPreferRouteRequest(Integer startStationId, Integer endStationId) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public Integer getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(Integer startStationId) {
        this.startStationId = startStationId;
    }

    public Integer getEndStationId() {
        return endStationId;
    }

    public void setEndStationId(Integer endStationId) {
        this.endStationId = endStationId;
    }
}
