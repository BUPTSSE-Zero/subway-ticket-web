package com.subwayticket.database.model;

import javax.persistence.*;

/**
 * Created by zhou-shengyun on 7/8/16.
 */
@Entity
@Table(name = "PreferRoute")
@IdClass(PreferRoutePK.class)
public class PreferRoute {
    private String userId;
    private int startStationId;
    private int endStartionId;

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

        PreferRoute that = (PreferRoute) o;

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

    private Account user;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "UserID", referencedColumnName = "PhoneNumber")
    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    private SubwayStation startStation;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "StartStationID", referencedColumnName = "SubwayStationID")
    public SubwayStation getStartStation() {
        return startStation;
    }

    public void setStartStation(SubwayStation startStation) {
        this.startStation = startStation;
    }

    private SubwayStation endStation;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "EndStationID", referencedColumnName = "SubwayStationID")
    public SubwayStation getEndStation() {
        return endStation;
    }

    public void setEndStation(SubwayStation endStation) {
        this.endStation = endStation;
    }
}
