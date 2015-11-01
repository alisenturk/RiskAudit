package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum CreditCardCategory {
    
    CLASSIC("CLASSIC","Classic"),
    STANDARD("STANDARD","Standart"),
    GOLD("GOLD","Gold"),
    PLATINUM("PLATINUM","Platin");
    
    private CreditCardCategory(String value,String label){
        this.value = value;
        this.label = label;
    }

    private String  value;
    private String  label;
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
}
