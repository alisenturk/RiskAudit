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
import com.riskaudit.entity.order.OrderProductCategory;
import com.riskaudit.entity.order.OrderStatus;
import com.riskaudit.entity.order.PaymentInfo;
import com.riskaudit.entity.order.ProductCategory;
import com.riskaudit.entity.order.ProductSubCategory;
import com.riskaudit.entity.order.Seller;
import com.riskaudit.enums.ChargebackProcessType;
import com.riskaudit.enums.Constants;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.MarketPlace;
import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author asenturk
 */
@Named(value = "orderInquiry")
@ViewScoped
public class OrderInquiryBean extends BaseAction<OrderInquiry> {

    @Inject
    ChargebackAction chargebackAction;
    
    @Inject
    MerchantOrderSearch merchantOrderSearch;
    
    private String  searchOrderNo = "";
    private Boolean required = false;
    
    private HashMap<String, Object>     paramsHashByMerchant        = Helper.getParamsHashByMerchant();
    
    private List<User>                  merchantFraudControllers    = new ArrayList<User>();
    private List<OrderStatus>           orderStatuses               = new ArrayList<OrderStatus>();
    private List<ProductCategory>       prodcutCategories           = new ArrayList<ProductCategory>();
    private List<ProductSubCategory>    productSubCategories        = new ArrayList<ProductSubCategory>();
    private List<Agent>                 agents                      = new ArrayList<Agent>();
    private List<Bank>                  posBanks                    = new ArrayList<Bank>();
    private List<Bank>                  cardBanks                   = new ArrayList<Bank>();
    
    private List<com.riskaudit.restws.data.order.OrderInfo> searchOrderInfoList = new ArrayList<com.riskaudit.restws.data.order.OrderInfo>();
    
    private Merchant    merchant = Helper.getCurrentUserMerchant();

    private OrderProduct    currentProduct = null;
    
    private Boolean openProductPanel = false;
    
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
                    super.getInstance().getOrderInfo().setMemberName("");
                    super.getInstance().getOrderInfo().setMemberSurname("");
                    super.getInstance().getOrderInfo().setMemberUsername("");
                    super.getInstance().getOrderInfo().setOrderNo("");
                    super.getInstance().getOrderInfo().setOrderTotal(0d);
                    
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
                boolean isError = false;
                if(getInstance()!=null){
                    if(getInstance().getOrderInfo().getOrderNo()==null
                       ||getInstance().getOrderInfo().getOrderNo().length()<1){
                        Helper.addMessage("Lütfen Sipariş numarasını giriniz.",FacesMessage.SEVERITY_ERROR);
                        isError=true;
                    }
                    if( getInstance().getOrderInfo().getOrderNo()!=null && 
                        getInstance().getOrderInfo().getOrderNo().length()>1 &&
                        this.checkOrderNo(getInstance().getOrderInfo().getOrderNo())
                           ){
                        Helper.addMessage("Bu sipariş numarası için daha önce giriş yapılmış.",FacesMessage.SEVERITY_ERROR);
                        isError = true;
                    }
                }
                if(!isError){
                    getCrud().createObject(getInstance());
                    Helper.addMessage(Helper.getMessage("Global.Record.Added"));
                    chargebackAction.setOrderInquiry(getInstance());
                    chargebackAction.init();
                }
            }
            super.setList(new ArrayList<OrderInquiry>());
        }catch(Exception e){
            Helper.addMessage("HATA..:" + e.getMessage(),FacesMessage.SEVERITY_FATAL);
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
            }else if(cardBanks.size()==0){
                cardBanks.addAll(getCrud().getNamedList("Bank.findAll"));
            }
            
        }
    }

    public String getSearchOrderNo() {
        return searchOrderNo;
    }

    public void setSearchOrderNo(String searchOrderNo) {
        this.searchOrderNo = searchOrderNo;
    }

    public List<com.riskaudit.restws.data.order.OrderInfo> getSearchOrderInfoList() {
        return searchOrderInfoList;
    }

    public void setSearchOrderInfoList(List<com.riskaudit.restws.data.order.OrderInfo> searchOrderInfoList) {
        this.searchOrderInfoList = searchOrderInfoList;
    }
    
    public void searchMerchantOrders(){
        searchOrderInfoList.clear();
        if(searchOrderNo!=null && searchOrderNo.length()>0){
            merchantOrderSearch.setOrderNo(searchOrderNo);
            searchOrderInfoList.addAll(merchantOrderSearch.orderInfoList());
        }
    }
    private Agent findAgent(String agentCode){
        HashMap<String, Object>  params  = new HashMap<String,Object>();
        params.put("mrchntid", merchant.getId());
        params.put("code",agentCode);
        
        return getCrud().findEntity(Agent.class,"Agent.findMerchantAgentByCode",params);
    }
    private OrderStatus findOrderStatus(String statusCode){
        HashMap<String, Object>  params  = new HashMap<String,Object>();
        params.put("mrchntid", merchant.getId());
        params.put("code",statusCode);
        
        return getCrud().findEntity(OrderStatus.class,"OrderStatus.findMerchantOrderStatusByCode",params);
    }
    private ProductCategory findProductCategory(String categoryCode){
        HashMap<String, Object>  params  = new HashMap<String,Object>();
        params.put("mrchntid", merchant.getId());
        params.put("catCode",categoryCode);
        
        return getCrud().findEntity(ProductCategory.class,"ProductCategory.findMerchantProductCategoryByCode",params);
    }
    private ProductSubCategory findSubCategory(String categoryCode,String subCategoryCode){
        HashMap<String, Object>  params  = new HashMap<String,Object>();
        params.put("mrchntid", merchant.getId());
        params.put("catCode",categoryCode);
        params.put("subCode", subCategoryCode);
        
        return getCrud().findEntity(ProductSubCategory.class,"ProductSubCategory.findProductSubCategoryByCode",params);
    }
    public void selectMerchantOrder(com.riskaudit.restws.data.order.OrderInfo order){
        try{
            if(getInstance()!=null && order!=null){
                required = true;
                
                getInstance().getOrderInfo().setOrderNo(order.getOrderNo());
                getInstance().getOrderInfo().setOrderDate(Helper.string2Date(order.getOrderDate(),"yyyyMMdd"));
                getInstance().getOrderInfo().setMemberName(order.getMemberName());
                getInstance().getOrderInfo().setMemberSurname(order.getMemberSurname());
                getInstance().getOrderInfo().setMemberUsername(order.getMemberUsername());
                getInstance().getOrderInfo().setMarketPlace(MarketPlace.valueOf(order.getMarketPlace()));
                getInstance().getOrderInfo().setOrderTotal(order.getOrderAmount());
                getInstance().getOrderInfo().setOrderCurrency(Currency.valueOf(order.getCurrency()));
                getInstance().getOrderInfo().setAgent(findAgent(order.getAgentCode()));
                getInstance().getOrderInfo().setOrderStatus(findOrderStatus(order.getStatusCode()));
                if(getInstance().getOrderProducts()==null){
                    getInstance().setOrderProducts(new ArrayList<OrderProduct>());
                }
                
                
                OrderProduct op = null;
                for(com.riskaudit.restws.data.order.OrderProduct prdct: order.getProducts()){
                    op = new OrderProduct();
                    op.setOrderInquiry(getInstance());
                    op.setProductCode(prdct.getProductCode());
                    op.setProductName(prdct.getProudctName());
                    //op.setProductCategory(findProductCategory(prdct.getCategoryCode()));
                    //op.setProductSubCategory(findSubCategory(prdct.getCategoryCode(),prdct.getSubCategoryCode()));
                    op.setQuantity(prdct.getQuantity());
                    op.setPrice(prdct.getPrice());
                    op.setStatus(Status.ACTIVE);
                    getInstance().getOrderProducts().add(op);
                } 
               
                
            }        
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
    
    public boolean checkOrderNo(String orderNo){
        boolean usedOrderNo = false;
        try{   
            OrderInquiryQuery query = new OrderInquiryQuery();
            query.setOrderNo(orderNo);
            query.setMerchant(merchant);
            
            List inquiries = new ArrayList<>();
            inquiries.addAll(getCrud().getList(query.getInqueryQuery(),query.getParams()));
            if(inquiries!=null && inquiries.size()>0){
                usedOrderNo = true;
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return usedOrderNo;
    }
    
    public void createProduct(){
        currentProduct = new OrderProduct();
        currentProduct.setProductName("");
        currentProduct.setSeller(new Seller());
        currentProduct.setCategory(new OrderProductCategory());
        openProductPanel = true;
    }
    public void saveProduct(){
        System.out.println(currentProduct.toString());
        currentProduct.setOrderInquiry(getInstance());
        if(currentProduct.isManaged()){
            getCrud().updateObject(currentProduct);
        }else{
            getCrud().createObject(currentProduct);
        }
        createProduct();
        openProductPanel = false;
    }
    public OrderProduct getCurrentProduct() {
        if(currentProduct==null){
            createProduct();
            openProductPanel = false;
        }
        return currentProduct;
    }

    public void setCurrentProduct(OrderProduct currentProduct) {
        this.currentProduct = currentProduct;
    }

    public Boolean getOpenProductPanel() {
        return openProductPanel;
    }

    public void setOpenProductPanel(Boolean openProductPanel) {
        this.openProductPanel = openProductPanel;
    }
    
    public void onRowSelect(SelectEvent event) {
        openProductPanel = true;
    }
 
    public void onRowUnselect(UnselectEvent event) {
        openProductPanel = false;
    }
    
}
