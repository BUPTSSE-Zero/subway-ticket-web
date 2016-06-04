package com.subwayticket.model.managedbean;

/**
 * Created by 张炜奇 on 2016/5/14 0014.
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import com.subwayticket.control.AccountControl;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.BundleUtil;
import com.subwayticket.util.JedisUtil;
import com.subwayticket.util.SecurityUtil;
import com.subwayticket.database.control.*;
import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class UserOperationBean implements Serializable {
    @EJB
    private SubwayTicketDBHelperBean dbBean;
    private ServletRequest request;
    private String phoneNumber;
    private String password;
    private String newPassword;
    private String captcha;

    public UserOperationBean(){}

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setCaptcha(String captcha){
        this.captcha = captcha;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    public boolean register() {
        request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try{
            Result result = AccountControl.register(request, new RegisterRequest(phoneNumber, newPassword, captcha),
                    dbBean, JedisUtil.getJedis());
            if (result.getResultCode() == PublicResultCode.SUCCESS_CODE) {
                login();
                return true;
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        result.getResultDescription(), ""));
            }
        }catch (Exception e){
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    BundleUtil.getString(request, "TipServerInternalError"), ""));
        }
        return false;
    }


    public boolean login(){
        FacesContext context = FacesContext.getCurrentInstance();
        Result result = AccountControl.webLogin((HttpServletRequest)context.getExternalContext().getRequest(),
                                                new LoginRequest(phoneNumber, password), dbBean);
        if(result.getResultCode() == PublicResultCode.SUCCESS_CODE || result.getResultCode() == AccountControl.LOGIN_SUCCESS_WITH_PRE_OFFLINE) {
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.addCallbackParam("login_result", result.getResultCode());
            setCaptcha("");
            setPassword("");
            setPhoneNumber("");
            setNewPassword("");
            return true;
        }else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getResultDescription(), ""));
        }
        return false;
    }


    public void sendCaptcha(){
        System.out.println("Send Captcha");
        request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try{
            Result result = SecurityUtil.sendPhoneCaptcha(request, phoneNumber, JedisUtil.getJedis());
            if (result.getResultCode() == PublicResultCode.SUCCESS_CODE) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        result.getResultDescription(), ""));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        result.getResultDescription(), ""));
            }
        }catch (Exception e){
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    BundleUtil.getString(request, "TipServerInternalError"), ""));
        }
    }

    public String getUserID(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if(session == null)
            return null;
        Account account = (Account)session.getAttribute(AccountControl.SESSION_ATTR_USER);
        if(account == null)
            return null;
        StringBuffer phoneNumber = new StringBuffer(account.getPhoneNumber());
        for(int i = 3; i < 7 && i < phoneNumber.length(); i++)
            phoneNumber.setCharAt(i, '*');
        return phoneNumber.toString();
    }
}
