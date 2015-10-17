package com.riskaudit.entity.bank;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.enums.CreditCardProvider;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @NamedQuery(name = "CreditCardBin.findAll",query = "select d from CreditCardBin d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})    
})
@XmlRootElement
public class CreditCardBin extends BaseEntity{
    
    private Bank                bank;
    private String              creditCardBin;
    private CreditCardProvider  provider;
    
    @ManyToOne
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @NotNull
    @Column(length = 6)
    public String getCreditCardBin() {
        return creditCardBin;
    }

    public void setCreditCardBin(String creditCardBin) {
        this.creditCardBin = creditCardBin;
    }

    @NotNull
    @Enumerated
    public CreditCardProvider getProvider() {
        return provider;
    }

    public void setProvider(CreditCardProvider provider) {
        this.provider = provider;
    }
    
    
}
