package com.riskaudit.action.index;

import com.riskaudit.action.base.CrudService;
import com.riskaudit.action.events.ScheduleEventQuery;
import com.riskaudit.entity.base.User;
import com.riskaudit.entity.events.ScheduleEvent;
import com.riskaudit.entity.order.OrderChargeback;
import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import com.riskaudit.util.JSFHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class DashboardView implements Serializable{
    
    @Inject
    CrudService crud;
    
    @Inject
    JSFHelper jsfHelper;
    
    
    private List<OrderChargeback>                           listChargebacks = new ArrayList<OrderChargeback>();    
    private List<com.riskaudit.entity.events.ScheduleEvent> listEvents      = new ArrayList<com.riskaudit.entity.events.ScheduleEvent>();
    private int recordCount = 0;
    
    @PostConstruct
    public void init() {
        loadData();
    }
    public void loadData() {
        Date today      = new Date();
        Date startDate  = Helper.string2Date(Helper.date2String(today) + " 00:00:00","dd/MM/yyyy HH:mm:SS");
        Date endDate    = Helper.dateAddMinute(today,0);
                
        User user = jsfHelper.getCurrentUserFromSession();
        if(user==null){
            System.out.println("user is null");
        }
        if(crud==null){
            System.out.println("crud is null");
        }
        if(user!=null && crud!=null){
            try{
                HashMap<String, Object> chargebackParam = new HashMap<String, Object>();
                chargebackParam.put("startDate",startDate);
                chargebackParam.put("endDate",Helper.string2Date(Helper.date2String(today) + " 23:59:59","dd/MM/yyyy HH:mm:SS"));
                chargebackParam.put("mrchntid", Helper.getCurrentUserMerchant().getId());
                
                listChargebacks.clear();
                listChargebacks.addAll(crud.getNamedList("OrderChargeback.reminderOrderChargebacksRangeDate",chargebackParam));
               
                
                HashMap<String, Object> eventParam = new HashMap<String, Object>();
                eventParam.put("today",startDate );
                eventParam.put("endDate",endDate );
                eventParam.put("currentUserId", user.getId());
                eventParam.put("departmentId", user.getDepartment().getId());
                eventParam.put("evntStatus",Status.ACTIVE);
                System.out.println("params..:" + eventParam.toString());
                listEvents = crud.getList(ScheduleEventQuery.getScheduleEventQuery(endDate), eventParam);
                System.out.println("listEvent..:" + listEvents.size());
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }

    public List<OrderChargeback> getListChargebacks() {
        return listChargebacks;
    }

    public void setListChargebacks(List<OrderChargeback> listChargebacks) {
        this.listChargebacks = listChargebacks;
    }

    public List<ScheduleEvent> getListEvents() {
        return listEvents;
    }

    public void setListEvents(List<ScheduleEvent> listEvents) {
        this.listEvents = listEvents;
    }

    public int getRecordCount() {
        recordCount = getListEvents().size() + getListChargebacks().size();
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
    
    
}
