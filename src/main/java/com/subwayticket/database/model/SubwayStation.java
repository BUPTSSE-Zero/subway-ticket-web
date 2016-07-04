package com.subwayticket.database.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by shengyun-zhou on 6/16/16.
 */
@Entity
@Table(name = "SubwayStation")
public class SubwayStation {
    private int subwayStationId;
    private String subwayStationName;
    private boolean available;
    private SubwayLine subwayLine;
    private transient List<TicketPrice> ticketAList;
    private transient List<TicketPrice> ticketBList;

    public SubwayStation(){}

    public SubwayStation(int subwayStationId){
        this.subwayStationId = subwayStationId;
    }

    @Id
    @Column(name = "SubwayStationID", nullable = false)
    public int getSubwayStationId() {
        return subwayStationId;
    }

    public void setSubwayStationId(int subwayStationId) {
        this.subwayStationId = subwayStationId;
    }

    @Basic
    @Column(name = "SubwayStationName", nullable = false, length = 30)
    public String getSubwayStationName() {
        return subwayStationName;
    }

    public void setSubwayStationName(String subwayStationName) {
        this.subwayStationName = subwayStationName;
    }

    @Basic
    @Column(name = "Available", nullable = false)
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubwayStation that = (SubwayStation) o;

        if (subwayStationId != that.subwayStationId) return false;
        if (available != that.available) return false;
        if (subwayStationName != null ? !subwayStationName.equals(that.subwayStationName) : that.subwayStationName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subwayStationId;
        result = 31 * result + (subwayStationName != null ? subwayStationName.hashCode() : 0);
        return result;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SubwayLineID")
    public SubwayLine getSubwayLine() {
        return subwayLine;
    }

    public void setSubwayLine(SubwayLine subwayLine) {
        this.subwayLine = subwayLine;
    }

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "subwayStationA")
    public List<TicketPrice> getTicketAList() {
        return ticketAList;
    }

    public void setTicketAList(List<TicketPrice> ticketStationA) {
        this.ticketAList = ticketStationA;
    }

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "subwayStationB")
    public List<TicketPrice> getTicketBList() {
        return ticketBList;
    }

    public void setTicketBList(List<TicketPrice> ticketB) {
        this.ticketBList = ticketB;
    }

    private StationMessage stationMessage;

    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    @JoinColumn(name = "StationMessageID")
    public StationMessage getStationMessage() {
        return stationMessage;
    }

    public void setStationMessage(StationMessage stationMessage) {
        this.stationMessage = stationMessage;
    }
}
