package com.riskaudit.action.order.chargeback;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.order.ChargebackMail;
import com.riskaudit.enums.MailSendStatus;
import com.riskaudit.util.Helper;
import com.riskaudit.util.Postwoman;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Named;

/**
 *
 * @author alisenturk
 */

@Named
public class ChargebackMailAction extends BaseAction<ChargebackMail>{
    
    @Override
    public List<ChargebackMail> getList() {
        List<ChargebackMail> list = new ArrayList<>();
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("chrgbckid",getInstance().getOrderChargeback().getId());
        list.addAll(getCrud().getNamedList("ChargebackMail.findChargebackMails", params));
        
        return list;
    }

    @Override
    public void save() {
        boolean sendMail = getInstance().isSaveNSend();
        try{
            if(sendMail){
                getInstance().setSendStatus(MailSendStatus.SENT);
            }else{
                getInstance().setSendStatus(MailSendStatus.WAIT);
            }
            if(getInstance().isManaged()){
                getCrud().updateObject(getInstance());
                Helper.addMessage(Helper.getMessage("Global.Record.Updated"));
            }else{
                getCrud().createObject(getInstance());
                Helper.addMessage(Helper.getMessage("Global.Record.Added"));
            }
            if(sendMail){
                mailSend();
            }
        }catch(Exception e){
            Helper.addMessage("HATA..:" + e.getMessage());
        }
    }
    
    private void mailSend(){
        List<String> toList = new ArrayList<>();
        toList.add(getInstance().getToAddress());
        
        List<String> ccList = new ArrayList<>();
        if(getInstance().getCcAddress()!=null && getInstance().getCcAddress().length()>3){
            ccList.addAll(Arrays.asList(getInstance().getCcAddress().split(";")));
        }
        
        boolean result = Helper.mailSend(getInstance().getMailSubject(),getInstance().getMailContent(),toList,ccList);
        if(!result){
            getInstance().setSendStatus(MailSendStatus.NOTSEND);
            getCrud().updateObject(getInstance());
        }
        System.out.println("***** BITTI *****");
    }
    
}
