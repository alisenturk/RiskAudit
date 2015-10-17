package com.riskaudit.action.bank;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.bank.Bank;
import com.riskaudit.entity.bank.CreditCardBin;
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
public class CreditCardBinAction extends BaseAction<CreditCardBin>{

    private List<Bank> bankList = new ArrayList<Bank>();
    private List<CreditCardBin> ccbList = new ArrayList<CreditCardBin>();
    
    @Override
    public List<CreditCardBin> getList() {
        ccbList.clear();
        ccbList.addAll(getCrud().getNamedList("CreditCardBin.findAll"));
        return ccbList;
    }

    public List<Bank> getBankList() {
        if(bankList.isEmpty()){
            bankList.addAll(getCrud().getNamedList("Bank.findAll"));
        }
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }
    
    
    
}
