package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum CreditCardProvider {
    VISA("VISA","Visa"),
    MASTERCARD("MASTERCARD","Mastercard"),
    MAESTRO("MAESTRO","Maestro"),
    DINNER("DINNER","Dinner");

    private CreditCardProvider(String value, String label) {
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
