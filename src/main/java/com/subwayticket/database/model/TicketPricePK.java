package com.subwayticket.database.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by shengyun-zhou on 6/16/16.
 */
public class TicketPricePK implements Serializable {
    private int subwayStationAid;
    private int subwayStationBid;

    public TicketPricePK(int subwayStationAid, int subwayStationBid) {
        this.subwayStationAid = subwayStationAid;
        this.subwayStationBid = subwayStationBid;
    }

    @Column(name = "SubwayStationAID", nullable = false)
    @Id
    public int getSubwayStationAid() {
        return subwayStationAid;
    }

    public void setSubwayStationAid(int subwayStationAid) {
        this.subwayStationAid = subwayStationAid;
    }

    @Column(name = "SubwayStationBID", nullable = false)
    @Id
    public int getSubwayStationBid() {
        return subwayStationBid;
    }

    public void setSubwayStationBid(int subwayStationBid) {
        this.subwayStationBid = subwayStationBid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketPricePK that = (TicketPricePK) o;

        if (subwayStationAid != that.subwayStationAid) return false;
        if (subwayStationBid != that.subwayStationBid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subwayStationAid;
        result = 31 * result + subwayStationBid;
        return result;
    }
}
