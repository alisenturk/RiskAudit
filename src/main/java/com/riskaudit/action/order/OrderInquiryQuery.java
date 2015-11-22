package com.riskaudit.action.order;

import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.base.User;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author asenturk
 */
public class OrderInquiryQuery {
    private String      orderNo;
    private Date        orderBeginDate;
    private Date        orderEndDate;
    private String      memberName;
    private String      memberSurname;
    private String      memberUsername;
    private User        fraudController;
    private Merchant    merchant;

    private HashMap<String,Object> params = new HashMap<String,Object>();
    
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderBeginDate() {
        return orderBeginDate;
    }

    public void setOrderBeginDate(Date orderBeginDate) {
        this.orderBeginDate = orderBeginDate;
    }

    public Date getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(Date orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberSurname() {
        return memberSurname;
    }

    public void setMemberSurname(String memberSurname) {
        this.memberSurname = memberSurname;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public User getFraudController() {
        return fraudController;
    }

    public void setFraudController(User fraudController) {
        this.fraudController = fraudController;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    
     public String getInqueryQuery(){
        StringBuffer sql = new StringBuffer();
        try{
            boolean where = false;
            sql.append("SELECT o FROM OrderInquiry o ");
            if(merchant!=null && merchant.getId()!=null){
                if(where){
                    sql.append(" AND ");
                }else{
                    sql.append(" WHERE ");
                    where = true;
                }
                sql.append(" o.merchant.id =:mrchntid ");
            }else{
                if(where){
                    sql.append(" AND ");
                }else{
                    sql.append(" WHERE ");
                    where = true;
                }
                sql.append(" o.merchant.id = -1 ");
            }
            if(orderNo!=null && orderNo.length()>1){
                if(where){
                    sql.append(" AND ");
                }else{
                    sql.append(" WHERE ");
                    where = true;
                }
                sql.append(" o.orderInfo.orderNo =:orderno ");
            }
            if(orderBeginDate!=null){
                if(where){
                    sql.append(" AND ");
                }else{
                    sql.append(" WHERE ");
                     where = true;
                }
                sql.append(" o.orderInfo.orderDate >=:beginDate ");
            }
            if(orderEndDate!=null){
                if(where){
                    sql.append(" AND ");
                }else{
                    sql.append(" WHERE ");
                     where = true;
                }
                sql.append(" o.orderInfo.orderDate <=:endDate ");
            }
            if(memberName!=null && memberName.length()>1){
                if(where){
                    sql.append(" AND ");
                }else{
                    sql.append(" WHERE ");
                     where = true;
                }
                sql.append(" o.orderInfo.memberName like :mname ");
            }
            if(memberSurname!=null && memberSurname.length()>1){
                if(where){
                    sql.append(" AND ");
                }else{
                    sql.append(" WHERE ");
                     where = true;
                }
                sql.append(" o.orderInfo.memberSurname like :msurname ");
            }
            if(memberUsername!=null && memberUsername.length()>1){
                if(where){
                    sql.append(" AND ");
                }else{
                    sql.append(" WHERE ");
                     where = true;
                }
                sql.append(" o.orderInfo.memberUsername like :musername ");
            }
            if(fraudController!=null && fraudController.getId()!=null && fraudController.getId()>0){
                if(where){
                    sql.append(" AND ");
                }else{
                    sql.append(" WHERE ");
                     where = true;
                }
                sql.append(" o.paymentInfo.fraudController.id=:fraudcntrl ");                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return sql.toString();
     }

    public HashMap<String, Object> getParams() {
        params = new HashMap<String,Object>();
        if(merchant!=null && merchant.getId()!=null){
           params.put("mrchntid",merchant.getId());
        }
        
        if(orderNo!=null && orderNo.length()>1){
           params.put("orderno",orderNo);
         }
         if(orderBeginDate!=null){
           params.put("beginDate",orderEndDate);
        }
        if(orderEndDate!=null){
           params.put("endDate",orderEndDate);
        }
        if(memberName!=null && memberName.length()>1){
           params.put("mname",memberName);
        }
        if(memberSurname!=null && memberSurname.length()>1){
           params.put("msurname",memberSurname);
        }
        if(memberUsername!=null && memberUsername.length()>1){
           params.put("musername",memberUsername);
        }
        if(fraudController!=null && fraudController.getId()!=null && fraudController.getId()>0){
           params.put("fraudcntrl",fraudController.getId());
        }
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
    
     
     
}
