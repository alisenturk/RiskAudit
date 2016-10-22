package com.riskaudit.entity.order;

import com.riskaudit.entity.base.BaseEntity;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alisenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "CustomerCall.findCustomerCalls",query = "select d from CustomerCall d where d.orderChargeback.id=:chrgbckid and d.status<>'DELETED' order by d.callDate desc",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class CustomerCall  extends BaseEntity{
    
    private OrderChargeback     orderChargeback;
    private Date                callDate = new Date();
    private String              interviewed;
    private String              interviewContent;
    private Boolean             recall;
    private Date                recallDate;

    @ManyToOne
    public OrderChargeback getOrderChargeback() {
        return orderChargeback;
    }

    public void setOrderChargeback(OrderChargeback orderChargeback) {
        this.orderChargeback = orderChargeback;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    public Date getCallDate() {
        return callDate;
    }

    public void setCallDate(Date callDate) {
        this.callDate = callDate;
    }

    @NotNull
    @Column(length = 60,nullable = false)
    public String getInterviewed() {
        return interviewed;
    }

    public void setInterviewed(String interviewed) {
        this.interviewed = interviewed;
    }

    @NotNull
    @Column(length = 2000,nullable = false)
    public String getInterviewContent() {
        return interviewContent;
    }

    public void setInterviewContent(String interviewContent) {
        this.interviewContent = interviewContent;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")   
    public Boolean getRecall() {
        return recall;
    }

    public void setRecall(Boolean recall) {
        this.recall = recall;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    public Date getRecallDate() {
        return recallDate;
    }

    public void setRecallDate(Date recallDate) {
        this.recallDate = recallDate;
    }


    
}
