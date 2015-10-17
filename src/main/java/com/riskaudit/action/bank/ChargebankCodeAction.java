package com.riskaudit.action.bank;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.bank.ChargebackCode;
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
public class ChargebankCodeAction extends BaseAction<ChargebackCode>{
    private List<ChargebackCode> codeList = new ArrayList<ChargebackCode>();

    @Override
    public List<ChargebackCode> getList() {
        codeList.clear();
        codeList.addAll(getCrud().getNamedList("ChargebackCode.findAll"));
        return codeList;
    }
    
    
}
