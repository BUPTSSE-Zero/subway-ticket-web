package com.subwayticket.database.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class HistoryRoutePK implements Serializable {
    private String userId;
    private int startStationId;
    private int endStartionId;

    @Column(name = "UserID", nullable = false, length = 20)
    @Id
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "StartStationID", nullable = false)
    @Id
    public int getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(int startStationId) {
        this.startStationId = startStationId;
    }

    @Column(name = "EndStartionID", nullable = false)
    @Id
    public int getEndStartionId() {
        return endStartionId;
    }

    public void setEndStartionId(int endStartionId) {
        this.endStartionId = endStartionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryRoutePK that = (HistoryRoutePK) o;

        if (startStationId != that.startStationId) return false;
        if (endStartionId != that.endStartionId) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + startStationId;
        result = 31 * result + endStartionId;
        return result;
    }
}
