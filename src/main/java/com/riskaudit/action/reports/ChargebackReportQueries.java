package com.riskaudit.action.reports;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author asenturk
 */
public class ChargebackReportQueries {

    private Long    merchantId;
    private Date    requestDateBegin;
    private Date    requestDateEnd;
    
    private Map<String,Object> params = new HashMap<String, Object>();
    
    
    public void clearData(){
        merchantId          = 0L;
        requestDateBegin    = null;
        requestDateEnd      = null;
        params.clear();
    }
    
    
    public String getChargebackProcessTypeBased(){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT c.processType, count(1) cnt ");
        sql.append("FROM riskauditdb.OrderInquiry o ");
        sql.append("JOIN riskauditdb.OrderChargeback c ON c.orderInquiry_id = o.id ");
        sql.append("WHERE o.merchant_id =:mrchntid AND (o.orderDate BETWEEN :requestBeginDate AND :requestEndDate) ");
        sql.append("GROUP BY c.processType ");
        
        return sql.toString();
    }
    
    public String getChargebackCodeBased(){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT cbc.title, count(1) cnt ");
        sql.append("FROM riskauditdb.OrderInquiry o ");
        sql.append("JOIN riskauditdb.OrderChargeback c ON c.orderInquiry_id = o.id ");
        sql.append("JOIN riskauditdb.chargebackcode cbc on cbc.id = c.chargebackCode_id ");
        sql.append("WHERE o.merchant_id =:mrchntid AND (o.orderDate BETWEEN :requestBeginDate AND :requestEndDate) ");
        sql.append("GROUP BY cbc.title ");
        
        return sql.toString();
    }
    
    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Date getRequestDateBegin() {
        return requestDateBegin;
    }

    public void setRequestDateBegin(Date requestDateBegin) {
        this.requestDateBegin = requestDateBegin;
    }

    public Date getRequestDateEnd() {
        return requestDateEnd;
    }

    public void setRequestDateEnd(Date requestDateEnd) {
        this.requestDateEnd = requestDateEnd;
    }

    public Map<String, Object> getParams() {
        if(params==null ||params.isEmpty()){
            params.put("mrchntid",merchantId);
            params.put("requestBeginDate",requestDateBegin);
            params.put("requestEndDate", requestDateEnd);
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
}
