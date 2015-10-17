package com.riskaudit.entity.bank;

import com.riskaudit.entity.base.BaseEntity;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "ChargebackCode.findAll",query = "select d from ChargebackCode d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})    
})
@XmlRootElement
public class ChargebackCode extends BaseEntity{
    
    private String  chargebackCode;
    private String  title;
    private String  processDescription;

    @Column(length = 20)
    public String getChargebackCode() {
        return chargebackCode;
    }

    public void setChargebackCode(String chargebackCode) {
        this.chargebackCode = chargebackCode;
    }

    @Column(length = 120)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 4000)
    public String getProcessDescription() {
        return processDescription;
    }

    public void setProcessDescription(String processDescription) {
        this.processDescription = processDescription;
    }
    
    
    
    
}
