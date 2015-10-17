package com.riskaudit.entity.base;

import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author asenturk
 */
@MappedSuperclass
@EntityListeners(value = BaseEntityListener.class)
public  class BaseEntity implements Serializable,BaseInterface{
    
    private Long    id;   
    private Status  status;
    private Date    creationDate;
    private Date    updateDate;
    private Long    createdBy;
    private Long    updatedBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    @Transient
    @XmlTransient
    public String getCreatedByNameAndSurname(){
        return Helper.getUserNameAndSurname(createdBy);
    }
    
    
    @Transient
    @XmlTransient
    public String getUpdatedByNameAndSurname(){
        return Helper.getUserNameAndSurname(updatedBy);
    }

    @Override
    @Transient
    @XmlTransient
    public boolean isManaged() {
        if(getId()!=null && getId()>0)return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BaseEntity other = (BaseEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
   
    
}
