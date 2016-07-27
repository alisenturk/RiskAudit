package com.riskaudit.entity.order;

import com.riskaudit.entity.bank.Bank;
import com.riskaudit.entity.bank.ChargebackCode;
import com.riskaudit.entity.bank.ChargebackReason;
import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.entity.order.chargeback.ChargebackStatus;
import com.riskaudit.entity.order.chargeback.LawReason;
import com.riskaudit.entity.order.chargeback.ProcessProgress;
import com.riskaudit.enums.CaseStatus;
import com.riskaudit.enums.ChargebackProcessType;
import com.riskaudit.enums.CollectionBox;
import com.riskaudit.enums.Currency;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "OrderChargeback.findOrderChargebacks",query = "select d from OrderChargeback d where d.orderInquiry.id=:orderinqid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "OrderChargeback.reminderOrderChargebacks",query = "select d from OrderChargeback d where d.orderInquiry.merchant.id=:mrchntid and d.appealReminderDate>=:today and d.appealReminder=true and d.processType='APPEAL' and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "OrderChargeback.reminderOrderChargebacksRangeDate",query = "select d from OrderChargeback d where d.orderInquiry.merchant.id=:mrchntid and (d.appealReminderDate BETWEEN :startDate and :endDate) and d.appealReminder=true  and d.processType='APPEAL' and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class OrderChargeback extends BaseEntity{
    
    private OrderInquiry            orderInquiry;    
    private Bank                    posBank;
    private String                  creditCardNo;
    private Bank                    cardBank;
    private String                  cardHolder;        
    private String                  iban;
    private ChargebackCode          chargebackCode;
    private ChargebackReason        chargebackReason;    
    private Double                  total;
    private Currency                currency;
    private ChargebackProcessType   processType;
    private Date                    appealDeclarationDate;         
    private Boolean                 appealReminder = false;
    private Date                    appealReminderDate;
    private Date                    chargebackDeclarationDate;
    private Date                    returnDeclarationDate;    
    private boolean                 returnRender = false;
    private boolean                 chargebackRender = false;
    private CollectionBox           collectionBox;
    private Date                    collectionDate;
    private ChargebackStatus        chargebackStatus;
    private ProcessProgress         processProgress;
    private Boolean                 sentLaw = false;
    private LawReason               lawReason;
    private CaseStatus              caseStatus;
    
    private List<OrderChargebackComment>    comments        = new ArrayList<>();
    private List<CustomerCall>              customerCalls   = new ArrayList<>();
    

    @OneToMany(mappedBy = "orderChargeback",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    public List<CustomerCall> getCustomerCalls() {
        return customerCalls;
    }

    public void setCustomerCalls(List<CustomerCall> customerCalls) {
        this.customerCalls = customerCalls;
    }
    
    @OneToMany(mappedBy = "orderChargeback",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    public List<OrderChargebackComment> getComments() {
        return comments;
    }

    public void setComments(List<OrderChargebackComment> comments) {
        this.comments = comments;
    }
    
    @OneToOne
    @JoinColumn(name="orderInquiry_id", unique= true, nullable=true, insertable=true, updatable=true)
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

    
    @Column(length =20,nullable = true)
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
    
    @ManyToOne
    public Bank getPosBank() {
        return posBank;
    }

    public void setPosBank(Bank posBank) {
        this.posBank = posBank;
    }

    @ManyToOne
    public Bank getCardBank() {
        return cardBank;
    }

    public void setCardBank(Bank cardBank) {
        this.cardBank = cardBank;
    }

    @Column(length = 120)
    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    @ManyToOne
    public ChargebackReason getChargebackReason() {
        return chargebackReason;
    }

    public void setChargebackReason(ChargebackReason chargebackReason) {
        this.chargebackReason = chargebackReason;
    }

    @Temporal(TemporalType.DATE)
    public Date getAppealDeclarationDate() {
        return appealDeclarationDate;
    }

    public void setAppealDeclarationDate(Date appealDeclarationDate) {
        this.appealDeclarationDate = appealDeclarationDate;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")   
    public Boolean getAppealReminder() {
        if(appealReminder==null)appealReminder = false;
        return appealReminder;
    }

    public void setAppealReminder(Boolean appealReminder) {
        this.appealReminder = appealReminder;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getAppealReminderDate() {
        return appealReminderDate;
    }

    public void setAppealReminderDate(Date appealReminderDate) {
        this.appealReminderDate = appealReminderDate;
    }

    @Temporal(TemporalType.DATE)
    public Date getChargebackDeclarationDate() {
        return chargebackDeclarationDate;
    }

    public void setChargebackDeclarationDate(Date chargebackDeclarationDate) {
        this.chargebackDeclarationDate = chargebackDeclarationDate;
    }

    @Temporal(TemporalType.DATE)
    public Date getReturnDeclarationDate() {
        return returnDeclarationDate;
    }

    public void setReturnDeclarationDate(Date returnDeclarationDate) {
        this.returnDeclarationDate = returnDeclarationDate;
    }

    @Transient
    public boolean isReturnRender() {
        if(processType!=null && processType.equals(ChargebackProcessType.REFUNDED)){
            returnRender = true;
        }else{
            returnRender = false;
        }
        return returnRender;
    }

    public void setReturnRender(boolean returnRender) {
        this.returnRender = returnRender;
    }

    @Transient
    public boolean isChargebackRender() {
        if(processType!=null && !processType.equals(ChargebackProcessType.APPEAL)){
            chargebackRender = true;
        }else{
            chargebackRender = false;
        }
        return chargebackRender;
    }

    public void setChargebackRender(boolean chargebackRender) {
        this.chargebackRender = chargebackRender;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    public CollectionBox getCollectionBox() {
        return collectionBox;
    }

    public void setCollectionBox(CollectionBox collectionBox) {
        this.collectionBox = collectionBox;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    @Enumerated(EnumType.STRING)
    public ChargebackStatus getChargebackStatus() {
        return chargebackStatus;
    }

    public void setChargebackStatus(ChargebackStatus chargebackStatus) {
        this.chargebackStatus = chargebackStatus;
    }

    @ManyToOne
    public ProcessProgress getProcessProgress() {
        return processProgress;
    }

    public void setProcessProgress(ProcessProgress processProgress) {
        this.processProgress = processProgress;
    }

    @Column(nullable = false,columnDefinition = "TINYINT(1)")
    public Boolean getSentLaw() {
        return sentLaw;
    }

    public void setSentLaw(Boolean sentLaw) {
        this.sentLaw = sentLaw;
    }

    @ManyToOne
    public LawReason getLawReason() {
        return lawReason;
    }

    public void setLawReason(LawReason lawReason) {
        this.lawReason = lawReason;
    }

    @Enumerated(EnumType.STRING)
    public CaseStatus getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(CaseStatus caseStatus) {
        this.caseStatus = caseStatus;
    }
    
    
    
    
    
}
