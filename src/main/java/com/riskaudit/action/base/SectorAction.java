package com.riskaudit.action.base;

import com.riskaudit.entity.base.Sector;
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
public class SectorAction extends BaseAction<Sector>{
    
    @Override
    public List<Sector> getList() {
        if(super.getList()==null || super.getList().isEmpty()){
           super.setList(new ArrayList<Sector>());
           super.getList().addAll(getCrud().getNamedList("Sector.findAll"));
        }
        return super.getList(); 
    }
}
