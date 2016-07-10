package com.subwayticket.model.managedbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import com.subwayticket.control.AccountControl;
import com.subwayticket.database.model.Account;
import com.subwayticket.model.PublicResultCode;
import com.subwayticket.model.request.LoginRequest;
import com.subwayticket.model.request.ModifyPasswordRequest;
import com.subwayticket.model.request.ResetPasswordRequest;
import com.subwayticket.model.request.RegisterRequest;
import com.subwayticket.model.result.Result;
import com.subwayticket.util.BundleUtil;
import com.subwayticket.util.JedisUtil;
import com.subwayticket.util.SecurityUtil;
import com.subwayticket.database.control.*;
import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by 张炜奇 on 2016/5/14 0014.
 */

@ManagedBean
@SessionScoped
public class UserOperationBean implements Serializable {
    @EJB
    private SystemDBHelperBean dbBean;
    private HttpServletRequest request;
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
            if (result.getResultCode() == PublicResultCode.SUCCESS) {
                password = newPassword;
                return login();
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
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            Result result = AccountControl.webLogin(request, new LoginRequest(phoneNumber, password), dbBean);
            if (result.getResultCode() == PublicResultCode.SUCCESS || result.getResultCode() == PublicResultCode.LOGIN_SUCCESS_WITH_PRE_OFFLINE) {
                RequestContext requestContext = RequestContext.getCurrentInstance();
                requestContext.addCallbackParam("login_result", result.getResultCode());
                setCaptcha("");
                setPassword("");
                setPhoneNumber("");
                setNewPassword("");
                if (result.getResultCode() == PublicResultCode.LOGIN_SUCCESS_WITH_PRE_OFFLINE)
                    return false;
                return true;
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getResultDescription(), ""));
            }
        }catch (Exception e){
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, BundleUtil.getString(request, "TipServerInternalError"), ""));
        }
        return false;
    }

    public void modifyPassword(){
        FacesContext context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        Result result = AccountControl.webModifyPassword(request, new ModifyPasswordRequest(password, newPassword), dbBean);
        if (result.getResultCode() != PublicResultCode.SUCCESS) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getResultDescription(), ""));
        }else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, result.getResultDescription(), ""));
        }
    }

    public boolean loginWithNewPassword(){
        FacesContext context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            Result result = AccountControl.resetPassword(request, new ResetPasswordRequest(phoneNumber, newPassword, captcha), dbBean, JedisUtil.getJedis());
            if (result.getResultCode() != PublicResultCode.SUCCESS) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getResultDescription(), ""));
                return false;
            }
            setPassword(newPassword);
            return login();
        }catch (Exception e){
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, BundleUtil.getString(request, "TipServerInternalError"), ""));
        }
        return false;
    }

    public void logout(){
        AccountControl.webLogout((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
    }

    public void sendCaptcha(){
        request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try{
            Result result = SecurityUtil.sendPhoneCaptcha(request, phoneNumber, JedisUtil.getJedis());
            if (result.getResultCode() == PublicResultCode.SUCCESS) {
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

    public void loginCheck() throws IOException{
        if(getUserID() == null){
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath()
            );
        }
    }
}
