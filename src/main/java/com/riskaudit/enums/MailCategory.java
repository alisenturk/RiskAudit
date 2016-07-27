package com.riskaudit.enums;

/**
 *
 * @author alisenturk
 */
public enum MailCategory {
    
    BANK("BANK","Banka Maili"),
    CARGO("CARGO","Kargo Firması Maili"),
    CUSTOMER("CUSTOMER","Müşteri Maili");

    MailCategory(String value, String label) {
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
