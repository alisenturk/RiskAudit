package com.riskaudit.restws.data.order;

import java.io.Serializable;

/**
 *
 * @author asenturk
 */
public class OrderProduct implements Serializable{
    
    private String  productCode;
    private String  proudctName;
    private String  categoryCode;
    private String  subCategoryCode;
    private int     quantity;
    private double  price;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProudctName() {
        return proudctName;
    }

    public void setProudctName(String proudctName) {
        this.proudctName = proudctName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderProduct{" + "productCode=" + productCode + ", proudctName=" + proudctName + ", categoryCode=" + categoryCode + ", subCategoryCode=" + subCategoryCode + ", quantity=" + quantity + ", price=" + price + '}';
    }

    
    
}
