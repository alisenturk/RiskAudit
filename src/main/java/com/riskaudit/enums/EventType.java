package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum EventType {
    MEETING("MEETING","Meeting"),
    VISIT("VISIT","Customer Visit"),
    HOLIDAY("HOLIDAY","Holiday"),
    REMINDER("REMINDER","Reminder"),
    CUSTOMER_CALL("CUSTCALL","Custommer Call");
    private String  key;
    private String  value;
    
    EventType(String key,String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    
}
