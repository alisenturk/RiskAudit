package com.riskaudit.action.order;

import com.riskaudit.action.base.CrudService;
import com.riskaudit.util.JSFHelper;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named(value = "orderInquirySearch")
@ViewScoped
public class OrderInquirySearch implements Serializable{
    @Inject
    CrudService crud;
        
    @Inject
    JSFHelper   jsfHelper;
    
}
