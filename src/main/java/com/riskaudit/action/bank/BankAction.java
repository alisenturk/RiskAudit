package com.riskaudit.action.bank;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.bank.Bank;
import com.riskaudit.entity.location.Country;
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
public class BankAction extends BaseAction<Bank>{

    private List<Bank>      bankList    = new ArrayList<Bank>();
    private List<Country>   countries   = new ArrayList<Country>();
    
    @Override
    public List<Bank> getList() {
        bankList.clear();
        bankList.addAll(getCrud().getNamedList("Bank.findAll"));
        return bankList;        
    }

    public List<Country> getCountries() {
        if(countries.isEmpty()){
            countries.addAll(getCrud().getNamedList("Country.findAll"));
        }
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
    
    
   
    
    
}
