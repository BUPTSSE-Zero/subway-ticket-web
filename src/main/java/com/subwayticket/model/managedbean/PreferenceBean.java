package com.subwayticket.model.managedbean;

import com.subwayticket.control.AccountControl;
import com.subwayticket.database.model.Account;
import com.subwayticket.database.model.SubwayStation;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenqipingguo on 16-7-9.
 */

@ManagedBean
@ViewScoped
public class PreferenceBean implements Serializable {
    private HttpServletRequest request;
    private Account user;

    @PostConstruct
    public void init(){
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        user = (Account)request.getSession(false).getAttribute(AccountControl.SESSION_ATTR_USER);
    }
}
