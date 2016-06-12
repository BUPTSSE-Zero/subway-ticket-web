package com.subwayticket.database.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
@Entity
@Table(name = "Account")
public class Account {
    private String phoneNumber;
    private String password;
    private Date registerDate;

    public Account() {}

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
}
