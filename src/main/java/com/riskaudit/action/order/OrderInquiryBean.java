package com.riskaudit.action.order;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.base.User;
import com.riskaudit.entity.order.OrderInfo;
import com.riskaudit.entity.order.OrderInquiry;
import com.riskaudit.entity.order.OrderProduct;
import com.riskaudit.entity.order.OrderStatus;
import com.riskaudit.entity.order.PaymentInfo;
import com.riskaudit.entity.order.ProductCategory;
import com.riskaudit.entity.order.ProductSubCategory;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.MarketPlace;
import com.riskaudit.util.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named(value = "orderInquiry")
@ViewScoped
public class OrderInquiryBean extends BaseAction<OrderInquiry> {

    @Override
    public OrderInquiry getInstance() {
       try{
            if(super.getInstance()==null){
                if(super.getId()!=null && super.getId().getValue()!=null && super.getId().getValue()>0){
                    super.setInstance(getCrud().find(OrderInquiry.class,super.getId().getValue()));                
                }else{
                    super.setInstance(new OrderInquiry());
                    super.getInstance().setOrderInfo(new OrderInfo());
                    super.getInstance().getOrderInfo().setMarketPlace(MarketPlace.WEB);
                    super.getInstance().getOrderInfo().setOrderCurrency(Currency.TRY);
                }
            }else{                
                if(super.getInstance().getOrderInfo()==null){
                    super.getInstance().setOrderInfo(new OrderInfo());
                }
                if(super.getInstance().getPaymentInfo()==null){
                    super.getInstance().setPaymentInfo(new PaymentInfo());
                }
                if(super.getInstance().getOrderInfo().getMarketPlace()==null){
                    super.getInstance().getOrderInfo().setMarketPlace(MarketPlace.WEB);
                }
                if(super.getInstance().getOrderInfo().getOrderCurrency()==null){
                    super.getInstance().getOrderInfo().setOrderCurrency(Currency.TRY);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       
       return super.getInstance();
    }

    private HashMap<String, Object>     paramsHashByMerchant        = Helper.getParamsHashByMerchant();
    
    
    private List<User>                  merchantFraudControllers    = new ArrayList<User>();
    private List<OrderStatus>           orderStatuses               = new ArrayList<OrderStatus>();
    private List<ProductCategory>       prodcutCategories           = new ArrayList<ProductCategory>();
    private List<ProductSubCategory>    productSubCategories        = new ArrayList<ProductSubCategory>();

    
    public void newOrderProduct(){
        System.out.println("aaaaaaa");
        super.getInstance().getOrderProducts().add(new OrderProduct());
        System.out.println("Product.Size..:" + super.getInstance().getOrderProducts().size());
    }
    
    public List<OrderStatus> getOrderStatuses() {
        if(orderStatuses.isEmpty()){
            orderStatuses.addAll(getCrud().getNamedList("OrderStatus.findAllMerchantOrderStatus",paramsHashByMerchant));
        }
        return orderStatuses;
    }

    public void setOrderStatuses(List<OrderStatus> orderStatuses) {
        this.orderStatuses = orderStatuses;
    }

    public List<User> getMerchantFraudControllers() {
        if(merchantFraudControllers.isEmpty()){
            merchantFraudControllers.addAll(getCrud().getNamedList("User.findMerchantFraudControllers",paramsHashByMerchant));
        }
        return merchantFraudControllers;
    }

    public void setMerchantFraudControllers(List<User> merchantFraudControllers) {
        this.merchantFraudControllers = merchantFraudControllers;
    }

    public List<ProductCategory> getProdcutCategories() {
        if(prodcutCategories.isEmpty()){
            prodcutCategories.addAll(getCrud().getNamedList("ProductCategory.findAllMerchantProductCategories",paramsHashByMerchant));
        }
        return prodcutCategories;
    }

    public void setProdcutCategories(List<ProductCategory> prodcutCategories) {
        
        this.prodcutCategories = prodcutCategories;
    }

    public List<ProductSubCategory> getProductSubCategories() {
        return productSubCategories;
    }

    public void setProductSubCategories(List<ProductSubCategory> productSubCategories) {
        this.productSubCategories = productSubCategories;
    }
    
    
    
}
