package com.subwayticket.model.result;

import com.subwayticket.database.model.HistoryRoute;

import java.util.List;

/**
 * Created by zhou-shengyun on 7/25/16.
 */
public class HistoryRouteListResult extends Result{
    private List<HistoryRoute> historyRouteList;
    
    public HistoryRouteListResult(int resultCode, String resultDescription, List<HistoryRoute> historyRouteList) {
        super(resultCode, resultDescription);
        this.historyRouteList = historyRouteList;
    }

    public List<HistoryRoute> getHistoryRouteList() {
        return historyRouteList;
    }

    public void setHistoryRouteList(List<HistoryRoute> historyRouteList) {
        this.historyRouteList = historyRouteList;
    }
}
