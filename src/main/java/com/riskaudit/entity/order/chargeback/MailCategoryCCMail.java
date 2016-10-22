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
    @NamedQuery(name = "MailCategoryCCMail.findAll",query = "select d from MailCategoryCCMail d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "MailCategoryCCMail.findMailTemplateByMerchant",query = "select d from MailCategoryCCMail d where d.merchant.id=:mrchntid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "MailCategoryCCMail.findByMailCategoryAndMerchant",query = "select d from MailCategoryCCMail d where d.merchant.id=:mrchntid and d.mailCategory=:ctgry and d.status='ACTIVE'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class MailCategoryCCMail extends BaseEntity{
    private Merchant        merchant;
    private MailCategory    mailCategory;
    private String          ccMails;

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

    @Column(length = 600)
    public String getCcMails() {
        return ccMails;
    }

    public void setCcMails(String ccMails) {
        this.ccMails = ccMails;
    }
    
    
    
}
