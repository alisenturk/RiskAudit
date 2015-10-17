package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum PaymentSecureType {
    
    FULL3DSECURE("3DFULLSECURE","3D Full Secure"),
    HALF3DSECURE("3DHALFSECURE","3D Half Secure"),
    NONSECURE("NONSECURE","Non Secure");

    private PaymentSecureType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    

    private String value;
    private String label;

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
