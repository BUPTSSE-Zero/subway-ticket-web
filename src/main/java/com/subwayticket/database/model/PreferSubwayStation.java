package com.subwayticket.database.model;

import javax.persistence.*;

/**
 * Created by zhou-shengyun on 7/8/16.
 */
@Entity
@Table(name = "PreferSubwayStation")
@IdClass(PreferSubwayStationPK.class)
public class PreferSubwayStation {
    private String userId;
    private int stationId;

    @Id
    @Column(name = "UserID", nullable = false, length = 20)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "StationID", nullable = false)
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

        PreferSubwayStation that = (PreferSubwayStation) o;

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

    private Account user;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "UserID", referencedColumnName = "PhoneNumber")
    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    private SubwayStation subwayStation;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "StationID", referencedColumnName = "SubwayStationID")
    public SubwayStation getSubwayStation() {
        return subwayStation;
    }

    public void setSubwayStation(SubwayStation subwayStation) {
        this.subwayStation = subwayStation;
    }
}
