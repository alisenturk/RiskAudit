package com.riskaudit.enums;

/**
 *
 * @author alisenturk
 */
public enum MailSendStatus {
    WAIT("WAIT","Beklemede"),
    SENT("SENT","Gönderildi"),
    NOTSEND("NOTSEND","Gönderilemedi");

    MailSendStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }
    
    
    
    private String  value;
    private String  label;

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
