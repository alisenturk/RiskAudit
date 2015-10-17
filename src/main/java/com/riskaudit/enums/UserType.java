package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum UserType {
    
    ADMIN("ADMIN","Yönetici"),
    MEMBER("MEMBER","Üye"),
    MEMBER_ADMIN("MEMBERADMIN","Üye İşyeri Yöneticisi");
    
    private UserType(String key,String val){
        this.key    = key;
        this.value  = val;
    }
    
    private String key;
    private String value;

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
