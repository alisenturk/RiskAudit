package com.riskaudit.util.converter;

import com.riskaudit.util.Helper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author asenturk
 */
@FacesConverter("toUpperEnglish")
public class ToUpperCaseConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return (value != null) ? Helper.utfConvStrEng(value.toString().toUpperCase()): null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value != null) ? Helper.utfConvStrEng(value.toString().toUpperCase()) : null;
    }
    
}
