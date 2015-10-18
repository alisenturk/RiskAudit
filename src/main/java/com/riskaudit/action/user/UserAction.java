package com.riskaudit.action.user;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.base.Department;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.base.User;
import com.riskaudit.entity.base.UserAuth;
import com.riskaudit.enums.UserType;
import com.riskaudit.util.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class UserAction extends BaseAction<User>{
    
    private List<Department>            departments = new ArrayList<Department>();
    private Merchant                    merchant    = Helper.getCurrentUserMerchant();
    private HashMap<String,Object>      params      = new HashMap<String,Object>();
    private List<Merchant>              merchants   = new ArrayList<Merchant>();
    
    @PostConstruct
    private void init(){
        merchant    = Helper.getCurrentUserMerchant();
        params.put("mrchntid",merchant.getId());
        getInstance().setMerchant(merchant);
        if(departments==null || departments.isEmpty()){
            departments.addAll(getCrud().getNamedList("Department.findAllMerchantDepartments",params));
        }
        if(getInstance()!=null && getInstance().getMerchant()!=null){
            loadDepartments(merchant);
        }
        
    }
    
    
    
    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    @Override
    public void newRecord() throws InstantiationException, IllegalAccessException {
        super.newRecord();
        getInstance().setMerchant(merchant);
    }
          
    
    
    @Override
    public void save(){
        
        if(!getInstance().isManaged() && getInstance().getUserType().equals(UserType.ADMIN)){
            UserAuth auth = (UserAuth)getCrud().getNamedList("UserAuth.findAdminUserAuth").get(0);
            getInstance().getUserAuths().add(auth);            
        }else if(!getInstance().isManaged() && getInstance().getUserType().equals(UserType.MEMBER)){
            UserAuth auth = (UserAuth)getCrud().getNamedList("UserAuth.findAdminUserAuth").get(0);
            getInstance().getUserAuths().add(auth);            
        }else if(!getInstance().isManaged() && getInstance().getUserType().equals(UserType.MEMBER_ADMIN)){
            UserAuth auth = (UserAuth)getCrud().getNamedList("UserAuth.findMemberAdminUserAuth").get(0);
            getInstance().getUserAuths().add(auth);            
        }
        
        if(!getInstance().isManaged() && getInstance().getMerchant()==null){
            Merchant merchant = Helper.getCurrentUserMerchant();
            getInstance().setMerchant(merchant);
        }
        
        super.save(); 
    }
    
    
     public void loadingDepartmentValueChange(ValueChangeEvent event){
        try{
            
            if(event!=null &&  event.getNewValue()!=null && !event.getNewValue().equals(getInstance().getMerchant())){            
                loadDepartments(((Merchant)event.getNewValue()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    @Override
    public List<User> getList() {
        if(super.getList()==null || super.getList().isEmpty()){
           super.setList(new ArrayList<User>());
           if(Helper.getCurrentUserIsAdmin()){
               super.getList().addAll(getCrud().getNamedList("User.findAll"));
           }else{
                super.getList().addAll(getCrud().getNamedList("User.findMerchantAllUsers",params));
           }
        }
        return super.getList(); 
    }

    public List<Merchant> getMerchants() {
        if(merchants.isEmpty()){
            if(Helper.getCurrentUserIsAdmin()){
                merchants.addAll(getCrud().getNamedList("Merchant.findAll"));
            }else{
                merchants.add(merchant);
            }
        }
        return merchants;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }

    private void loadDepartments(Merchant merchant){
        HashMap<String,Object> paramsDept = new HashMap<String,Object>();
        paramsDept.put("mrchntid",merchant.getId());
        setDepartments(new ArrayList<Department>());
        getDepartments().addAll(getCrud().getNamedList("Department.findAllMerchantDepartments",paramsDept));
    }
    
    public void selectUser(){
        loadDepartments(getInstance().getMerchant());
    }
}
