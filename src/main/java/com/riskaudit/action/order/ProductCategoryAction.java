package com.riskaudit.action.order;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.order.ProductCategory;
import com.riskaudit.entity.order.ProductSubCategory;
import com.riskaudit.util.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class ProductCategoryAction extends BaseAction<ProductCategory>{
    
    Merchant merchant = Helper.getCurrentUserMerchant();
    private List<Merchant>              merchantList    = new ArrayList<Merchant>();
    private List<ProductSubCategory>    subCategories   = new ArrayList<ProductSubCategory>();
    @Override
    public List<ProductCategory> getList() {
        if(super.getList()==null || super.getList().isEmpty()){
            HashMap<String,Object> params = new HashMap<String,Object>();
            params.put("mrchntid",merchant.getId());
           super.setList(new ArrayList<ProductCategory>());
           if(Helper.getCurrentUserIsAdmin()){
               super.getList().addAll(getCrud().getNamedList("ProductCategory.findAll"));
           }else{
                super.getList().addAll(getCrud().getNamedList("ProductCategory.findAllMerchantProductCategories",params));
           }
        }
        return super.getList(); 
    }

    @Override
    public void newRecord() throws InstantiationException, IllegalAccessException {
        super.newRecord();
        getInstance().setMerchant(merchant);
        subCategories = new ArrayList<ProductSubCategory>();        
    }

    @Override
    public void save() {
        
        if(merchant!=null && merchant.getId()!=null && merchant.getId()>0){
            if(getInstance()!=null && getInstance().getMerchant()==null){
                getInstance().setMerchant(merchant);               
            }            
        }    
        if(getInstance().getSubCategories().isEmpty() && !subCategories.isEmpty()){
            getInstance().getSubCategories().addAll(subCategories);
        }
       
        super.save(); 
        
        subCategories = new ArrayList<ProductSubCategory>();
        
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

    public List<ProductSubCategory> getSubCategories() {
        if(subCategories.isEmpty()){
            subCategories.addAll(getInstance().getSubCategories());
        }
        
        return subCategories;
    }

    public void setSubCategories(List<ProductSubCategory> subCategories) {
        this.subCategories = subCategories;
    }
    
    public void newSubProductCategory(){
        subCategories.add(0,new ProductSubCategory(getInstance()));
    }
    public void onRowEdit(RowEditEvent event) {
         try{
            ProductSubCategory subc = ((ProductSubCategory) event.getObject());
            if(subc.isManaged()){
                getCrud().updateObject(subc);
            }else if(getInstance().isManaged()){
                subc.setProductCategory(getInstance());
                getCrud().createObject(subc);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled",""+((ProductSubCategory) event.getObject()).getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void selectProductCategory(){
        subCategories.clear();
    }
    
}
