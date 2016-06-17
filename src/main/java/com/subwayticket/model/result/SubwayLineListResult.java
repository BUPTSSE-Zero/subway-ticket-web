package com.subwayticket.model.result;

import com.subwayticket.database.model.SubwayLine;

import java.util.List;

/**
 * Created by shengyun-zhou on 6/16/16.
 */
public class SubwayLineListResult extends Result{
    private List<SubwayLine> subwayLineList;

    public SubwayLineListResult(){}


    public SubwayLineListResult(int resultCode, String resultDescription, List<SubwayLine> subwayLineList) {
        super(resultCode, resultDescription);
        this.subwayLineList = subwayLineList;
    }

    public List<SubwayLine> getSubwayLineList() {
        return subwayLineList;
    }

    public void setSubwayLineList(List<SubwayLine> subwayLineList) {
        this.subwayLineList = subwayLineList;
    }
}
