package com.subwayticket.model.result;

import com.subwayticket.database.model.PreferRoute;

import java.util.List;

/**
 * Created by zhou-shengyun on 7/25/16.
 */
public class PreferRouteListResult extends Result {
    private List<PreferRoute> preferRouteList;

    public PreferRouteListResult(int resultCode, String resultDescription, List<PreferRoute> preferRouteList) {
        super(resultCode, resultDescription);
        this.preferRouteList = preferRouteList;
    }

    public List<PreferRoute> getPreferRouteList() {
        return preferRouteList;
    }

    public void setPreferRouteList(List<PreferRoute> preferRouteList) {
        this.preferRouteList = preferRouteList;
    }
}
