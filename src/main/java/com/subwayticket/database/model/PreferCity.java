package com.subwayticket.database.model;

import javax.persistence.*;

/**
 * Created by zhou-shengyun on 7/8/16.
 */
@Entity
@Table(name = "PreferCity")
@IdClass(PreferCityPK.class)
public class PreferCity {
    private String userId;
    private int cityId;

    @Id
    @Column(name = "UserID", nullable = false, length = 20)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "CityID", nullable = false)
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreferCity that = (PreferCity) o;

        if (cityId != that.cityId) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + cityId;
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

    private City city;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(name = "CityID", referencedColumnName = "CityID")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
