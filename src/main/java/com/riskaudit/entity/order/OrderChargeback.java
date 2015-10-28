package com.riskaudit.entity.order;

import com.riskaudit.entity.bank.Bank;
import com.riskaudit.entity.bank.ChargebackCode;
import com.riskaudit.entity.bank.ChargebackReason;
import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.enums.ChargebackProcessType;
import com.riskaudit.enums.Currency;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "OrderChargeback.findOrderChargebacks",query = "select d from OrderChargeback d where d.orderInquiry.id=:orderinqid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class OrderChargeback extends BaseEntity{
    
    private OrderInquiry            orderInquiry;
    private ChargebackProcessType   processType;
    private Bank                    bank;
    private String                  creditCardNo;
    private String                  iban;
    private ChargebackCode          chargebackCode;
    private ChargebackReason        charebackReason;    
    private Date                    processDate;
    private Date                    declarationDate;
    private Double                  total;
    private Currency                currency;
    private String                  comment;
    private Bank                    creditCardProvider;
    
    @ManyToOne
    public OrderInquiry getOrderInquiry() {
        return orderInquiry;
    }

    public void setOrderInquiry(OrderInquiry orderInquiry) {
        this.orderInquiry = orderInquiry;
    }

    @Enumerated(EnumType.STRING)
    public ChargebackProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ChargebackProcessType processType) {
        this.processType = processType;
    }

    @ManyToOne
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Column(length = 19,nullable = true)
    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    @Column(length = 60,nullable = true)
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @ManyToOne
    public ChargebackCode getChargebackCode() {
        return chargebackCode;
    }

    public void setChargebackCode(ChargebackCode chargebackCode) {
        this.chargebackCode = chargebackCode;
    }

    @ManyToOne
    public ChargebackReason getCharebackReason() {
        return charebackReason;
    }

    public void setCharebackReason(ChargebackReason charebackReason) {
        this.charebackReason = charebackReason;
    }

    @Temporal(TemporalType.DATE)
    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    @Temporal(TemporalType.DATE)
    public Date getDeclarationDate() {
        return declarationDate;
    }

    public void setDeclarationDate(Date declarationDate) {
        this.declarationDate = declarationDate;
    }


    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Enumerated(EnumType.STRING)
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Column(length = 4000,nullable = true)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne
    public Bank getCreditCardProvider() {
        return creditCardProvider;
    }

    public void setCreditCardProvider(Bank creditCardProvider) {
        this.creditCardProvider = creditCardProvider;
    }
    
    
    
}
