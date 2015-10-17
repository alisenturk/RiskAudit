package com.riskaudit.entity.order;

import com.riskaudit.entity.base.BaseEntity;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "ProductSubCategory.findAll",query = "select d from ProductSubCategory d where d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "ProductSubCategory.findAllProductSubCategories",query = "select d from ProductSubCategory d where d.productCategory.id=:prdctcatid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class ProductSubCategory  extends BaseEntity{
    
    private ProductCategory productCategory;
    private String          subCategoryCode;
    private String          subCategoryDescription;

    public ProductSubCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }
    public ProductSubCategory() {
        super();
    }
    
    @ManyToOne
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public String getSubCategoryDescription() {
        return subCategoryDescription;
    }

    public void setSubCategoryDescription(String subCategoryDescription) {
        this.subCategoryDescription = subCategoryDescription;
    }
    
    
}
