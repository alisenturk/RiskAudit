package com.riskaudit.entity.order;

import com.riskaudit.entity.base.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author asenturk
 */
@Entity
public class OrderInquiry extends BaseEntity{
    
    private OrderInfo   orderInfo;
    private PaymentInfo paymentInfo;
    
    private List<OrderProduct>      orderProducts       = new ArrayList<OrderProduct>();
    private List<OrderChargeback>   orderChargebacks    = new ArrayList<OrderChargeback>();

    @Embedded
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Embedded
    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @OneToMany(mappedBy = "orderInquiry",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    @OneToMany(mappedBy = "orderInquiry",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    public List<OrderChargeback> getOrderChargebacks() {
        return orderChargebacks;
    }

    public void setOrderChargebacks(List<OrderChargeback> orderChargebacks) {
        this.orderChargebacks = orderChargebacks;
    }
    
    
    
    
}
