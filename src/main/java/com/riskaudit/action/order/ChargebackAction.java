package com.riskaudit.action.order;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.bank.Bank;
import com.riskaudit.entity.bank.ChargebackCode;
import com.riskaudit.entity.bank.ChargebackReason;
import com.riskaudit.entity.bank.CreditCardBin;
import com.riskaudit.entity.order.OrderChargeback;
import com.riskaudit.entity.order.OrderChargebackComment;
import com.riskaudit.entity.order.OrderInquiry;
import com.riskaudit.entity.order.PaymentInfo;
import com.riskaudit.enums.ChargebackProcessType;
import com.riskaudit.enums.CreditCardProvider;
import com.riskaudit.enums.Currency;
import com.riskaudit.util.Helper;
import com.riskaudit.util.JSFHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class ChargebackAction extends BaseAction<OrderChargeback>{
    @Inject
    JSFHelper helper;
    
    private OrderInquiry            orderInquiry;
    private OrderChargebackComment  comment = new OrderChargebackComment();
    
    private List<Bank>                      banks               = new ArrayList<Bank>();
    private List<Bank>                      cardBanks           = new ArrayList<Bank>();
    private List<ChargebackCode>            chargebackCodes     = new ArrayList<ChargebackCode>();
    private List<ChargebackReason>          chargebackReasons   = new ArrayList<ChargebackReason>();
    private List<OrderChargeback>           orderChargebacks    = new ArrayList<OrderChargeback>();
    private List<OrderChargebackComment>    comments            = new ArrayList<OrderChargebackComment>();
    
    
    public OrderInquiry getOrderInquiry() {
        return orderInquiry;
    }

    public void setOrderInquiry(OrderInquiry orderInquiry) {
        this.orderInquiry = orderInquiry;
    }

    public List<Bank> getBanks() {
        if(banks.isEmpty()){
            banks.addAll(getCrud().getNamedList("Bank.findAll"));
        }
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public List<ChargebackCode> getChargebackCodes() {
        if(chargebackCodes.isEmpty()){
            chargebackCodes.addAll(getCrud().getNamedList("ChargebackCode.findAll"));
        }
        return chargebackCodes;
    }

    public void setChargebackCodes(List<ChargebackCode> chargebackCodes) {
        this.chargebackCodes = chargebackCodes;
    }

    public List<ChargebackReason> getChargebackReasons() {
        if(chargebackReasons.isEmpty()){
            chargebackReasons.addAll(getCrud().getNamedList("ChargebackReason.findAll"));
        }
        return chargebackReasons;
    }

    public void setChargebackReasons(List<ChargebackReason> chargebackReasons) {
        this.chargebackReasons = chargebackReasons;
    }

    public List<OrderChargeback> getOrderChargebacks() {
        if(getOrderInquiry()!=null && getOrderInquiry().isManaged() && orderChargebacks.isEmpty()){
            HashMap<String,Object> offerMap = new HashMap<String,Object>();
            offerMap.put("orderinqid",getOrderInquiry().getId());
            orderChargebacks.addAll(getCrud().getNamedList("OrderChargeback.findOrderChargebacks",offerMap));
        }
        return orderChargebacks;
    }

    public void setOrderChargebacks(List<OrderChargeback> orderChargebacks) {
        this.orderChargebacks = orderChargebacks;
    }
    
    @Override
    public void save() {
       
        try{
            if(orderInquiry!=null && orderInquiry.isManaged()){
                if(getInstance().isManaged()){                    
                    getCrud().updateObject(getInstance());
                    Helper.addMessage(Helper.getMessage("Global.Record.Updated"));
                }else{
                    getInstance().setOrderInquiry(orderInquiry);
                    getCrud().createObject(getInstance());
                    Helper.addMessage(Helper.getMessage("Global.Record.Added"));
                }

                
            }
        }catch(Exception e){
            e.printStackTrace();
            Helper.addMessage("HATA..:" + e.getMessage());
        }
        
    }
    
    public void init(){
        if(!getInstance().isManaged() && getOrderInquiry()!=null && getOrderInquiry().isManaged()){
            HashMap<String,Object> params = new HashMap<String, Object>();
            params.put("orderinqid",getOrderInquiry().getId());
            List<OrderChargeback> chargebacks = getCrud().getNamedList("OrderChargeback.findOrderChargebacks", params);
            if(chargebacks!=null && chargebacks.size()>0){
                setInstance(chargebacks.get(0));
                loadProviderBankAndChargebackCodes();
            }else{
                PaymentInfo pay = getOrderInquiry().getPaymentInfo();
                getInstance().setCardBank(pay.getCardBank());
                getInstance().setCardHolder(pay.getCardHolder());
                getInstance().setCreditCardNo(pay.getCreditCardNo());
                getInstance().setCurrency(pay.getPayCurrency());
                getInstance().setPosBank(pay.getPosBank());
                getInstance().setTotal(pay.getPayAmount());
                getInstance().setProcessType(ChargebackProcessType.APPEAL);
            }
        }
    }

    public List<Bank> getCardBanks() {
        return cardBanks;
    }

    public void setCardBanks(List<Bank> cardBanks) {
        this.cardBanks = cardBanks;
    }
    
    
    public CreditCardProvider getCardProviderByBin(String bin){
        HashMap<String,Object> prms = new HashMap<String,Object>();
        prms.put("bin",bin);
        List<CreditCardBin> list = getCrud().getNamedList("CreditCardBin.findCreditCardBinByBin", prms);
        if(list.size()>0){
          return list.get(0).getProvider();            
        }
        
        return null;
    }
    public void loadChargebackCodes(CreditCardProvider provider){
        HashMap<String,Object> prms = new HashMap<String,Object>();
        prms.put("prvdr",provider);
        List<ChargebackCode> list = getCrud().getNamedList("ChargebackCode.findAllByProvider", prms);
        if(list.size()>0){
            chargebackCodes.clear();
            chargebackCodes.addAll(list);
        }else{
            chargebackCodes.clear();
        }
    }
    public void loadProviderBankAndChargebackCodes(){
            cardBanks = new ArrayList<Bank>();
            String bin = getInstance().getCreditCardNo().substring(0,7).replace("-","");
            HashMap<String,Object> prms = new HashMap<String,Object>();
            prms.put("bin",bin);
            cardBanks.addAll(getCrud().getNamedList("CreditCardBin.findBankByBin",prms ));
            if(cardBanks.size()>0){
                getInstance().setCardBank(cardBanks.get(0));
            }else{
                cardBanks.addAll(getCrud().getNamedList("Bank.findAll"));
            }
            loadChargebackCodes(getCardProviderByBin(bin));
    }
    public void handleCreditCardKeyEvent(){
        
        if( getInstance()!=null && 
            getInstance().getCreditCardNo()!=null && 
            getInstance().getCreditCardNo().length()>=7 ){
            loadProviderBankAndChargebackCodes();
        }
    }

    public OrderChargebackComment getComment() {
        return comment;
    }

    public void setComment(OrderChargebackComment comment) {
        this.comment = comment;
    }

    public List<OrderChargebackComment> getComments() {
        if(comments.isEmpty()){
            
        }
        return comments;
    }

    public void setComments(List<OrderChargebackComment> comments) {
        this.comments = comments;
    }
    
    public void saveComment(){
        try{
            if(getInstance()!=null && getInstance().getId()>0){
                if(comment!=null && comment.getOrderChargeback()==null){
                    comment.setOrderChargeback(getInstance());
                }
                if(comment!=null && comment.getProcessType()==null){
                    comment.setProcessType(getInstance().getProcessType());
                }
                if(comment!=null && comment.getId()!=null && comment.getId()>0){
                    getCrud().updateObject(comment);
                }else if(comment!=null && (comment.getId()==null || comment.getId()==0)){
                    getCrud().createObject(comment);
                    getInstance().getComments().add(comment);
                }
                comment = new OrderChargebackComment();
                comment.setOrderChargeback(getInstance());
                comment.setProcessType(getInstance().getProcessType());
            }
        }catch(Exception e){
            
        }
    }
    
    public void selectComment(OrderChargebackComment comm){
        comment = comm;
    }
    
    public void removeComment(OrderChargebackComment cmnt){
        if(cmnt!=null && cmnt.isManaged()){
            getInstance().getComments().remove(cmnt);
            getCrud().deleteObject(cmnt);            
        }
    }
}
