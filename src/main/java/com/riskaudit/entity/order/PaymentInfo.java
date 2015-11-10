package com.riskaudit.entity.order;

import com.riskaudit.entity.bank.Bank;
import com.riskaudit.entity.base.User;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.PaymentMethod;
import com.riskaudit.enums.PaymentSecureType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

/**
 *
 * @author asenturk
 */
@Embeddable
public class PaymentInfo {
    
    private PaymentMethod       paymentMethod;
    private PaymentSecureType   paymentSecureType;
    private User                fraudController;
    private Bank                posBank;
    private String              creditCardNo;
    private Bank                cardBank;
    private String              cardHolder;
    private Double              payAmount;
    private Currency            payCurrency;
    
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

    @ManyToOne
    public Bank getPosBank() {
        return posBank;
    }

    public void setPosBank(Bank posBank) {
        this.posBank = posBank;
    }

    @Column(length = 20)
    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
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

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    @Enumerated(EnumType.STRING)
    public Currency getPayCurrency() {
        return payCurrency;
    }

    public void setPayCurrency(Currency payCurrency) {
        this.payCurrency = payCurrency;
    }

    
    
}
