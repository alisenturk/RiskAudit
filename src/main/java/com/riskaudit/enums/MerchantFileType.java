package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum MerchantFileType {
    CHARGEBACK_RESPONSE_TEMPLATE("CHARGEBACKRESPONSETEMPLATE","Chargeback Response Template");
    
    private String key;
    private String label;
    
    MerchantFileType(String key,String value){
        this.key    = key;
        this.label  = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
}
