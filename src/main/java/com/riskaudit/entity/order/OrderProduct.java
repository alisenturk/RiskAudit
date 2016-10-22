package com.riskaudit.entity.order;

import com.riskaudit.entity.base.BaseEntity;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Cacheable(true)
@Entity
@XmlRootElement
public class OrderProduct extends BaseEntity{
    
    private OrderInquiry            orderInquiry;
    private String                  productCode;
    private String                  productName;
    private OrderProductCategory    category;
    private Double                  price           = 0D;
    private Integer                 quantity        = 0;
    private Double                  totalPrice      = 0D;
    private Seller                  seller          ;
    private String                  cargoFirmCode   ;
    private String                  cargoFirmName   ;
    private String                  cargoTrackNo    ;
    private Boolean                 objection       = true;

    @ManyToOne
    public OrderInquiry getOrderInquiry() {
        return orderInquiry;
    }

    public void setOrderInquiry(OrderInquiry orderInquiry) {
        this.orderInquiry = orderInquiry;
    }

    @NotNull
    @Column(length = 60)
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @NotNull
    @Column(length = 200)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Embedded
    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    @Column(length = 120)
    public String getCargoFirmName() {
        return cargoFirmName;
    }

    public void setCargoFirmName(String cargoFirmName) {
        this.cargoFirmName = cargoFirmName;
    }

    @Column(length = 20)
    public String getCargoTrackNo() {
        return cargoTrackNo;
    }

    public void setCargoTrackNo(String cargoTrackNo) {
        this.cargoTrackNo = cargoTrackNo;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")   
    public Boolean getObjection() {
        return objection;
    }

    public void setObjection(Boolean objection) {
        this.objection = objection;
    }

    @Embedded
    public OrderProductCategory getCategory() {
        return category;
    }

    public void setCategory(OrderProductCategory category) {
        this.category = category;
    }

    public String getCargoFirmCode() {
        return cargoFirmCode;
    }

    public void setCargoFirmCode(String cargoFirmCode) {
        this.cargoFirmCode = cargoFirmCode;
    }

    @Override
    public String toString() {
        return "OrderProduct{" + "orderInquiry=" + orderInquiry + ", productCode=" + productCode + ", productName=" + productName + ", category=" + category + ", price=" + price + ", quantity=" + quantity + ", totalPrice=" + totalPrice + ", seller=" + seller + ", cargoFirmCode=" + cargoFirmCode + ", cargoFirmName=" + cargoFirmName + ", cargoTrackNo=" + cargoTrackNo + ", objection=" + objection + '}';
    }

    
    
    
}
