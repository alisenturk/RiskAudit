package com.riskaudit.action.order.chargeback;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.order.chargeback.MailCategoryCCMail;
import com.riskaudit.util.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author alisenturk
 */
@Named(value = "ccMailAct")
@ViewScoped
public class MailCategoryCCMailAction extends BaseAction<MailCategoryCCMail>{
    
    Merchant merchant = Helper.getCurrentUserMerchant();
    
    private List<Merchant> merchantList = new ArrayList<Merchant>();
    
    @Override
    public List<MailCategoryCCMail> getList() {
        if(super.getList()==null || super.getList().isEmpty()){
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("mrchntid",merchant.getId());
           super.setList(new ArrayList<MailCategoryCCMail>());
           if(Helper.getCurrentUserIsAdmin()){
               super.getList().addAll(getCrud().getNamedList("MailCategoryCCMail.findAll"));
           }else{
                super.getList().addAll(getCrud().getNamedList("MailCategoryCCMail.findMailTemplateByMerchant",params));
           }
        }
        return super.getList(); 
    }

    @Override
    public void newRecord() throws InstantiationException, IllegalAccessException {
        super.newRecord();
        getInstance().setMerchant(merchant);
    }

    @Override
    public void save() {
        
        if(merchant!=null && merchant.getId()!=null && merchant.getId()>0){
            if(getInstance()!=null && getInstance().getMerchant()==null){
                getInstance().setMerchant(merchant);
            }            
        }    
        super.save(); 
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public List<Merchant> getMerchantList() {
        if(merchantList.isEmpty() && Helper.getCurrentUserIsAdmin()){
            merchantList.addAll(getCrud().getNamedList("Merchant.findAll"));
        }else if(merchantList.isEmpty() && !Helper.getCurrentUserIsAdmin()){
            merchantList.add(merchant);
        }
        return merchantList;
    }

    public void setMerchantList(List<Merchant> merchantList) {
        this.merchantList = merchantList;
    }
    
}
