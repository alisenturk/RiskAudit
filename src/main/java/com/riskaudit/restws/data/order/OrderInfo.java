package com.riskaudit.restws.data.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asenturk
 */
public class OrderInfo implements Serializable{
    
    private String  orderNo;
    private String  orderDate; //yyyyMMdd;
    private String  memberName;
    private String  memberSurname;
    private String  memberUsername;
    private String  marketPlace;
    private String  agentCode;
    private double  orderAmount;
    private String  currency;
    private String  statusCode;
    
    private List<OrderProduct> products = new ArrayList<OrderProduct>();

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberSurname() {
        return memberSurname;
    }

    public void setMemberSurname(String memberSurname) {
        this.memberSurname = memberSurname;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public String getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(String marketPlace) {
        this.marketPlace = marketPlace;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "OrderInfo{" + "orderNo=" + orderNo + ", orderDate=" + orderDate + ", memberName=" + memberName + ", memberSurname=" + memberSurname + ", memberUsername=" + memberUsername + ", marketPlace=" + marketPlace + ", agentCode=" + agentCode + ", orderAmount=" + orderAmount + ", currency=" + currency + ", statusCode=" + statusCode + ", products=" + products + '}';
    }

    
    
}
