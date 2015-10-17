package com.riskaudit.action.base;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author asenturk
 */
@SessionScoped
@ManagedBean(name = "language")
public class LanguageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String  localeCode;
    private Locale  currentLocale;
    private static Map<String, Object> countries;
    
    @PostConstruct
    public void init(){        
        localeCode = getCurrentLanguage();
    }
    
    static {        
        countries = new LinkedHashMap<String, Object>();
        countries.put("English", Locale.ENGLISH); //label, value
        countries.put("Türkçe", new Locale("tr","TR"));
        countries.put("German", new Locale("de","DE"));        
    }

    public Map<String, Object> getCountriesInMap() {
        return countries;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }
    public String getCurrentLanguage(){
        return getCurrentLocale().getLanguage()+"_"+getCurrentLocale().getLanguage().toUpperCase();
    }
  
    public void countryLocaleCodeChanged(ValueChangeEvent e) {
        String newLocaleValue = e.getNewValue().toString();
        
        for (Map.Entry<String, Object> entry : countries.entrySet()) {
            if (entry.getValue().toString().equals(newLocaleValue)) {
                currentLocale = (Locale) entry.getValue();
                FacesContext.getCurrentInstance().getViewRoot().setLocale(currentLocale);
            }
        }        
    }

    public Locale getCurrentLocale() {
        if(currentLocale==null){
            currentLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        }
        return currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }
    
    
}
