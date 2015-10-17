package com.riskaudit.entity.base;

import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import java.util.Date;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 *
 * @author asenturk
 */
public class BaseEntityListener {

    @PrePersist
    public void create(BaseEntity entity) {
        User user = Helper.getCurrentUserFromSession();
        if(user!=null){
            entity.setCreatedBy(user.getId());
        }
        if(entity.getStatus()==null){
            entity.setStatus(Status.ACTIVE);
        }
        entity.setCreationDate(new Date());    
    }

    @PreUpdate
    public void update(BaseEntity entity) {

        User user = Helper.getCurrentUserFromSession();
        if (user != null) {
            entity.setUpdatedBy(user.getId());
        }
        if(entity.getStatus()==null){
            entity.setStatus(Status.ACTIVE);
        } 
        entity.setUpdateDate(new Date());
    }
}
