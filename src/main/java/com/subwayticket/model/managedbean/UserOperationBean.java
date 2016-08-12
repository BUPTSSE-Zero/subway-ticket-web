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
import redis.clients.jedis.Jedis;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author 张炜奇
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
    private Account user;

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

    public void checkSignupPhoneNumber(){
        request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Result result = AccountControl.checkPhoneNumber(phoneNumber, request);
        if(result.getResultCode() == PublicResultCode.SUCCESS)
            result = AccountControl.checkPhoneNumberRegistered(phoneNumber, dbBean, request);
        if(result.getResultCode() != PublicResultCode.SUCCESS)
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    result.getResultDescription(), ""));
    }

    public boolean register() {
        request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try{
            Jedis jedis = JedisUtil.getJedis();
            Result result = AccountControl.register(request, new RegisterRequest(phoneNumber, newPassword, captcha),
                    dbBean, jedis);
            jedis.close();
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

    /**
     * 将结果发送给前端的网页
     * @param result 要发送的结果
     */
    private void sendResult(Result result){
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("result_code", result.getResultCode());
        requestContext.addCallbackParam("result_description", result.getResultDescription());
    }

    public boolean login(){
        FacesContext context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            Result result = AccountControl.webLogin(request, new LoginRequest(phoneNumber, password), dbBean);
            sendResult(result);
            if (result.getResultCode() == PublicResultCode.SUCCESS || result.getResultCode() == PublicResultCode.LOGIN_SUCCESS_WITH_PRE_OFFLINE) {
                setCaptcha("");
                setPassword("");
                setPhoneNumber("");
                setNewPassword("");
                HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
                user = (Account) session.getAttribute(AccountControl.SESSION_ATTR_USER);
                user = (Account) dbBean.find(Account.class, user.getPhoneNumber());
                return result.getResultCode() != PublicResultCode.LOGIN_SUCCESS_WITH_PRE_OFFLINE;
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
        Result result = AccountControl.modifyPassword(request, new ModifyPasswordRequest(password, newPassword), user, dbBean);
        if (result.getResultCode() != PublicResultCode.SUCCESS) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getResultDescription(), ""));
        }else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, result.getResultDescription(), ""));
        }
    }

    /**
     * 重置密码并登录
     * @return true表示重置密码且登录成功
     */
    public boolean loginWithNewPassword(){
        FacesContext context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            Jedis jedis = JedisUtil.getJedis();
            Result result = AccountControl.resetPassword(request, new ResetPasswordRequest(phoneNumber, newPassword, captcha), dbBean, jedis);
            jedis.close();
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
        user = null;
        AccountControl.webLogout((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());
    }

    public void sendCaptcha(){
        request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try{
            Jedis jedis = JedisUtil.getJedis();
            Result result = SecurityUtil.sendPhoneCaptcha(request, phoneNumber, jedis);
            jedis.close();
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
        if(user == null)
            return null;
        StringBuffer phoneNumber = new StringBuffer(user.getPhoneNumber());
        for(int i = 3; i < 7 && i < phoneNumber.length(); i++)
            phoneNumber.setCharAt(i, '*');
        return phoneNumber.toString();
    }

    /**
     * 检查用户是否登录，如果未登录则强制重定向到首页
     * @throws IOException
     */
    public void loginCheck() throws IOException{
        if(getUserID() == null){
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath()
            );
        }
    }

    public void refreshLoginUser(){
        if(user != null)
            dbBean.refresh(user);
    }
}
