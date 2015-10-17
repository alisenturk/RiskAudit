package com.riskaudit.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asenturk
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "UserAuth.findAll",query = "select d from UserAuth d where d.status<>'DELETED'"),
    @NamedQuery(name = "UserAuth.findMemberUserAuth",query = "select d from UserAuth d where d.authCode='ROLE_MEMBER'"),
    @NamedQuery(name = "UserAuth.findMemberAdminUserAuth",query = "select d from UserAuth d where d.authCode='ROLE_MEMBERADMIN'"),    
    @NamedQuery(name = "UserAuth.findAdminUserAuth",query = "select d from UserAuth d where d.authCode='ROLE_ADMIN'")    
})
@XmlRootElement
public class UserAuth extends BaseEntity{
        
    private String  authCode;
    private String  authName;
    private String  authNameEn;

    @NotNull
    @Column(length = 20)
    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @NotNull
    @Column(length = 120)
    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    @NotNull
    @Column(length = 120)
    public String getAuthNameEn() {
        return authNameEn;
    }

    public void setAuthNameEn(String authNameEn) {
        this.authNameEn = authNameEn;
    }
   
    
}
