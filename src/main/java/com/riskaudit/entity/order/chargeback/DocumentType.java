package com.riskaudit.entity.order.chargeback;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.enums.DocDirection;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alisenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "DocumentType.findAll",query = "select d from DocumentType d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "DocumentType.findDocumentTypeByMerchant",query = "select d from DocumentType d where d.merchant.id=:mrchntid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "DocumentType.findByDocumentTypeAndMerchant",query = "select d from DocumentType d where d.merchant.id=:mrchntid and d.docDirection=:docdirct and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class DocumentType  extends BaseEntity{
    
    private Merchant                merchant;
    private DocDirection            docDirection;
    private String                  title;
    private String                  description;

    @ManyToOne
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Enumerated(EnumType.STRING)
    public DocDirection getDocDirection() {
        return docDirection;
    }

    public void setDocDirection(DocDirection docDirection) {
        this.docDirection = docDirection;
    }
    
    @Column(length = 60)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 260)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
