package com.riskaudit.entity.order.chargeback;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.enums.ChargebackProcessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alisenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "LawReason.findAll",query = "select d from LawReason d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "LawReason.findLawReasonByMerchant",query = "select d from LawReason d where d.merchant.id=:mrchntid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
})
@XmlRootElement
public class LawReason extends BaseEntity{
    
    private Merchant                merchant;
    private String                  title;
    private String                  description;

    @ManyToOne
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @NotNull
    @Column(length = 60,nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    @Column(length = 250,nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    
}
