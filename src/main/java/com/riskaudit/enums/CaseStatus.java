package com.riskaudit.enums;

/**
 *
 * @author alisenturk
 */
public enum CaseStatus {
    OPENED("OPENED","Dava Açıldı"),
    LOST("LOST","Dava Kaybedildi"),        
    CLOSED("CLOSED","Dosya Kapatıldı"),        
    WON("WON","Dava Kazanıldı");

    CaseStatus(String value, String title) {
        this.value = value;
        this.label = title;
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
