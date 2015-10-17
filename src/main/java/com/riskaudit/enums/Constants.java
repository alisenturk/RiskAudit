package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum Constants {
    InquirySearchResult("InquirySearchResult","Inquiry arama sonuçları");
    
    
    Constants(String val){
        this.value = val;        
    }
    Constants(String val,String description){
        this.value = val;
        this.description = description;
    }
    
    private String  value;
    private String  description;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
