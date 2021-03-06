package com.riskaudit.entity.order;

import com.riskaudit.entity.base.Agent;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.MarketPlace;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author asenturk
 */
@Embeddable
public class OrderInfo {
    
    private String      orderNo         = "";
    private Date        orderDate       = null;
    private String      memberName      = "";
    private String      memberSurname   = "";
    private String      memberUsername  = "";
    private String      memberTCKN      = ""; 
    private Double      orderTotal      = 0d;
    private Currency    orderCurrency   = Currency.TRY;
    private MarketPlace marketPlace     = MarketPlace.WEB;
    private OrderStatus orderStatus     = null;
    private Agent       agent           = null;    

    @NotNull
    @Column(length = 20,nullable = false)
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Temporal(TemporalType.DATE)
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    @Size(min = 1,max = 50)
    @NotNull
    @Column(length = 60,nullable = false)
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @NotNull
    @Column(length = 80,nullable = false)
    public String getMemberSurname() {
        return memberSurname;
    }

    public void setMemberSurname(String memberSurname) {
        this.memberSurname = memberSurname;
    }

    @Column(length = 120,nullable = true)
    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    @Enumerated(EnumType.STRING)
    public Currency getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(Currency orderCurrency) {
        this.orderCurrency = orderCurrency;
    }

    @Enumerated(EnumType.STRING)
    public MarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(MarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }

    @ManyToOne
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @ManyToOne
    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @Column(length = 11)
    public String getMemberTCKN() {
        return memberTCKN;
    }

    public void setMemberTCKN(String memberTCKN) {
        this.memberTCKN = memberTCKN;
    }

    
}
