package com.riskaudit.entity.order;

import com.riskaudit.entity.base.User;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.PaymentMethod;
import com.riskaudit.enums.PaymentSecureType;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author asenturk
 */
@Embeddable
public class PaymentInfo {
    
    private PaymentMethod       paymentMethod;
    private PaymentSecureType   paymentSecureType;
    private User                fraudController;
    private Boolean             refunded;
    private Date                refundedDate;
    private Double              refundedTotal;
    private Currency            refundCurrency;
    private Boolean             willCollection;
    private Date                collectionDate;
    private Double              collectionTotal;
    private Currency            collectionCurrency;

    @Enumerated(EnumType.STRING)
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Enumerated(EnumType.STRING)
    public PaymentSecureType getPaymentSecureType() {
        return paymentSecureType;
    }

    public void setPaymentSecureType(PaymentSecureType paymentSecureType) {
        this.paymentSecureType = paymentSecureType;
    }

    @ManyToOne
    public User getFraudController() {
        return fraudController;
    }

    public void setFraudController(User fraudController) {
        this.fraudController = fraudController;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")   
    public Boolean getRefunded() {
        return refunded;
    }

    public void setRefunded(Boolean refunded) {
        this.refunded = refunded;
    }

    @Temporal(TemporalType.DATE)
    public Date getRefundedDate() {
        return refundedDate;
    }

    public void setRefundedDate(Date refundedDate) {
        this.refundedDate = refundedDate;
    }

    public Double getRefundedTotal() {
        return refundedTotal;
    }

    public void setRefundedTotal(Double refundedTotal) {
        this.refundedTotal = refundedTotal;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")   
    public Boolean getWillCollection() {
        return willCollection;
    }

    public void setWillCollection(Boolean willCollection) {
        this.willCollection = willCollection;
    }

    @Temporal(TemporalType.DATE)
    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public Double getCollectionTotal() {
        return collectionTotal;
    }

    public void setCollectionTotal(Double collectionTotal) {
        this.collectionTotal = collectionTotal;
    }

    @Enumerated(EnumType.STRING)
    public Currency getRefundCurrency() {
        return refundCurrency;
    }

    public void setRefundCurrency(Currency refundCurrency) {
        this.refundCurrency = refundCurrency;
    }

    @Enumerated(EnumType.STRING)
    public Currency getCollectionCurrency() {
        return collectionCurrency;
    }

    public void setCollectionCurrency(Currency collectionCurrency) {
        this.collectionCurrency = collectionCurrency;
    }
    
    
    
}
