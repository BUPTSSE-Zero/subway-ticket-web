package com.subwayticket.model.request;

/**
 * Created by zhou-shengyun on 7/4/16.
 */
public class SubmitOrderRequest {
    private int startStationID;
    private int endStationID;
    private int amount;

    public SubmitOrderRequest(int startStationID, int endStationID, int amount) {
        this.startStationID = startStationID;
        this.endStationID = endStationID;
        this.amount = amount;
    }

    public int getStartStationID() {
        return startStationID;
    }

    public void setStartStationID(int startStationID) {
        this.startStationID = startStationID;
    }

    public int getEndStationID() {
        return endStationID;
    }

    public void setEndStationID(int endStationID) {
        this.endStationID = endStationID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
