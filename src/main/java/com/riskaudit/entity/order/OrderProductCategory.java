package com.riskaudit.entity.order;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author alisenturk
 */
@Embeddable
public class OrderProductCategory {
    
    private String  code;
    private String  name;

    @Column(name = "prdctCatCode",length = 20)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "prdctCatName",length = 60)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
