package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum VisitType {
    PHONECALL("PHONECALL","Phone Call"),
    VISIT("VISIT","Visit");
    
    private String  value;
    private String  label;
    
    VisitType(String value,String label){
        this.value = value;
        this.label = label;
    }

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
