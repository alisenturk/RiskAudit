package com.riskaudit.entity.order.chargeback;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.enums.ChargebackProcessType;
import java.io.Serializable;
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
 * @author alisenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "ProcessProgress.findAll",query = "select d from ProcessProgress d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "ProcessProgress.findProcessProgressByMerchant",query = "select d from ProcessProgress d where d.merchant.id=:mrchntid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "ProcessProgress.findByProcessTypeAndMerchant",query = "select d from ProcessProgress d where d.merchant.id=:mrchntid and d.processType=:prsstype and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class ProcessProgress  extends BaseEntity{
    
    private Merchant                merchant;
    private ChargebackProcessType   processType;    
    private String                  title;
    private String                  description;
    private Boolean                 chargebackClosed = false;
    private Boolean                 sentLaw = false;
    @ManyToOne
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Enumerated(EnumType.STRING)
    public ChargebackProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ChargebackProcessType processType) {
        this.processType = processType;
    }

    @NotNull
    @Column(length = 60,nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 256)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")   
    public Boolean getChargebackClosed() {
        return chargebackClosed;
    }

    public void setChargebackClosed(Boolean chargebackClosed) {
        this.chargebackClosed = chargebackClosed;
    }

    @Column(nullable = false,columnDefinition = "TINYINT(1)")
    public Boolean getSentLaw() {
        return sentLaw;
    }

    public void setSentLaw(Boolean sentLaw) {
        this.sentLaw = sentLaw;
    }
    
    
    
    
}
