package com.subwayticket.model.result;

import com.subwayticket.database.model.PreferSubwayStation;

import java.util.List;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class PreferStationListResult extends Result {
    private List<PreferSubwayStation> preferSubwayStationList;

    public PreferStationListResult(int resultCode, String resultDescription, List<PreferSubwayStation> preferSubwayStationList) {
        super(resultCode, resultDescription);
        this.preferSubwayStationList = preferSubwayStationList;
    }

    public List<PreferSubwayStation> getPreferSubwayStationList() {
        return preferSubwayStationList;
    }

    public void setPreferSubwayStationList(List<PreferSubwayStation> preferSubwayStationList) {
        this.preferSubwayStationList = preferSubwayStationList;
    }
}
