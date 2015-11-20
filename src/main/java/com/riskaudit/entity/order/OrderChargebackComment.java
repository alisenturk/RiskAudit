package com.riskaudit.entity.order;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.enums.ChargebackProcessType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "OrderChargebackComment.findOrderChargebackComments",query = "select d from OrderChargebackComment d where d.orderChargeback.id=:chrgbckid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class OrderChargebackComment extends BaseEntity{
    
    private OrderChargeback         orderChargeback; 
    private ChargebackProcessType   processType;
    private String                  comment;
    
    @ManyToOne
    public OrderChargeback getOrderChargeback() {
        return orderChargeback;
    }

    public void setOrderChargeback(OrderChargeback orderChargeback) {
        this.orderChargeback = orderChargeback;
    }

    @NotNull
    @Column(length = 3000)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Enumerated(EnumType.STRING)
    public ChargebackProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ChargebackProcessType processType) {
        this.processType = processType;
    }
    
    
    
    
}
