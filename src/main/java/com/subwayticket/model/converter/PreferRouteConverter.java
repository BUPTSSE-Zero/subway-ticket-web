package com.subwayticket.model.converter;

import com.subwayticket.database.model.PreferRoute;
import com.subwayticket.model.service.PreferRouteService;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by shenqipingguo on 16-7-12.
 */

@FacesConverter("preferRouteConverter")
public class PreferRouteConverter implements Converter {
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                System.out.println(value);
                PreferRouteService service = (PreferRouteService) fc.getExternalContext().getApplicationMap().get("preferRouteService");
                for(PreferRoute preferRoute : service.getPreferRoutes()) {
                    String[] strings = value.split(" ");
                    if (preferRoute.getUserId().equals(strings[0])
                            && preferRoute.getStartStationId() == Integer.parseInt(strings[1])
                            && preferRoute.getEndStationId() == Integer.parseInt(strings[2])) {
                        System.out.println(preferRoute.getUserId());
                        System.out.println(preferRoute.getStartStationId());
                        System.out.println(preferRoute.getEndStationId());
                        return preferRoute;
                    }
                }
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
            String returnValue = String.valueOf(((PreferRoute) object).getUserId()) + " " + String.valueOf(((PreferRoute) object).getStartStationId()) + " " + String.valueOf(((PreferRoute) object).getEndStationId());
            return returnValue;
        }
        else {
            return null;
        }
    }
}
