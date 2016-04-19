package com.riskaudit.entity.order;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.enums.OrderFileType;
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
    @NamedQuery(name = "OrderChargebackFile.findOrderChargebackFiles",query = "select d from OrderChargebackFile d where d.orderChargeback.id=:chrgbckid and d.status<>'DELETED'",hints={@QueryHint(name="javax.persistence.query.timeout", value="1800000")})
})
@XmlRootElement
public class OrderChargebackFile extends BaseEntity{
    
    private OrderChargeback     orderChargeback;    
    private OrderFileType       orderFileType;
    private String              comment;
    private String              fileName;
    private String              filePath;
    private String              fileType;
    private String              fileMimeType;
    
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

    @NotNull
    @Column(length = 200)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @NotNull
    @Column(length = 300)
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @NotNull
    @Column(length = 120)
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @NotNull
    @Column(length = 120)
    public String getFileMimeType() {
        return fileMimeType;
    }

    public void setFileMimeType(String fileMimeType) {
        this.fileMimeType = fileMimeType;
    }

    @Enumerated(EnumType.STRING)
    public OrderFileType getOrderFileType() {
        return orderFileType;
    }

    public void setOrderFileType(OrderFileType orderFileType) {
        this.orderFileType = orderFileType;
    }

    
    
    
    
    
}
