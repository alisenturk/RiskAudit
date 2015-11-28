package com.riskaudit.entity.order;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.entity.base.Merchant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = "ProductCategory.findAll",query = "select d from ProductCategory d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "ProductCategory.findAllMerchantOnlyProductCategories",query = "select d from ProductCategory d where d.merchant.id=:mrchntid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "ProductCategory.findMerchantProductCategoryByCode",query = "select d from ProductCategory d where d.merchant.id=:mrchntid and d.categoryCode=:catCode and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "ProductCategory.findAllMerchantProductCategories",query = "select d from ProductCategory d join fetch d.subCategories where d.merchant.id=:mrchntid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class ProductCategory extends BaseEntity{
  
    private Merchant    merchant;
    private String      categoryCode;
    private String      categoryDesc;
    
    private List<ProductSubCategory> subCategories = new ArrayList<ProductSubCategory>();

    
    @ManyToOne
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @NotNull
    @Column(length =40)
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @NotNull
    @Column(length = 120)
    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    @OneToMany(mappedBy = "productCategory",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    public List<ProductSubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<ProductSubCategory> subCategories) {
        this.subCategories = subCategories;
    }
    
    
    
}
