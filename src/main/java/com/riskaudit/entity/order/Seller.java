package com.riskaudit.entity.order;

import com.riskaudit.enums.Status;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author alisenturk
 */
@Embeddable
public class Seller {
    private long    id;
    private String  name;
    private String  email;
    private String  phone;
    private String  responsible;
    private Status  status  = Status.ACTIVE;

    @Column(name = "sellerId")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "sellerName",length = 120)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "sellerEmail",length = 60)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "sellerPhone",length = 15)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "sellerResponsible",length = 120)
    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "sellerStatus",length = 10)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    
}
