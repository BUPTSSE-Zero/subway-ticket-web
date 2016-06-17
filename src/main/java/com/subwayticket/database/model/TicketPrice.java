package com.subwayticket.database.model;

import javax.persistence.*;

/**
 * Created by shengyun-zhou on 6/16/16.
 */
@Entity
@Table(name = "TicketPrice")
@IdClass(TicketPricePK.class)
public class TicketPrice {
    private int subwayStationAid;
    private int subwayStationBid;
    private SubwayStation subwayStationA;
    private SubwayStation subwayStationB;
    private float price;

    @Id
    @Column(name = "SubwayStationAID", nullable = false)
    public int getSubwayStationAid() {
        return subwayStationAid;
    }

    public void setSubwayStationAid(int subwayStationAid) {
        this.subwayStationAid = subwayStationAid;
    }

    @Id
    @Column(name = "SubwayStationBID", nullable = false)
    public int getSubwayStationBid() {
        return subwayStationBid;
    }

    public void setSubwayStationBid(int subwayStationBid) {
        this.subwayStationBid = subwayStationBid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketPrice that = (TicketPrice) o;

        if (subwayStationAid != that.subwayStationAid) return false;
        if (subwayStationBid != that.subwayStationBid) return false;
        if (price != that.price) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subwayStationAid;
        result = 31 * result + subwayStationBid;
        result = 31 * result + (int)price;
        return result;
    }

    @Basic
    @Column(name = "Price", nullable = false)
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn(name = "SubwayStationAID", referencedColumnName = "SubwayStationID")
    public SubwayStation getSubwayStationA() {
        return subwayStationA;
    }

    public void setSubwayStationA(SubwayStation subwayStationA) {
        this.subwayStationA = subwayStationA;
    }

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn(name = "SubwayStationBID", referencedColumnName = "SubwayStationID")
    public SubwayStation getSubwayStationB() {
        return subwayStationB;
    }

    public void setSubwayStationB(SubwayStation subwayStationB) {
        this.subwayStationB = subwayStationB;
    }
}
