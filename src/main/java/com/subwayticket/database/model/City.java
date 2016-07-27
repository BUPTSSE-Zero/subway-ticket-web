package com.subwayticket.database.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by shengyun-zhou on 6/16/16.
 */
@Entity
@Table(name = "City")
public class City {
    private int cityId;
    private String cityName;
    private transient List<SubwayLine> subwayLineList;

    public City(int cityId) {
        this.cityId = cityId;
    }

    public City() {
    }

    @Id
    @Column(name = "CityID", nullable = false)
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Basic
    @Column(name = "CityName", nullable = false, length = 40)
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (cityId != city.cityId) return false;
        if (cityName != null ? !cityName.equals(city.cityName) : city.cityName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cityId;
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        return result;
    }

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "city")
    @OrderBy("subwayLineId ASC")
    public List<SubwayLine> getSubwayLineList() {
        return subwayLineList;
    }

    public void setSubwayLineList(List<SubwayLine> subwayLineList) {
        this.subwayLineList = subwayLineList;
    }
}
