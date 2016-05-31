package com.subwayticket.model.primefaces;

/**
 * Created by 张炜奇 on 2016/5/14 0014.
 */

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.application.FacesMessage;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.RegisterRequest;
import com.subwayticket.model.Result;
import com.subwayticket.util.CheckUtil;
import com.subwayticket.util.JedisUtil;
import com.subwayticket.util.SecurityUtil;
import com.subwayticket.database.control.*;
import com.subwayticket.database.model.Account;
import javax.ejb.EJB;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
public class UserOperation{
    @EJB
    private SubwayTicketDBHelperBean dbBean;
    private RegisterRequest regReq;
    private ServletRequest request;
    private String phoneNumber;
    private String password;
    private String captcha;
    private boolean registSuccessfull;
    public UserOperation(){}
    public UserOperation(String phoneNumber, String password, String captcha){
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.captcha = captcha;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setCaptcha(String captcha){
        this.captcha = captcha;
    }
    public void setRegistSuccessfull(boolean registSuccessfull){
        this.registSuccessfull = registSuccessfull;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public String getPassword(){
        return password;
    }
    public String getCaptcha(){
        return captcha;
    }
    public boolean getRegistSuccessfull() {
        return registSuccessfull;
    }
    public void register(ActionEvent actionEgetCurrentInstancevent) {
        request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Result result = CheckUtil.checkRegisterInfo(request, regReq, dbBean, JedisUtil.getJedis());
        if (result.getResultCode() == PublicResultCode.SUCCESS_CODE) {
            //将新的用户信息写入到数据库中
            Account newUser = new Account(regReq.getPhoneNumber(), regReq.getPassword());
            dbBean.create(newUser);
            setRegistSuccessfull(true);
            addMessage("result.getResultDescription()");
        }
        else {
            setRegistSuccessfull(false);
            addMessage("result.getResultDescription()");
        }
    }
    public void login(){

    }
    public void sendCaptcha(ActionEvent actionEvent){
        if (SecurityUtil.sendPhoneCaptcha(request, getPhoneNumber(), JedisUtil.getJedis()).getResultCode() == PublicResultCode.SUCCESS_CODE){
        }
    }
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
