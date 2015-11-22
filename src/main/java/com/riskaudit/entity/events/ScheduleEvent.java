package com.riskaudit.entity.events;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.entity.base.User;
import com.riskaudit.enums.EventType;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "ScheduleEvent.findAll",query = "select d from ScheduleEvent d where d.status<>'DELETED' order by eventStartdate,eventEnddate"),    
    @NamedQuery(name = "ScheduleEvent.findSchedule",query = "select d from ScheduleEvent d where d.id=:schid")    
})
@XmlRootElement
public class ScheduleEvent extends BaseEntity{
    
    private EventType   eventType;
    private Date        eventStartdate;
    private Date        eventEnddate;
    private String      description;
    private User        responsible;
    private Boolean     viewDepartment  = false;
    private Boolean     enableAllDay    = true;

    public ScheduleEvent(Date eventStartdate, Date eventEnddate, User responsible) {
        this.eventStartdate = eventStartdate;
        this.eventEnddate = eventEnddate;
        this.responsible = responsible;
    }
    
    public ScheduleEvent() {
        super();
    }
    
    @Enumerated(EnumType.STRING)
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getEventStartdate() {
        return eventStartdate;
    }

    public void setEventStartdate(Date eventStartdate) {
        this.eventStartdate = eventStartdate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getEventEnddate() {
        return eventEnddate;
    }

    public void setEventEnddate(Date eventEnddate) {
        this.eventEnddate = eventEnddate;
    }

    @NotNull
    @Column(length = 256,nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")    
    public Boolean getViewDepartment() {
        return viewDepartment;
    }

    public void setViewDepartment(Boolean viewDepartment) {
        this.viewDepartment = viewDepartment;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")    
    public Boolean getEnableAllDay() {
        return enableAllDay;
    }

    public void setEnableAllDay(Boolean enableAllDay) {
        this.enableAllDay = enableAllDay;
    }
    
    
    
}
