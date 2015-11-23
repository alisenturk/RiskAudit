package com.riskaudit.action.index;

import com.riskaudit.action.base.CrudService;
import com.riskaudit.action.events.ScheduleEventQuery;
import com.riskaudit.entity.base.User;
import com.riskaudit.entity.order.OrderChargeback;
import com.riskaudit.entity.order.OrderInquiry;
import com.riskaudit.enums.EventType;
import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import com.riskaudit.util.JSFHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class ScheduleView implements Serializable {

    @Inject
    CrudService crud;

    @Inject
    JSFHelper jsfHelper;

    private User user = null;
    private ScheduleModel eventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    
    private List<OrderChargeback>                           listChargebacks = new ArrayList<OrderChargeback>();    
    private List<com.riskaudit.entity.events.ScheduleEvent> listEvents      = new ArrayList<com.riskaudit.entity.events.ScheduleEvent>();

    private com.riskaudit.entity.events.ScheduleEvent scheduleEvent;
    
    private Date startDate = new Date();
    
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public void loadData() {

        if(user==null){
            user = jsfHelper.getCurrentUserFromSession();
        }
        boolean isInquiryStatusNull=true,isInquiryTypesNull=true;
        
        HashMap<String, Object> chargebackParam = new HashMap<String, Object>();
        chargebackParam.put("today",startDate);
        chargebackParam.put("mrchntid", Helper.getCurrentUserMerchant().getId());
        listChargebacks.clear();
        listChargebacks.addAll(crud.getNamedList("OrderChargeback.reminderOrderChargebacks",chargebackParam));
               
        HashMap<String, Object> eventParam = new HashMap<String, Object>();
        eventParam.put("today", startDate);
        eventParam.put("currentUserId", user.getId());
        eventParam.put("departmentId", user.getDepartment().getId());
        eventParam.put("evntStatus",Status.ACTIVE);
        listEvents = crud.getList(ScheduleEventQuery.getScheduleEventQuery(), eventParam);
        
    }
    
    public void refreshSchedule(){
        loadData();
        renderSchedule();
    }

    public void renderSchedule() {
        try {
            //eventModel = new DefaultScheduleModel();
            
            DefaultScheduleEvent event = null;
            int count = 0;
            
            for(OrderChargeback oc:listChargebacks){
                count++;
                event = new DefaultScheduleEvent(oc.getOrderInquiry().getOrderInfo().getOrderNo()+"", getEventStartDate(oc.getAppealReminderDate()), getEventEndDate(oc.getAppealReminderDate()), true);
                event.setId(String.valueOf(oc.getId()));
                event.setData(oc.getOrderInquiry().getId());
                event.setEditable(false);
                event.setStyleClass("inquiry1");
                event.setDescription(getEventInquiryDescription(oc.getOrderInquiry(),1));
                
                eventModel.addEvent(event);
            }
            
            for(com.riskaudit.entity.events.ScheduleEvent evnt : listEvents) {
                count++;                
                event = new DefaultScheduleEvent(evnt.getDescription(),getEventDate(evnt.getEventStartdate()),getEventDate(evnt.getEventEnddate()),evnt.getEnableAllDay());
                event.setId(String.valueOf(evnt.getId()));
                event.setData(evnt);
                event.setEditable(true);                
                if(evnt.getEventType().equals(EventType.HOLIDAY)){
                    event.setStyleClass("eventType1");
                }else if(evnt.getEventType().equals(EventType.VISIT)){
                    event.setStyleClass("eventType2");
                }else if(evnt.getEventType().equals(EventType.MEETING)){
                    event.setStyleClass("eventType3");
                }
                event.setDescription(evnt.getDescription());
                eventModel.addEvent(event);
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        
        user = jsfHelper.getCurrentUserFromSession();
        if (user != null && crud != null) {
            
            eventModel = new LazyScheduleModel(){
                @Override
                public void loadEvents(Date start, Date end) {
                    startDate = start;
                    loadData();
                    renderSchedule();
                }   
            };
        }
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public void addEvent(ActionEvent actionEvent) {
        if(event.getId() == null) {        
           crud.createObject(scheduleEvent);
        }else if(event.getId()!=null && event.isEditable()){            
            crud.updateObject(scheduleEvent);
        }
        loadData();
        renderSchedule();
    }

    private Date getEventStartDate(Date date) {
        Calendar t = Calendar.getInstance();
        t.setTime(date);
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE));
        t.set(Calendar.HOUR, 9);

        return t.getTime();
    }

    private Date getEventEndDate(Date date) {
        Calendar t = Calendar.getInstance();
        t.setTime(date);
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE));
        t.set(Calendar.HOUR, 5);

        return t.getTime();
    }
    
    private Date getEventDate(Date date) {
        Calendar t = Calendar.getInstance();
        t.setTimeInMillis(date.getTime());
        if(date.getHours()>=12){
            t.set(Calendar.AM_PM, Calendar.PM);
        }else{
               t.set(Calendar.AM_PM, Calendar.AM);
        }
        t.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return t.getTime();
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        scheduleEvent = new com.riskaudit.entity.events.ScheduleEvent();
        if(event.getId()!=null && event.isEditable() && event.getData()!=null){
            scheduleEvent = (com.riskaudit.entity.events.ScheduleEvent)event.getData();
        }
    }

    public void onDateSelect(SelectEvent selectEvent) {
        DefaultScheduleEvent dse = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        scheduleEvent = new com.riskaudit.entity.events.ScheduleEvent();
        scheduleEvent.setEventStartdate((Date) selectEvent.getObject());
        scheduleEvent.setEventEnddate((Date) selectEvent.getObject());
        scheduleEvent.setResponsible(user);
        scheduleEvent.setEventType(EventType.MEETING);
        dse.setData(scheduleEvent);
        event = dse;
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public com.riskaudit.entity.events.ScheduleEvent getScheduleEvent() {
        if(scheduleEvent == null){
            scheduleEvent = new com.riskaudit.entity.events.ScheduleEvent();
            scheduleEvent.setEventType(EventType.MEETING);
            scheduleEvent.setResponsible(user);
        }
        return scheduleEvent;
    }

    public void setScheduleEvent(com.riskaudit.entity.events.ScheduleEvent scheduleEvent) {
        this.scheduleEvent = scheduleEvent;
    }

    public List<OrderChargeback> getListChargebacks() {
        return listChargebacks;
    }

    public void setListChargebacks(List<OrderChargeback> listChargebacks) {
        this.listChargebacks = listChargebacks;
    }
    
    private String getEventInquiryDescription(OrderInquiry inq,int evetType){
        StringBuffer desc = new StringBuffer();
        desc.append("<table>");        
        desc.append("    <tr>");
        desc.append("        <th style=\"font-weight:bold;text-align:left;\">Sipariş No</th>");
        desc.append("        <td>"+inq.getOrderInfo().getOrderNo()+"</td>");
        desc.append("    </tr>");        
        desc.append("    <tr>");
        desc.append("        <th style=\"font-weight:bold;text-align:left;\">Sipariş Sahibi</th>");
        desc.append("        <td>"+inq.getOrderInfo().getMemberName()+" " + inq.getOrderInfo().getMemberSurname()+ "</td>");
        desc.append("    </tr>");    
        desc.append("    <tr>");
        desc.append("        <td style=\"font-weight:bold;text-align:left;\" colspan=\"2\"> Sipariş için gelen itirazın yanıtlanması gerekiyor.</td>");
        desc.append("    </tr>");        
        desc.append("</table>");
        
        
        return desc.toString();
    }
    
}
