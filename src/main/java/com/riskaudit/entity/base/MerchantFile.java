package com.riskaudit.entity.base;

import com.riskaudit.enums.MerchantFileType;
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
 * @author asenturk
 */
@Cacheable(true)
@Entity
@NamedQueries({
    @NamedQuery(name = "MerchantFile.findAll",query = "select d from MerchantFile d where d.merchant.id=:mrchntid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "MerchantFile.findMerchantCustomerOfferFile",query = "select d from MerchantFile d where d.merchant.id=:mrchntid and d.merchantFileType='CUSTOMEROFFERTEMPLATE' and d.status='ACTIVE'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")}),
    @NamedQuery(name = "MerchantFile.findMerchantFile",query = "select d from MerchantFile d where d.merchant.id=:mrchntid and d.merchantFileType=:mfiletype and d.status='ACTIVE'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})        
})
@XmlRootElement
public class MerchantFile extends BaseEntity{
    
    private Merchant            merchant;
    private MerchantFileType    merchantFileType;
    private String              fileName;
    private String              filePath;
    private String              fileType;
    private String              fileMimeType;

    @ManyToOne
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Enumerated(EnumType.STRING)
    public MerchantFileType getMerchantFileType() {
        return merchantFileType;
    }

    public void setMerchantFileType(MerchantFileType merchantFileType) {
        this.merchantFileType = merchantFileType;
    }

    @Column(length = 120)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @Column(length = 200)
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(length = 20)
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Column(length = 120)
    public String getFileMimeType() {
        return fileMimeType;
    }

    public void setFileMimeType(String fileMimeType) {
        this.fileMimeType = fileMimeType;
    }
    
    
    
    

}
