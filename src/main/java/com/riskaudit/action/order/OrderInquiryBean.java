package com.riskaudit.action.order;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.base.Merchant;
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
import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.component.api.UIData;
import org.primefaces.event.RowEditEvent;

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

    private Merchant    merchant = Helper.getCurrentUserMerchant();
    
    public void newOrderProduct(){
        OrderProduct product = new OrderProduct();
        product.setOrderInquiry(getInstance());
        super.getInstance().getOrderProducts().add(product);
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
            prodcutCategories.addAll(getCrud().getNamedList("ProductCategory.findAllMerchantOnlyProductCategories",paramsHashByMerchant));
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
    
 
    public void onRowEdit(RowEditEvent event) {
        try{
            OrderProduct product = ((OrderProduct) event.getObject());
            
            if(product.isManaged()){
                if(product.getOrderInquiry()==null){
                    product.setOrderInquiry(getInstance());
                }
                getCrud().updateObject(product);
            }            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
     
    public void onRowCancel(RowEditEvent event) {
        
    }

    private void loadSubCategories(Long id){
        productSubCategories = new ArrayList<ProductSubCategory>();
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("prdctcatid",id);
        productSubCategories.addAll(getCrud().getNamedList("ProductSubCategory.findAllProductSubCategories",params));
        System.out.println("productSubCategories..:" + productSubCategories.size());
    }
    public void categoryValueChange(ValueChangeEvent e){
        UIData data = (UIData) e.getComponent().findComponent("orderProductTable");
        ProductCategory myNewValue = (ProductCategory)e.getNewValue();
        ProductCategory myOldValue = (ProductCategory)e.getOldValue();
        if(myNewValue!=null && myOldValue!=null && !myNewValue.getId().equals(myOldValue.getId())){
            loadSubCategories(myNewValue.getId());
        }
    }
    
    public void removeOrderProductRecord(OrderProduct product){
        try{    
            System.out.println("product.isManaged()..:" + product.isManaged());
            if(product.isManaged()){
                product.setStatus(Status.DELETED);
                getCrud().deleteObject(product);
            }else{
                getInstance().getOrderProducts().remove(product);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void save(){
        try{
            if(getInstance().getMerchant()==null){
                getInstance().setMerchant(merchant);
            }
            if(getInstance().isManaged()){                
                getCrud().updateObject(getInstance());
                
                Helper.addMessage(Helper.getMessage("Global.Record.Updated"));
            }else{
                getCrud().createObject(getInstance());
                Helper.addMessage(Helper.getMessage("Global.Record.Added"));
                
            }
            super.setList(new ArrayList<OrderInquiry>());
        }catch(Exception e){
            Helper.addMessage("HATA..:" + e.getMessage());
        }
    }

    @Override
    public void newRecord() throws InstantiationException, IllegalAccessException {
        System.out.println("aaaaa");
        super.newRecord(); 
    }
    
    
}
