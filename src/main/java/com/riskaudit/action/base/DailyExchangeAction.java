package com.riskaudit.action.base;

import com.riskaudit.action.schedule.TCMBDailyExchangeReader;
import com.riskaudit.entity.base.DailyExchange;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class DailyExchangeAction extends BaseAction<DailyExchange>{
    
    @Inject
    TCMBDailyExchangeReader tcmbReader;
    
    private Date    exchangeDate = new Date();
    private HashMap<String,Object> params = new HashMap<String,Object>();
    
    @Override
    public List<DailyExchange> getList() {
        if(super.getList()==null || super.getList().isEmpty()){
            
           params = new HashMap<String,Object>();
           params.put("currdate", getExchangeDate());
           super.setList(new ArrayList<DailyExchange>());
           super.getList().addAll(getCrud().getNamedList("DailyExchange.findDateAllExchange",params));
        }
        return super.getList(); 
    }
    
    public void loadDailyExchange(){
        super.setList(new ArrayList<DailyExchange>());
    }
    
    public void loadCurrencyExchangeFromTCMB(){
        tcmbReader.setExchangeDate(exchangeDate);
        try {
            tcmbReader.read();
        } catch (Exception ex) {
            Logger.getLogger(DailyExchangeAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadDailyExchange();
    }
    
    public Date getExchangeDate() {
        if(exchangeDate==null){
            exchangeDate = new Date();
        }
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }
    
    
    
}
