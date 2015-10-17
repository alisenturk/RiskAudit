package com.riskaudit.action.base;

import com.riskaudit.entity.base.UserAuth;
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
public class UserAuthAction extends BaseAction<UserAuth> {

   
    @Override
    public List<UserAuth> getList() {
        if(super.getList()==null || super.getList().isEmpty()){
           super.setList(new ArrayList<UserAuth>());
           super.getList().addAll(getCrud().getNamedList("UserAuth.findAll"));
        }
        return super.getList(); 
    }

    
    
    
    
}
