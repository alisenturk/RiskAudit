package com.riskaudit.action.events;

import com.riskaudit.entity.base.User;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author asenturk
 */
public class ScheduleEventQuery {
    private Date        eventStartdate;
    private Date        eventEndDate;
    private User        responsible;
    private Boolean     viewDepartment;
    
    private HashMap<String,Object> params = new HashMap<String,Object>();
    
    public Date getEventStartdate() {
        return eventStartdate;
    }

    public void setEventStartdate(Date eventStartdate) {
        this.eventStartdate = eventStartdate;
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }

    public Boolean getViewDepartment() {
        return viewDepartment;
    }

    public void setViewDepartment(Boolean viewDepartment) {
        this.viewDepartment = viewDepartment;
    }
    
    public static String getScheduleEventQuery(){
        return getScheduleEventQuery(null);
    }
    public static String getScheduleEventQuery(Date eventEndDate){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT d FROM ScheduleEvent d ");
        sql.append("WHERE ");
        sql.append("    d.eventStartdate>=:today and ");
        if(eventEndDate!=null){
            sql.append("    d.eventStartdate<=:endDate and ");
            sql.append(" d.eventEnddate >=:endDate and ");
        }
        sql.append("    d.status=:evntStatus and ");
        sql.append("    ( ");
        sql.append("        d.responsible.id =:currentUserId or ");
        sql.append("        ( ");
        sql.append("            d.viewDepartment=true and d.responsible.department.id =:departmentId ");
        sql.append("        ) ");
        sql.append("    )");
        sql.append("ORDER BY d.eventStartdate,d.eventEnddate ");
        return sql.toString();
    }
    
    
}
