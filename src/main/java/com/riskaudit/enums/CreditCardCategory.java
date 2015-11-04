package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum CreditCardCategory {
    
    BUSINESS("BUSINESS","Business"),
    CLASSIC("CLASSIC","Classic"),
    CORPORATE("CORPORATE","Corporate"),
    ELECTRON("ELECTRON","Electron"),
    GOLD("GOLD","Gold"),
    INFINITE("INFINITE","Infinite"),
    MAESTRO("MAESTRO","Maestro"),
    OTHER("OTHER","Other"),
    PLATINUM("PLATINUM","Platinum"),
    PREMIER("PREMIER","Premier"),
    PREPAID("PREPAID","Prepaid"),
    SIGNATURE("SIGNATURE","Signature"),
    SIGNIA("SIGNIA","Signia"),
    TITANIUM("TITANIUM","Titanium"),
    WORLD("WORLD","World");

    
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
