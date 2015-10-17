package com.riskaudit.entity.base;

import com.riskaudit.enums.Currency;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "DailyExchange.findAll",query = "select d from DailyExchange d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "DailyExchange.findDateAllExchange",query = "select d from DailyExchange d where d.date=:currdate and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),    
    @NamedQuery(name = "DailyExchange.deleteDateExchange",query = "delete from DailyExchange d where d.date=:currdate "),    
    @NamedQuery(name = "DailyExchange.findDateExchange",query = "select d from DailyExchange d where d.date=:currdate and d.currency=:currtype and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})    
})
@XmlRootElement
public class DailyExchange extends BaseEntity{
    
    private Currency    currency;
    private Date        date;
    private double      buyingPrice;
    private double      sellingPrice;
    private double      effectiveBuyingPrice;
    private double      effectiveSellingPrice;
    private double      crossRateUSD;

    
    @Enumerated(EnumType.STRING)
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getEffectiveBuyingPrice() {
        return effectiveBuyingPrice;
    }

    public void setEffectiveBuyingPrice(double effectiveBuyingPrice) {
        this.effectiveBuyingPrice = effectiveBuyingPrice;
    }

    public double getEffectiveSellingPrice() {
        return effectiveSellingPrice;
    }

    public void setEffectiveSellingPrice(double effectiveSellingPrice) {
        this.effectiveSellingPrice = effectiveSellingPrice;
    }

    public double getCrossRateUSD() {
        return crossRateUSD;
    }

    public void setCrossRateUSD(double crossRateUSD) {
        this.crossRateUSD = crossRateUSD;
    }
    
    
    
    
}
