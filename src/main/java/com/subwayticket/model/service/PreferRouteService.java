package com.subwayticket.model.service;

import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.PreferRoute;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * Created by shenqipingguo on 16-7-12.
 */
@ManagedBean(name="preferRouteService", eager = true)
@ApplicationScoped
public class PreferRouteService {
    @EJB
    SystemDBHelperBean systemDBHelperBean;
    private List<PreferRoute> preferRoutes;

    //@PostConstruct
    //public void init(){
    //    preferRoutes = systemDBHelperBean.findAll(PreferRoute.class);
    //}

    public List<PreferRoute> getPreferRoutes() {
        preferRoutes = systemDBHelperBean.findAll(PreferRoute.class);
        return preferRoutes;
    }
}