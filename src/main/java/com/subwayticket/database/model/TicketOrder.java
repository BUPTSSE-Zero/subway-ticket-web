package com.subwayticket.database.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhou-shengyun on 7/4/16.
 */
@Entity
@Table(name = "TicketOrder")
public class TicketOrder {
    public static final char ORDER_STATUS_NOT_PAY = 'A';
    public static final char ORDER_STATUS_NOT_DRAW_TICKET = 'B';
    public static final char ORDER_STATUS_FINISHED = 'C';
    public static final char ORDER_STATUS_REFUNDED = 'D';

    private String ticketOrderId;
    private Date ticketOrderTime;
    private Account user;
    private SubwayStation endStation;
    private SubwayStation startStation;
    private float ticketPrice;
    private int drawAmount;
    private int amount;
    private char status;
    private String ticketKey;
    private String comment;

    public TicketOrder(){}

    public TicketOrder(String ticketOrderId, Date ticketOrderTime, Account user, TicketPrice ticketPrice, int amount){
        this(ticketOrderId, ticketOrderTime, user, ticketPrice.getSubwayStationA(), ticketPrice.getSubwayStationB(), ticketPrice.getPrice(), amount);
    }

    public TicketOrder(String ticketOrderId, Date ticketOrderTime, Account user, SubwayStation startStation, SubwayStation endStation, float ticketPrice, int amount) {
        this.ticketOrderId = ticketOrderId;
        this.ticketOrderTime = ticketOrderTime;
        this.user = user;
        this.endStation = endStation;
        this.startStation = startStation;
        this.ticketPrice = ticketPrice;
        this.amount = amount;
        this.status = ORDER_STATUS_NOT_PAY;
        this.drawAmount = 0;
    }


    @Id
    @Column(name = "TicketOrderID", nullable = false, length = 30)
    public String getTicketOrderId() {
        return ticketOrderId;
    }

    public void setTicketOrderId(String ticketOrderId) {
        this.ticketOrderId = ticketOrderId;
    }

    @Basic
    @Column(name = "TicketOrderTime", nullable = false)
    public Date getTicketOrderTime() {
        return ticketOrderTime;
    }

    public void setTicketOrderTime(Date ticketOrderTime) {
        this.ticketOrderTime = ticketOrderTime;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    @JoinColumn(name = "UserID")
    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    @JoinColumn(name = "StartStationID")
    public SubwayStation getStartStation() {
        return startStation;
    }

    public void setStartStation(SubwayStation startStation) {
        this.startStation = startStation;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    @JoinColumn(name = "EndStationID")
    public SubwayStation getEndStation() {
        return endStation;
    }

    public void setEndStation(SubwayStation endStation) {
        this.endStation = endStation;
    }

    @Basic
    @Column(name = "TicketPrice", nullable = false, precision = 0)
    public float getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(float ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Basic
    @Column(name = "DrawAmount", nullable = false)
    public int getDrawAmount() {
        return drawAmount;
    }

    public void setDrawAmount(int drawAmount) {
        this.drawAmount = drawAmount;
    }

    @Basic
    @Column(name = "Amount", nullable = false)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "Status", nullable = false, length = 1)
    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @Basic
    @Column(name = "TicketKey", nullable = true, length = 15)
    public String getTicketKey() {
        return ticketKey;
    }

    public void setTicketKey(String ticketKey) {
        this.ticketKey = ticketKey;
    }

    @Basic
    @Column(name = "Comment", nullable = true, length = 50)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketOrder that = (TicketOrder) o;

        if (Double.compare(that.ticketPrice, ticketPrice) != 0) return false;
        if (drawAmount != that.drawAmount) return false;
        if (amount != that.amount) return false;
        if (ticketOrderId != null ? !ticketOrderId.equals(that.ticketOrderId) : that.ticketOrderId != null)
            return false;
        if (status != that.status) return false;
        if (ticketKey != null ? !ticketKey.equals(that.ticketKey) : that.ticketKey != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = ticketOrderId != null ? ticketOrderId.hashCode() : 0;
        temp = Double.doubleToLongBits(ticketPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + drawAmount;
        result = 31 * result + amount;
        result = 31 * result + new Character(status).hashCode();
        result = 31 * result + (ticketKey != null ? ticketKey.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
