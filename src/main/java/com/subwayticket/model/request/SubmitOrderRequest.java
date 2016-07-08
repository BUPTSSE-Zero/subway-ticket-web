package com.subwayticket.model.request;

/**
 * Created by zhou-shengyun on 7/4/16.
 */
public class SubmitOrderRequest {
    private Integer startStationId;
    private Integer endStationId;
    private Integer amount;

    public SubmitOrderRequest(Integer startStationId, Integer endStationId, Integer amount) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.amount = amount;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
