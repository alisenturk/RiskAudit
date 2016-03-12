package com.riskaudit.action.base;

import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.base.MerchantFile;
import com.riskaudit.entity.base.Sector;
import com.riskaudit.enums.MerchantFileType;
import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import static com.riskaudit.util.Helper.getCurrentUserMerchant;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class MerchantAction extends BaseAction<Merchant>{
    
    
    private List<MerchantFile>  merchantFiles   = new ArrayList<MerchantFile>();
    private List<Sector>        sectors         = new ArrayList<Sector>();
    private MerchantFileType    merchantFileType = MerchantFileType.CHARGEBACK_RESPONSE_TEMPLATE;
    @Override
    public List<Merchant> getList() {
        if(super.getList()==null || super.getList().isEmpty()){
           super.setList(new ArrayList<Merchant>());
           if(Helper.getCurrentUserIsAdmin()){
                super.getList().addAll(getCrud().getNamedList("Merchant.findAll"));               
           }else{
               super.getList().addAll(getCrud().getNamedList("Merchant.findMerchant",Helper.getParamsHashByMerchant()));               
           }
        }
        return super.getList(); 
    }

    public List<MerchantFile> getMerchantFiles() {
        if(merchantFiles.isEmpty() && getInstance().isManaged()){
            HashMap<String, Object> params = new HashMap<String, Object>();
            Merchant merchant = getCurrentUserMerchant();
            params.put("mrchntid", getInstance().getId());
            merchantFiles.addAll(getCrud().getNamedList("MerchantFile.findAll",params));
        }
        return merchantFiles;
    }

    public void setMerchantFiles(List<MerchantFile> merchantFiles) {
        this.merchantFiles = merchantFiles;
    }
    
    
    public void handleFileUpload(FileUploadEvent event) {
        try{
            String fileName = event.getFile().getFileName();
            UploadedFile source = event.getFile();
            String mainFolder = "/opt/merchant/";
            String folderPath = mainFolder+getInstance().getId();
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
                System.out.println("FilenameUtils.getExtension(filePath).toUpperCase()..:" + FilenameUtils.getExtension(filePath).toUpperCase());
                System.out.println("source.getContentType()...:" + source.getContentType());
                
                MerchantFile mfile = new MerchantFile();
                mfile.setMerchantFileType(merchantFileType);
                mfile.setMerchant(getInstance());
                mfile.setFileName(fileName);
                mfile.setFilePath(filePath);
                mfile.setFileType(FilenameUtils.getExtension(filePath).toUpperCase());
                mfile.setFileMimeType(source.getContentType());
                mfile.setStatus(Status.ACTIVE);
                getCrud().createObject(mfile);
               
                merchantFiles = new ArrayList<MerchantFile>();
                FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            
        }catch(Exception e){
            Helper.addMessage(e.getMessage(), FacesMessage.SEVERITY_FATAL);
        }
        
    }

    public void deleteInquiryFile(MerchantFile file) {
        file.setStatus(Status.DELETED);
        getCrud().deleteObject(file);
        Helper.addMessage(Helper.getMessage("Global.Record.Deleted"));
        merchantFiles = new ArrayList<MerchantFile>();
    }

    public List<Sector> getSectors() {
        if(sectors.isEmpty()){
            sectors.addAll(getCrud().getNamedList("Sector.findAll"));
        }
        return sectors;
    }

    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    @Override
    public void save() {
        if(getInstance().getLicenseExpireDate()!=null && getInstance().getMerchantName()!=null){
            getInstance().setLicenseHash(Helper.generateMD5(Helper.date2String(getInstance().getLicenseExpireDate())+getInstance().getMerchantName()));
        }
        super.save(); 
    }

    public MerchantFileType getMerchantFileType() {
        return merchantFileType;
    }

    public void setMerchantFileType(MerchantFileType merchantFileType) {
        this.merchantFileType = merchantFileType;
    }

    
    
    
}
