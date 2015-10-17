package com.riskaudit.enums;

/**
 *
 * @author asenturk
 */
public enum Currency {
    TRY("TRY","Türk Lirası"),
    USD("USD","ABD Doları"),
    EUR("EUR","Euro"),
    GBP("GBP","Sterlin"),
    RUB("RUB","Ruble");
    
    private String  key;
    private String  value;
    Currency(String key,String value) {
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
