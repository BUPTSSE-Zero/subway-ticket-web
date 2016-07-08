package com.subwayticket.model.request;

/**
 * Created by zhou-shengyun on 7/4/16.
 */
public class SubmitOrderRequest {
    private Integer startStationID;
    private Integer endStationID;
    private Integer amount;

    public SubmitOrderRequest(Integer startStationID, Integer endStationID, Integer amount) {
        this.startStationID = startStationID;
        this.endStationID = endStationID;
        this.amount = amount;
    }

    public Integer getStartStationID() {
        return startStationID;
    }

    public void setStartStationID(Integer startStationID) {
        this.startStationID = startStationID;
    }

    public Integer getEndStationID() {
        return endStationID;
    }

    public void setEndStationID(Integer endStationID) {
        this.endStationID = endStationID;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
