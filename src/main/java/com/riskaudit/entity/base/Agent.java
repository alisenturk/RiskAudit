package com.riskaudit.entity.base;

import com.riskaudit.enums.MarketPlace;
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
    @NamedQuery(name = "Agent.findAll",query = "select d from Agent d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "Agent.findAllMerchantAgentsByMarket",query = "select d from Agent d where d.merchant.id=:mrchntid and d.agencyPlace=:aplace and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "Agent.findAllMerchantAgents",query = "select d from Agent d where d.merchant.id=:mrchntid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class Agent extends BaseEntity{
    
    private Merchant    merchant;
    private String      agentName;
    private String      phoneNumber;
    private String      email;
    private String      responsible;
    private MarketPlace agencyPlace;
    
    @ManyToOne
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }  
    
    @NotNull
    @Column(length = 200,nullable = false)
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Column(length = 20)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(length = 200)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(length = 80)
    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    @Enumerated(EnumType.STRING)
    public MarketPlace getAgencyPlace() {
        return agencyPlace;
    }

    public void setAgencyPlace(MarketPlace agencyPlace) {
        this.agencyPlace = agencyPlace;
    }
    
    
    
    
    
}
