package com.subwayticket.database.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by shengyun-zhou on 6/16/16.
 */
@Entity
@Table(name = "SubwayLine")
public class SubwayLine {
    private int subwayLineId;
    private String subwayLineName;
    private City city;
    private transient List<SubwayStation> subwayStationList;

    public SubwayLine(int subwayLineId) {
        this.subwayLineId = subwayLineId;
    }

    public SubwayLine() {
    }


    @Id
    @Column(name = "SubwayLineID", nullable = false)
    public int getSubwayLineId() {
        return subwayLineId;
    }

    public void setSubwayLineId(int subwayLineId) {
        this.subwayLineId = subwayLineId;
    }

    @Basic
    @Column(name = "SubwayLineName", nullable = false, length = 20)
    public String getSubwayLineName() {
        return subwayLineName;
    }

    public void setSubwayLineName(String subwayLineName) {
        this.subwayLineName = subwayLineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubwayLine that = (SubwayLine) o;

        if (subwayLineId != that.subwayLineId) return false;
        if (subwayLineName != null ? !subwayLineName.equals(that.subwayLineName) : that.subwayLineName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subwayLineId;
        result = 31 * result + (subwayLineName != null ? subwayLineName.hashCode() : 0);
        return result;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CityID")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "subwayLine")
    public List<SubwayStation> getSubwayStationList() {
        return subwayStationList;
    }

    public void setSubwayStationList(List<SubwayStation> subwayStationList) {
        this.subwayStationList = subwayStationList;
    }
}
