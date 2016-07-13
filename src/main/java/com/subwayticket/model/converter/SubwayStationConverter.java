package com.subwayticket.model.converter;

import com.subwayticket.database.model.SubwayStation;
import com.subwayticket.model.service.SubwayStationService;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Created by shenqipingguo on 16-7-11.
 */

@FacesConverter("subwayStationConverter")
public class SubwayStationConverter implements Converter {

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                SubwayStationService service = (SubwayStationService) fc.getExternalContext().getApplicationMap().get("subwayStationService");
                for(SubwayStation subwayStation : service.getSubwayStations())
                    if(subwayStation.getSubwayStationId() == Integer.parseInt(value))
                        return subwayStation;
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
            return String.valueOf(((SubwayStation) object).getSubwayStationId());
        }
        else {
            return null;
        }
    }
}
