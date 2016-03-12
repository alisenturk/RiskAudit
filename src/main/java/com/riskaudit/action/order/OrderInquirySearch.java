package com.riskaudit.action.order;

import com.riskaudit.action.base.CrudService;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.base.User;
import com.riskaudit.entity.order.OrderInquiry;
import com.riskaudit.enums.ChargebackProcessType;
import com.riskaudit.enums.Constants;
import com.riskaudit.util.Helper;
import com.riskaudit.util.JSFHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named(value = "inquirySearch")
@ViewScoped
public class OrderInquirySearch implements Serializable{
    @Inject
    CrudService crud;
        
    @Inject
    JSFHelper   jsfHelper;
    
    private String      orderNo;
    private Date        orderBeginDate;
    private Date        orderEndDate;
    private Date        objectionBeginDate;
    private Date        objectionEndDate;
    private Date        chargebackBeginDate;
    private Date        chargebackEndDate;
    private String      memberName;
    private String      memberSurname;
    private String      memberUsername;
    private User        fraudController;
    private Merchant    merchant = Helper.getCurrentUserMerchant();
    
    private ChargebackProcessType processType;
            
    private List<User>              merchantFraudControllers    = new ArrayList<User>();
    private List<OrderInquiry>      inquiries                   = new ArrayList<OrderInquiry>();

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public List<User> getMerchantFraudControllers() {
        if(merchantFraudControllers.isEmpty()){
            merchantFraudControllers.addAll(crud.getNamedList("User.findMerchantFraudControllers",Helper.getParamsHashByMerchant()));
        }
        return merchantFraudControllers;
    }

    public void setMerchantFraudControllers(List<User> merchantFraudControllers) {
        this.merchantFraudControllers = merchantFraudControllers;
    }

    public List<OrderInquiry> getInquiries() {
        if(inquiries.isEmpty() && jsfHelper.getSessionValue(Constants.InquirySearchResult.getValue())!=null){
            inquiries = (List)jsfHelper.getSessionValue(Constants.InquirySearchResult.getValue());
        }
        return inquiries;
    }
    
    public void setInquiries(List<OrderInquiry> inquiries) {
        this.inquiries = inquiries;
    }
    
    
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderBeginDate() {
        return orderBeginDate;
    }

    public void setOrderBeginDate(Date orderBeginDate) {
        this.orderBeginDate = orderBeginDate;
    }

    public Date getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(Date orderEndDate) {
        this.orderEndDate = orderEndDate;
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

    public User getFraudController() {
        return fraudController;
    }

    public void setFraudController(User fraudController) {
        this.fraudController = fraudController;
    }

    public Date getObjectionBeginDate() {
        return objectionBeginDate;
    }

    public void setObjectionBeginDate(Date objectionBeginDate) {
        this.objectionBeginDate = objectionBeginDate;
    }

    public Date getObjectionEndDate() {
        return objectionEndDate;
    }

    public void setObjectionEndDate(Date objectionEndDate) {
        this.objectionEndDate = objectionEndDate;
    }

    public Date getChargebackBeginDate() {
        return chargebackBeginDate;
    }

    public void setChargebackBeginDate(Date chargebackBeginDate) {
        this.chargebackBeginDate = chargebackBeginDate;
    }

    public Date getChargebackEndDate() {
        return chargebackEndDate;
    }

    public void setChargebackEndDate(Date chargebackEndDate) {
        this.chargebackEndDate = chargebackEndDate;
    }

    public ChargebackProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ChargebackProcessType processType) {
        this.processType = processType;
    }
    
    
    
    
     public void searchInquiry(){
        inquiries = new ArrayList<>();
        
        try{   
            jsfHelper.removeSessionValue(Constants.InquirySearchResult.getValue());
            OrderInquiryQuery query = new OrderInquiryQuery();
            query.setOrderNo(orderNo);
            query.setOrderBeginDate(orderBeginDate);
            query.setOrderEndDate(orderEndDate);
            query.setMemberUsername(memberUsername);
            query.setMemberName(memberName);
            query.setMemberSurname(memberSurname);
            query.setFraudController(fraudController);
            query.setMerchant(merchant);
            query.setProcessType(processType);
            query.setAppealBeginDate(objectionBeginDate);
            query.setAppealEndDate(objectionEndDate);
            query.setChargebackBeginDate(chargebackBeginDate);
            query.setChargebackEndDate(chargebackEndDate);
            
            inquiries.addAll(crud.getList(query.getInqueryQuery(),query.getParams()));
            if(inquiries!=null && inquiries.size()>0){
                jsfHelper.setSessionValue(Constants.InquirySearchResult.getValue(),inquiries);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}
