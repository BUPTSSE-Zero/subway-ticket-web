package com.subwayticket.model.converter;

import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.SubwayStation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * SubwayStation对象的转换器
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */

@FacesConverter("subwayStationConverter")
public class SubwayStationConverter implements Converter{
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value == null || value.isEmpty())
            return null;
        SystemDBHelperBean dbBean = SystemDBHelperBean.getInstance();
        try{
            return dbBean.find(SubwayStation.class, Integer.valueOf(value));
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null)
            return null;
        if(value instanceof String)
            return (String) value;
        return String.valueOf(((SubwayStation)value).getSubwayStationId());
    }
}
