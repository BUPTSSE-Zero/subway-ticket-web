package com.subwayticket.model.converter;

import com.subwayticket.database.control.SystemDBHelperBean;
import com.subwayticket.database.model.City;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by zhou-shengyun on 7/26/16.
 */

@FacesConverter("cityConverter")
public class CityConverter implements Converter{
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value == null || value.isEmpty())
            return null;
        SystemDBHelperBean dbBean = SystemDBHelperBean.getInstance();
        try {
            return dbBean.find(City.class, Integer.valueOf(value));
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null)
            return null;
        if(value instanceof City)
            return String.valueOf(((City)value).getCityId());
        return value.toString();
    }
}
