package com.subwayticket.database.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
@Entity
@Table(name = "PreferRoute")
@IdClass(PreferRoutePK.class)
public class PreferRoute{
    private transient String userId;
    private int startStationId;
    private int endStationId;
    private transient Account user;
    private SubwayStation startStation;
    private SubwayStation endStation;
    private Date addTime;

    public PreferRoute(){}

    public PreferRoute(String userId, int startStationId, int endStationId){
        this.userId = userId;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.addTime = new Date();
    }

    @Id
    @Column(name = "UserID", nullable = false, length = 20)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "StartStationID", nullable = false)
    public int getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(int startStationId) {
        this.startStationId = startStationId;
    }

    @Id
    @Column(name = "EndStationID", nullable = false)
    public int getEndStationId() {
        return endStationId;
    }

    public void setEndStationId(int endStationId) {
        this.endStationId = endStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreferRoute that = (PreferRoute) o;

        if (startStationId != that.startStationId) return false;
        if (endStationId != that.endStationId) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + startStationId;
        result = 31 * result + endStationId;
        return result;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "UserID", referencedColumnName = "PhoneNumber")
    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "StartStationID", referencedColumnName = "SubwayStationID")
    public SubwayStation getStartStation() {
        return startStation;
    }

    public void setStartStation(SubwayStation startStation) {
        this.startStation = startStation;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "EndStationID", referencedColumnName = "SubwayStationID")
    public SubwayStation getEndStation() {
        return endStation;
    }

    public void setEndStation(SubwayStation endStation) {
        this.endStation = endStation;
    }

    @Basic
    @Column(name = "AddTime", nullable = false)
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
