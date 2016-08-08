package com.subwayticket.model.result;

import com.subwayticket.database.model.HistoryRoute;

import java.util.List;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
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
