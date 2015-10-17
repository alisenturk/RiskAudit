package com.riskaudit.action.bank;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.bank.ChargebackReason;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class ChargebackReasonAction extends BaseAction<ChargebackReason>{
    
    protected List<ChargebackReason> reasons = new ArrayList<ChargebackReason>();

    @Override
    public List<ChargebackReason> getList() {
        reasons.clear();
        reasons.addAll(getCrud().getNamedList("ChargebackReason.findAll"));
        
        return reasons;
    }
    
    
    
}
