package com.subwayticket.model.request;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
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
