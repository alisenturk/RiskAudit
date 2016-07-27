package com.riskaudit.action.order.chargeback;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.events.ScheduleEvent;
import com.riskaudit.entity.order.CustomerCall;
import com.riskaudit.entity.order.OrderInquiry;
import com.riskaudit.enums.EventType;
import com.riskaudit.util.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Named;

/**
 *
 * @author alisenturk
 */
@Named
public class CustomerCallAction extends BaseAction<CustomerCall>{

    @Override
    public List<CustomerCall> getList() {
        List<CustomerCall> list = new ArrayList<>();
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("chrgbckid",getInstance().getOrderChargeback().getId());
        list.addAll(getCrud().getNamedList("CustomerCall.findCustomerCalls", params));
        
        return list;
    }

    private void deleteEvent(){
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ScheduleEvent WHERE sourceModule = 'CustomerCall' AND sourceModuleId = " + getInstance().getId());
        getCrud().executeNativeUpdate(sql.toString());
    }
    
    @Override
    public void save() {
        try{
            if(getInstance().isManaged()){
                getCrud().updateObject(getInstance());
                Helper.addMessage(Helper.getMessage("Global.Record.Updated"));
            }else{
                getCrud().createObject(getInstance());
         
                Helper.addMessage(Helper.getMessage("Global.Record.Added"));
            }
            getList().clear();
        }catch(Exception e){
            Helper.addMessage("HATA..:" + e.getMessage());
        }
        
        if(getInstance()!=null && getInstance().isManaged()){
            deleteEvent();
        }
        if(getInstance()!=null && 
           getInstance().isManaged() && 
           getInstance().getRecall().booleanValue() && 
           getInstance().getRecallDate()!=null){
            
            OrderInquiry order = getInstance().getOrderChargeback().getOrderInquiry();
            ScheduleEvent event = new ScheduleEvent();
            event.setEventType(EventType.CUSTOMER_CALL);
            event.setEventStartdate(getInstance().getRecallDate());
            event.setEventEnddate(Helper.dateAddMinute(event.getEventStartdate(), 60));
            event.setDescription(order.getOrderInfo().getOrderNo() + " nolu sipariş için " + order.getOrderInfo().getMemberName() + " " + order.getOrderInfo().getMemberSurname() + " isimli müşterinin aranması gerekmektedir.");
            event.setResponsible(Helper.getCurrentUserFromSession());
            event.setViewDepartment(true);
            event.setTargetModuleName("OrderInquiry");
            event.setTargetModelRecordId(order.getId());
            event.setTargetPath("/app/orderinquiry.xhtml?id="+event.getTargetModelRecordId());
            event.setSourceModule("CustomerCall");
            event.setSourceModuleId(getInstance().getId());
            
            getCrud().createObject(event);
            
        }
    }
    
    
    
    
}
