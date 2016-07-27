package com.riskaudit.entity.order.chargeback;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.enums.MailCategory;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alisenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "MailTemplate.findAll",query = "select d from MailTemplate d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "MailTemplate.findMailTemplateByMerchant",query = "select d from MailTemplate d where d.merchant.id=:mrchntid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
})
@XmlRootElement
public class MailTemplate extends BaseEntity{
    private Merchant        merchant;
    private MailCategory    mailCategory;
    private String          name;
    private String          mailContent;

    @ManyToOne
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Enumerated(EnumType.STRING)
    public MailCategory getMailCategory() {
        return mailCategory;
    }

    public void setMailCategory(MailCategory mailCategory) {
        this.mailCategory = mailCategory;
    }

    @Column(length = 4000)
    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    @Column(length = 120)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}