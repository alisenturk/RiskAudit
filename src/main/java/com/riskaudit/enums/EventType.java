package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum EventType {
    MEETING("MEETING","Meeting"),
    VISIT("VISIT","Customer Visit"),
    HOLIDAY("HOLIDAY","Holiday");

    private String  key;
    private String  value;
    
    EventType(String key,String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
}
