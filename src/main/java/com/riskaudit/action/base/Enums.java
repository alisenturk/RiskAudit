package com.riskaudit.action.base;

import com.riskaudit.enums.AddressType;
import com.riskaudit.enums.CreditCardProvider;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.EventType;
import com.riskaudit.enums.Gender;
import com.riskaudit.enums.Status;
import com.riskaudit.enums.UserType;
import com.riskaudit.enums.VisitType;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named(value = "enums")
public class Enums implements Serializable{
        
    public SelectItem[] getStastusSelect() {
        SelectItem[] items = new SelectItem[Status.values().length];
        int i = 0;
        for (Status s : Status.values()) {
            items[i++] = new SelectItem(s, s.toString());
        }
        return items;
    }
    
    
    public SelectItem[] getUserTypeSelect() {
       SelectItem[] items = new SelectItem[UserType.values().length];
       int i = 0;
       for (UserType s : UserType.values()) {
           items[i++] = new SelectItem(s,s.getValue());
       }
       return items;
    }
    
    public SelectItem[] getGenderSelect() {
       SelectItem[] items = new SelectItem[Gender.values().length];
       int i = 0;
       for (Gender s : Gender.values()) {
           items[i++] = new SelectItem(s,s.getValue());
       }
       return items;
    }
    
    
     public SelectItem[] getAddressTypeSelect() {
       SelectItem[] items = new SelectItem[AddressType.values().length];
       int i = 0;
       for (AddressType s : AddressType.values()) {
           items[i++] = new SelectItem(s,s.getValue());
       }
       return items;
    }
    
    public SelectItem[] getCurrencySelect() {
       SelectItem[] items = new SelectItem[Currency.values().length];
       int i = 0;
       for (Currency s : Currency.values()) {
           items[i++] = new SelectItem(s,s.getValue());
       }
       return items;
    }
    
    public SelectItem[] getEventTypes(){
       SelectItem[] items = new SelectItem[EventType.values().length];
       int i = 0;
       for (EventType s : EventType.values()) {
           items[i++] = new SelectItem(s,s.getValue());
       }
       return items;
    }
    
    
    public SelectItem[] getVisitTypeSelect() {
        SelectItem[] items = new SelectItem[VisitType.values().length];
        int i = 0;
        for (VisitType s : VisitType.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
    
    public SelectItem[] getCreditCardProviderSelect() {
        SelectItem[] items = new SelectItem[CreditCardProvider.values().length];
        int i = 0;
        for (CreditCardProvider s : CreditCardProvider.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
    
}
