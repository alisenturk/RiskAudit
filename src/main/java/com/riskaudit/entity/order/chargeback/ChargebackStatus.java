package com.riskaudit.entity.order.chargeback;

/**
 *
 * @author alisenturk
 */
public enum ChargebackStatus {
    OPEN("OPEN","Open"),
    CLOSED("CLOSED","Closed"),
    CANCELLED("CANCELLED","Cancelled");

    ChargebackStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }
    
    private String  value;
    private String  label;

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
    
    
}
