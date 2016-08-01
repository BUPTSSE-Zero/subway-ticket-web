package com.subwayticket.database.model;

import javax.persistence.*;

/**
 * Created by zhou-shengyun on 8/1/16.
 */

@Entity
@Table(name = "SystemAccount")
public class SystemAccount {
    private String userId;
    private String password;

    public SystemAccount(){}

    public SystemAccount(String userId){
        this.userId = userId;
    }

    @Id
    @Column(name = "UserID", nullable = false, length = 15)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "Password", nullable = false, length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemAccount that = (SystemAccount) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
