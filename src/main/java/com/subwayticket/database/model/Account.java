package com.subwayticket.database.model;

import javax.persistence.*;
import java.util.List;
import java.util.List;
import java.util.List;
import java.util.Date;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
@Entity
@Table(name = "Account")
public class Account {
    private String phoneNumber;
    private transient String password;
    private Date registerDate;

    public Account() {
    }

    public Account(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.registerDate = new Date();
    }

    public Account(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Id
    @Column(name = "PhoneNumber", nullable = false, length = 20)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "Password", nullable = false, length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "RegisterDate", nullable = false)
    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (phoneNumber != null ? !phoneNumber.equals(account.phoneNumber) : account.phoneNumber != null) return false;
        if (password != null ? !password.equals(account.password) : account.password != null) return false;
        if (registerDate != null ? !registerDate.equals(account.registerDate) : account.registerDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = phoneNumber != null ? phoneNumber.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (registerDate != null ? registerDate.hashCode() : 0);
        return result;
    }

    private List<TicketOrder> orderList;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "user")
    public List<TicketOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<TicketOrder> orderList) {
        this.orderList = orderList;
    }

    private List<PreferSubwayStation> preferSubwayStationList;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "user")
    public List<PreferSubwayStation> getPreferSubwayStationList() {
        return preferSubwayStationList;
    }

    public void setPreferSubwayStationList(List<PreferSubwayStation> preferSubwayStationList) {
        this.preferSubwayStationList = preferSubwayStationList;
    }

    private List<HistoryRoute> historyRouteList;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "user")
    public List<HistoryRoute> getHistoryRouteList() {
        return historyRouteList;
    }

    public void setHistoryRouteList(List<HistoryRoute> historyRouteList) {
        this.historyRouteList = historyRouteList;
    }

    private List<PreferRoute> preferRouteList;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "user")
    public List<PreferRoute> getPreferRouteList() {
        return preferRouteList;
    }

    public void setPreferRouteList(List<PreferRoute> preferRouteList) {
        this.preferRouteList = preferRouteList;
    }
}
