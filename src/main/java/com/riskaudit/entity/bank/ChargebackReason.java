package com.riskaudit.entity.bank;

import com.riskaudit.entity.base.BaseEntity;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "ChargebackReason.findAll",query = "select d from ChargebackReason d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})    
})
@XmlRootElement
public class ChargebackReason extends BaseEntity{
    
    private String  reasonCode;
    private String  reasonDescription;

    
    @NotNull
    @Column(length = 10)
    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    @NotNull
    @Column(length = 60)
    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }
    
    
    
}
