package com.riskaudit.action.order;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.bank.Bank;
import com.riskaudit.entity.base.Agent;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.base.User;
import com.riskaudit.entity.order.OrderChargeback;
import com.riskaudit.entity.order.OrderInfo;
import com.riskaudit.entity.order.OrderInquiry;
import com.riskaudit.entity.order.OrderProduct;
import com.riskaudit.entity.order.OrderStatus;
import com.riskaudit.entity.order.PaymentInfo;
import com.riskaudit.entity.order.ProductCategory;
import com.riskaudit.entity.order.ProductSubCategory;
import com.riskaudit.enums.ChargebackProcessType;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.MarketPlace;
import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author asenturk
 */
@Named(value = "orderInquiry")
@ViewScoped
public class OrderInquiryBean extends BaseAction<OrderInquiry> {

    @Inject
    ChargebackAction chargebackAction;
    
    private HashMap<String, Object>     paramsHashByMerchant        = Helper.getParamsHashByMerchant();
    
    private List<User>                  merchantFraudControllers    = new ArrayList<User>();
    private List<OrderStatus>           orderStatuses               = new ArrayList<OrderStatus>();
    private List<ProductCategory>       prodcutCategories           = new ArrayList<ProductCategory>();
    private List<ProductSubCategory>    productSubCategories        = new ArrayList<ProductSubCategory>();
    private List<Agent>                 agents                      = new ArrayList<Agent>();
    private List<Bank>                  posBanks                    = new ArrayList<Bank>();
    private List<Bank>                  cardBanks                   = new ArrayList<Bank>();
    
    private Merchant    merchant = Helper.getCurrentUserMerchant();
    
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
    @PostConstruct
    private void postLoad(){        
        chargebackAction.setInstance(new OrderChargeback());
        chargebackAction.getInstance().setProcessType(ChargebackProcessType.CHARGEBACK);
        chargebackAction.getInstance().setCurrency(Currency.TRY);
        if(getInstance()!=null && getInstance().getId()!=null && getInstance().getId()>0){            
            chargebackAction.setOrderInquiry(getInstance());            
            chargebackAction.init();
        }
        loadMarketPlaceAgencies(getInstance().getOrderInfo().getMarketPlace());
        handleCreditCardKeyEvent();
    }
    
    
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
    
    public List<ProductSubCategory> loadSubCategories(ProductCategory prodCat){
        if(prodCat!=null && prodCat.getId()!=null && prodCat.getId()>0){
            loadSubCategories(prodCat.getId());
        }
        return getProductSubCategories();
    }
    
    public void onEditInit(RowEditEvent event){
        /*OrderProduct product = ((OrderProduct) event.getObject());
        System.out.println("product.getProductCategory().getId()..:" + product.getProductCategory().getId());
        if(product!=null && product.getProductCategory()!=null && product.getProductCategory().getId()!=null){
            loadSubCategories(product.getProductCategory().getId());
            
        } */
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
        if(id!=null && id>0){
            productSubCategories = new ArrayList<ProductSubCategory>();
            HashMap<String,Object> params = new HashMap<String,Object>();
            params.put("prdctcatid",id);
            productSubCategories.addAll(getCrud().getNamedList("ProductSubCategory.findAllProductSubCategories",params));
        }
    }
    public void categoryValueChange(ValueChangeEvent e){
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
                chargebackAction.setOrderInquiry(getInstance());
                chargebackAction.init();
            }
            super.setList(new ArrayList<OrderInquiry>());
        }catch(Exception e){
            Helper.addMessage("HATA..:" + e.getMessage());
        }
    }

    @Override
    public void newRecord() throws InstantiationException, IllegalAccessException {
        super.newRecord(); 
    }
    public void marketPlaceValueChange(ValueChangeEvent e){
        MarketPlace myNewValue = (MarketPlace)e.getNewValue();
        MarketPlace myOldValue = (MarketPlace)e.getOldValue();
        if(myNewValue!=null && myOldValue!=null && !myNewValue.equals(myOldValue)){
            loadMarketPlaceAgencies(myNewValue);
        }
    }
    public void loadMarketPlaceAgencies(MarketPlace aplace){
        agents = new ArrayList<Agent>();
        if(merchant!=null && merchant.getId()>0){
            HashMap<String,Object> params2 = new HashMap<String,Object>();
            params2.put("mrchntid",merchant.getId());
            params2.put("aplace",aplace);
            agents.addAll(getCrud().getNamedList("Agent.findAllMerchantAgentsByMarket",params2));
        }
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public List<Bank> getPosBanks() {
        if(posBanks.isEmpty()){
            posBanks.addAll(getCrud().getNamedList("Bank.findAll"));                    
        }
        return posBanks;
    }

    public void setPosBanks(List<Bank> posBanks) {
        this.posBanks = posBanks;
    }

    public List<Bank> getCardBanks() {
        return cardBanks;
    }

    public void setCardBanks(List<Bank> cardBanks) {
        this.cardBanks = cardBanks;
    }
    
    public void handleCreditCardKeyEvent(){
        
        if( getInstance()!=null && getInstance().getPaymentInfo()!=null && 
            getInstance().getPaymentInfo().getCreditCardNo()!=null && 
            getInstance().getPaymentInfo().getCreditCardNo().length()>=7 ){
            cardBanks = new ArrayList<Bank>();
            String bin = getInstance().getPaymentInfo().getCreditCardNo().substring(0,7).replace("-","");
            HashMap<String,Object> prms = new HashMap<String,Object>();
            prms.put("bin",bin);
            cardBanks.addAll(getCrud().getNamedList("CreditCardBin.findBankByBin",prms ));
            if(cardBanks.size()>0){
                getInstance().getPaymentInfo().setCardBank(cardBanks.get(0));
            }
        }
    }
    
}
