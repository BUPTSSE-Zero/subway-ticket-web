package com.subwayticket.model.result;

import com.subwayticket.database.model.SubwayStation;

import java.util.List;

/**
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class SubwayStationListResult extends Result {
    private List<SubwayStation> subwayStationList;

    public SubwayStationListResult(int resultCode, String resultDescription, List<SubwayStation> subwayStationList) {
        super(resultCode, resultDescription);
        this.subwayStationList = subwayStationList;
    }

    public SubwayStationListResult() {}

    public List<SubwayStation> getSubwayStationList() {
        return subwayStationList;
    }

    public void setSubwayStationList(List<SubwayStation> subwayStationList) {
        this.subwayStationList = subwayStationList;
    }
}
