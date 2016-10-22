package com.riskaudit.entity.order;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.entity.order.chargeback.MailTemplate;
import com.riskaudit.enums.MailCategory;
import com.riskaudit.enums.MailSendStatus;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alisenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "ChargebackMail.findChargebackMails",query = "select d from ChargebackMail d where d.orderChargeback.id=:chrgbckid and d.status<>'DELETED' order by d.sentDate desc",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class ChargebackMail  extends BaseEntity implements Cloneable{
    
    private OrderChargeback     orderChargeback;
    private MailCategory        mailCategory;        
    private MailTemplate        mailTemplate;
    private String              fromAddress;
    private String              toAddress;
    private String              mailSubject;
    private String              mailContent;
    private MailSendStatus      sendStatus;
    private Date                sentDate;
    private boolean             saveNSend = true;
    private String              ccAddress;
    
    @ManyToOne
    public OrderChargeback getOrderChargeback() {
        return orderChargeback;
    }

    public void setOrderChargeback(OrderChargeback orderChargeback) {
        this.orderChargeback = orderChargeback;
    }

    @Enumerated
    @Column(nullable = false)
    public MailCategory getMailCategory() {
        return mailCategory;
    }

    public void setMailCategory(MailCategory mailCategory) {
        this.mailCategory = mailCategory;
    }

    @ManyToOne
    public MailTemplate getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(MailTemplate mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    @Column(length = 120)
    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    @Column(length = 120)
    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    @Column(length = 120)
    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    @Lob    
    @Column(length = 40000,columnDefinition = "TEXT")
    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    @Enumerated
    public MailSendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(MailSendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    @Transient
    public boolean isSaveNSend() {
        return saveNSend;
    }

    public void setSaveNSend(boolean saveNSend) {
        this.saveNSend = saveNSend;
    }

    @Column(length = 600)
    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }
    
    
    
    @Override
    public ChargebackMail clone() throws CloneNotSupportedException {
        ChargebackMail cbm = (ChargebackMail)super.clone();
        cbm.setId(null);
        return cbm;
    }
}
