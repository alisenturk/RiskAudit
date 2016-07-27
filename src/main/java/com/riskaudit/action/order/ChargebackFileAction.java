package com.riskaudit.action.order;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.order.OrderChargebackFile;
import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author alisenturk
 */
@Named
public class ChargebackFileAction extends BaseAction<OrderChargebackFile>{

    @Override
    public List<OrderChargebackFile> getList() {
        if(getInstance()!=null && 
            getInstance().getOrderChargeback()!=null && 
            getInstance().getOrderChargeback().getId()>0){
            
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("chrgbckid", getInstance().getOrderChargeback().getId());
            return getCrud().getNamedList("OrderChargebackFile.findOrderChargebackFiles",params);
        }
        return new ArrayList<>();
    }
    
    
    
    
    @PreDestroy
    public void destroy(){
        System.out.println("ChargebackFileAction is destroyed");
    }
    
    public boolean handleFileUpload(FileUploadEvent event) {
        boolean result = false;
        try{
            String fileName = Helper.removeForbiddenChar(event.getFile().getFileName()).toLowerCase(Locale.ENGLISH);
            UploadedFile source = event.getFile();
            String mainFolder = "/opt/riskaudit/merchant/chargebackdoc/";
            
            if(Helper.getAppParameterValue("product.stage").equals("dev")){
                mainFolder = "/Users/alisenturk/Temp"+mainFolder;
            }
            
            String folderPath = mainFolder+ Helper.getCurrentUserMerchant().getId() +"/"+getInstance().getOrderChargeback().getId();
            File folder = new File(folderPath);
            if(!folder.exists()){
                folder.mkdirs();
            }
            String filePath = folderPath +"/"+fileName;
            byte[] bytes=null;
            if (null!=source) {
                bytes = source.getContents();
                
                File newFile = new File(filePath);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(newFile));
                stream.write(bytes);
                stream.close();
                
                getInstance().setFileName(fileName);
                getInstance().setFilePath(filePath);
                getInstance().setFileType(FilenameUtils.getExtension(filePath).toUpperCase());
                getInstance().setFileMimeType(source.getContentType());
                getInstance().setStatus(Status.ACTIVE);                
                getCrud().createObject(getInstance());
               
                //chargebackFiles = new ArrayList<OrderChargebackFile>();
                FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                result = true;
            }
            
        }catch(Exception e){
            e.printStackTrace();
            Helper.addMessage(e.getMessage(), FacesMessage.SEVERITY_FATAL);
        }
        
        return result;
        
    }

    public void deleteChargebackFile(OrderChargebackFile file) {
        file.setStatus(Status.DELETED);
        getCrud().deleteObject(file);
        Helper.addMessage(Helper.getMessage("Global.Record.Deleted"));
        //chargebackFiles = new ArrayList<OrderChargebackFile>();
    }

}
