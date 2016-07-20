package com.subwayticket.database.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhou-shengyun on 7/8/16.
 */
@Entity
@Table(name = "PreferSubwayStation")
@IdClass(PreferSubwayStationPK.class)
public class PreferSubwayStation implements Comparable<PreferSubwayStation> {
    private String userId;
    private int stationId;
    private Account user;
    private SubwayStation subwayStation;
    private Date addTime;

    public PreferSubwayStation(){}

    public PreferSubwayStation(String userId, int stationId){
        this.userId = userId;
        this.stationId = stationId;
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

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "UserID", referencedColumnName = "PhoneNumber")
    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "StationID", referencedColumnName = "SubwayStationID")
    public SubwayStation getSubwayStation() {
        return subwayStation;
    }

    public void setSubwayStation(SubwayStation subwayStation) {
        this.subwayStation = subwayStation;
    }

    @Basic
    @Column(name = "AddTime", nullable = false)
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    @Override
    public int compareTo(PreferSubwayStation o) {
        if(this.addTime.before(o.addTime))
            return -1;
        else if(this.addTime.after(o.addTime))
            return 1;
        return 0;
    }
}
