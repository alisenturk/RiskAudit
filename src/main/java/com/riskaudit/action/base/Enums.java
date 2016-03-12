package com.riskaudit.action.base;

import com.riskaudit.enums.AddressType;
import com.riskaudit.enums.ChargebackProcessType;
import com.riskaudit.enums.CollectionBox;
import com.riskaudit.enums.CreditCardCategory;
import com.riskaudit.enums.CreditCardProvider;
import com.riskaudit.enums.CreditCardType;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.EventType;
import com.riskaudit.enums.Gender;
import com.riskaudit.enums.MarketPlace;
import com.riskaudit.enums.MerchantFileType;
import com.riskaudit.enums.PaymentMethod;
import com.riskaudit.enums.PaymentSecureType;
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
    
    public SelectItem[] getMarketplaceSelect() {
        SelectItem[] items = new SelectItem[MarketPlace.values().length];
        int i = 0;
        for (MarketPlace s : MarketPlace.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
    
    public SelectItem[] getPaymentTypeSelect() {
        SelectItem[] items = new SelectItem[PaymentMethod.values().length];
        int i = 0;
        for (PaymentMethod s : PaymentMethod.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
    public SelectItem[] getPaymentSecureTypeSelect() {
        SelectItem[] items = new SelectItem[PaymentSecureType.values().length];
        int i = 0;
        for (PaymentSecureType s : PaymentSecureType.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
    
    public SelectItem[] getChargebackProcessTypeSelect() {
        SelectItem[] items = new SelectItem[ChargebackProcessType.values().length];
        int i = 0;
        for (ChargebackProcessType s : ChargebackProcessType.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
    
    public SelectItem[] getCreditCardTypeSelect() {
        SelectItem[] items = new SelectItem[CreditCardType.values().length];
        int i = 0;
        for (CreditCardType s : CreditCardType.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
    
    public SelectItem[] getCreditCardCategorySelect() {
        SelectItem[] items = new SelectItem[CreditCardCategory.values().length];
        int i = 0;
        for (CreditCardCategory s : CreditCardCategory.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
    
    public SelectItem[] getCollectionBoxSelect() {
        SelectItem[] items = new SelectItem[CollectionBox.values().length];
        int i = 0;
        for (CollectionBox s : CollectionBox.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
    
    public SelectItem[] getMerchantFileTypesSelect() {
        SelectItem[] items = new SelectItem[MerchantFileType.values().length];
        int i = 0;
        for (MerchantFileType s : MerchantFileType.values()) {
            items[i++] = new SelectItem(s, s.getLabel());
        }
        return items;
    }
}
