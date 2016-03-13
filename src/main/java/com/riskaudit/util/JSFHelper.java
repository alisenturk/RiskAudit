package com.riskaudit.util;


import com.riskaudit.entity.base.DailyExchange;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.base.MerchantFile;
import com.riskaudit.entity.base.User;
import com.riskaudit.entity.order.OrderChargebackFile;
import com.riskaudit.enums.Currency;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author asenturk
 */
@Named(value = "helper")
@Stateless
public class JSFHelper implements Serializable{

    @Inject
    private EntityManager entityManager;
    
    public String getMessage(String key){
        return Helper.getMessage(key);
    }
    public String getCurrentUserNameSurnameFromSession(){
        User user = Helper.getCurrentUserFromSession();
        return (user.getFirstname()+ " " + user.getLastname());
    }
    public Object getSessionValue(String key){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        return session.getAttribute(key);
    }
    public void setSessionValue(String key,Object val){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute(key,val);
    }
    public void removeSessionValue(String key){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.removeAttribute(key);
    }
    public User getCurrentUserFromSession(){
        User user = Helper.getCurrentUserFromSession();
        return user;
    }
    public String date2String(Date date){
        if(date!=null){
            return Helper.date2String(date);
        }
        
        return "";
    }
    public String timestamp2String(Date date){
        if(date!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return dateFormat.format(date);
        }
        
        return "";
    }
    public String getTimeAsString(Date date){
        if(date!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            return dateFormat.format(date);
        }
        
        return "";
    }
    public Merchant getCurrentUserMerchant(){
        Merchant merchant = Helper.getCurrentUserMerchant();
        return merchant;
    }
    
    public boolean isAdmin(){
        return  Helper.getCurrentUserIsAdmin();
    }
    
    public StreamedContent fileDownload(MerchantFile merchantFile){
         return fileDownload(merchantFile.getFilePath(),merchantFile.getFileMimeType(),merchantFile.getFileName());
    }
    public StreamedContent fileDownload(OrderChargebackFile file){
         return fileDownload(file.getFilePath(),file.getFileMimeType(),file.getFileName());
    }
    
    public StreamedContent fileDownload(String filePath,String fileMimeType,String fileName){
        StreamedContent file = null;
        try{
            File source = new File(filePath);
            InputStream fis = new FileInputStream(source);
            
            
            file = new DefaultStreamedContent(fis,fileMimeType,fileName);
            
            fis = null;
            source = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        return file;
    }
     public StreamedContent fileDownload(File source,String fileMimeType,String fileName){
        StreamedContent file = null;
        try{
            InputStream fis = new FileInputStream(source);
            file = new DefaultStreamedContent(fis,fileMimeType,fileName);
            
            fis = null;
            source = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        return file;
    }
    
    public double getDailyExchangeValue(Currency currency,Date date){
        double val = 0d;
        try{
            DailyExchange de = (DailyExchange)entityManager.createNamedQuery("DailyExchange.findDateExchange").setParameter("currdate",date).setParameter("currtype",currency).getSingleResult();
            if(de!=null){
                val = de.getBuyingPrice();
            }
        }catch(javax.persistence.NoResultException nre){
            val = 0d;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return val;
    }
    public boolean isActiveMerchantOrderSearch(){
        Merchant merchant = Helper.getCurrentUserMerchant();
        return merchant.getActiveOrderWS();
    }
}
