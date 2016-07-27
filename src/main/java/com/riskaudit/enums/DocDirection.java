package com.riskaudit.enums;

/**
 *
 * @author alisenturk
 */
public enum DocDirection {

    OUTGOING("OUTGOING","Giden Evrak"),
    INCOMING("INCOMING","Gelen Evrak");

    private DocDirection(String value, String label) {
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
