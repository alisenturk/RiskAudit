package com.riskaudit.entity.base;

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
 * @author asenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "Merchant.findAll",query = "select d from Merchant d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "Merchant.findMerchant",query = "select d from Merchant d where d.id =:mrchntid and  d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})    
})
@XmlRootElement
public class Merchant extends BaseEntity{
    
    private String      merchantName;    
    private Sector      sector;
    private Date        licenseExpireDate;
    private String      licenseHash;

    
    @NotNull
    @Column(length = 200)
    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    @ManyToOne
    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLicenseExpireDate() {
        return licenseExpireDate;
    }

    public void setLicenseExpireDate(Date licenseExpireDate) {
        this.licenseExpireDate = licenseExpireDate;
    }

    @Column(length = 500)
    public String getLicenseHash() {
        return licenseHash;
    }

    public void setLicenseHash(String licenseHash) {
        this.licenseHash = licenseHash;
    }
    
    
    
    
}
