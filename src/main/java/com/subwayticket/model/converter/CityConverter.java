package com.subwayticket.model.converter;

import com.subwayticket.database.model.City;
import com.subwayticket.model.service.CityService;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.Converter;

/**
 * Created by shenqipingguo on 16-7-11.
 */

@FacesConverter("cityConverter")
public class CityConverter implements Converter{

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                CityService service = (CityService) fc.getExternalContext().getApplicationMap().get("cityService");
                for(City city : service.getCities())
                    if(city.getCityId() == Integer.parseInt(value))
                        return city;
                return null;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((City) object).getCityId());
        }
        else {
            return null;
        }
    }
}
