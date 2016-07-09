package com.subwayticket.database.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by zhou-shengyun on 7/8/16.
 */
public class PreferSubwayStationPK implements Serializable {
    private String userId;
    private int stationId;

    @Column(name = "UserID", nullable = false, length = 20)
    @Id
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "StationID", nullable = false)
    @Id
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreferSubwayStationPK that = (PreferSubwayStationPK) o;

        if (stationId != that.stationId) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + stationId;
        return result;
    }
}
