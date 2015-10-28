package com.riskaudit.action.order;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.bank.Bank;
import com.riskaudit.entity.bank.ChargebackCode;
import com.riskaudit.entity.bank.ChargebackReason;
import com.riskaudit.entity.order.OrderChargeback;
import com.riskaudit.entity.order.OrderInquiry;
import com.riskaudit.enums.ChargebackProcessType;
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
    
    private List<Bank>              banks               = new ArrayList<Bank>();
    private List<ChargebackCode>    chargebackCodes     = new ArrayList<ChargebackCode>();
    private List<ChargebackReason>  chargebackReasons   = new ArrayList<ChargebackReason>();
    private List<OrderChargeback>   orderChargebacks    = new ArrayList<OrderChargeback>();
    
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

                setInstance(new OrderChargeback());
                getInstance().setProcessType(ChargebackProcessType.CHARGEBACK);
                getInstance().setCurrency(Currency.TRY);
                getInstance().setOrderInquiry(orderInquiry);
                orderChargebacks.clear();
            }
        }catch(Exception e){
            e.printStackTrace();
            Helper.addMessage("HATA..:" + e.getMessage());
        }
        
    }
    
    
}
