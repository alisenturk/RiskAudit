package com.riskaudit.action.base;

import com.riskaudit.entity.base.Department;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.util.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class DepartmentAction extends BaseAction<Department>{
    
    Merchant merchant = Helper.getCurrentUserMerchant();
    private List<Merchant> merchantList = new ArrayList<Merchant>();
    
    @Override
    public List<Department> getList() {
        if(super.getList()==null || super.getList().isEmpty()){
            HashMap<String,Object> params = new HashMap<String,Object>();
            params.put("mrchntid",merchant.getId());
           super.setList(new ArrayList<Department>());
           if(Helper.getCurrentUserIsAdmin()){
               super.getList().addAll(getCrud().getNamedList("Department.findAll"));
           }else{
                super.getList().addAll(getCrud().getNamedList("Department.findAllMerchantDepartments",params));
           }
        }
        return super.getList(); 
    }

    @Override
    public void newRecord() throws InstantiationException, IllegalAccessException {
        super.newRecord();
        getInstance().setMerchant(merchant);
    }

    @Override
    public void save() {
        
        if(merchant!=null && merchant.getId()!=null && merchant.getId()>0){
            if(getInstance()!=null && getInstance().getMerchant()==null){
                getInstance().setMerchant(merchant);
            }            
        }    
        super.save(); 
    }

    public List<Merchant> getMerchantList() {
        if(merchantList.isEmpty() && Helper.getCurrentUserIsAdmin()){
            merchantList.addAll(getCrud().getNamedList("Merchant.findAll"));
        }else if(merchantList.isEmpty() && !Helper.getCurrentUserIsAdmin()){
            merchantList.add(merchant);
        }
        return merchantList;
    }

    public void setMerchantList(List<Merchant> merchantList) {
        this.merchantList = merchantList;
    }
    
    
    
}
