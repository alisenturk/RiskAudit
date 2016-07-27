package com.riskaudit.action.order;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.action.order.chargeback.CustomerCallAction;
import com.riskaudit.entity.bank.Bank;
import com.riskaudit.entity.bank.ChargebackCode;
import com.riskaudit.entity.bank.ChargebackReason;
import com.riskaudit.entity.bank.CreditCardBin;
import com.riskaudit.entity.base.MerchantFile;
import com.riskaudit.entity.order.CustomerCall;
import com.riskaudit.entity.order.OrderChargeback;
import com.riskaudit.entity.order.OrderChargebackComment;
import com.riskaudit.entity.order.OrderChargebackFile;
import com.riskaudit.entity.order.OrderInquiry;
import com.riskaudit.entity.order.OrderProduct;
import com.riskaudit.entity.order.PaymentInfo;
import com.riskaudit.entity.order.chargeback.ChargebackStatus;
import com.riskaudit.entity.order.chargeback.DocumentType;
import com.riskaudit.entity.order.chargeback.LawReason;
import com.riskaudit.entity.order.chargeback.ProcessProgress;
import com.riskaudit.enums.ChargebackProcessType;
import com.riskaudit.enums.CreditCardProvider;
import com.riskaudit.enums.DocDirection;
import com.riskaudit.enums.MerchantFileType;
import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import com.riskaudit.util.JSFHelper;
import com.riskaudit.util.WordDocumentReplace;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class ChargebackAction extends BaseAction<OrderChargeback>{
    @Inject
    JSFHelper helper;
    
    @Inject
    CustomerCallAction  customerCallAct;
    
    @Inject
    ChargebackFileAction    chargebackFileAction;    
    
    private OrderInquiry            orderInquiry;
    private OrderChargebackComment  comment         = new OrderChargebackComment();
    private CustomerCall            customerCall    = new CustomerCall();
    private OrderChargebackFile     chargebackFile  = new OrderChargebackFile();
    
    private File            appealDocumentCover;
    private String          fileName;
    private String          fileType;

    private boolean         lawRender = false;    
    
    private List<Bank>                      banks               = new ArrayList<>();
    private List<Bank>                      cardBanks           = new ArrayList<>();
    private List<ChargebackCode>            chargebackCodes     = new ArrayList<>();
    private List<ChargebackReason>          chargebackReasons   = new ArrayList<>();
    private List<OrderChargeback>           orderChargebacks    = new ArrayList<>();
    private List<OrderChargebackComment>    comments            = new ArrayList<>();
    private List<OrderChargebackFile>       chargebackFiles     = new ArrayList<>();
    private List<ProcessProgress>           processProgresses   = new ArrayList<>();
    private List<LawReason>                 lawReasons          = new ArrayList<>();
    private List<CustomerCall>              customerCalls       = new ArrayList<>();
    private List<DocumentType>              documentTypes       = new ArrayList<>();
    private List<SelectItem>                documentTypeItems   = null;
    private String                          processComment = "";
    
    public OrderInquiry getOrderInquiry() {
        return orderInquiry;
    }

    public void setOrderInquiry(OrderInquiry orderInquiry) {
        this.orderInquiry = orderInquiry;
    }

    public List<Bank> getBanks() {
        if(banks.isEmpty()){
            banks.addAll(getCrud().getNamedList("Bank.findAll"));
        }
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public List<ChargebackCode> getChargebackCodes() {
        if(chargebackCodes.isEmpty()){
            chargebackCodes.addAll(getCrud().getNamedList("ChargebackCode.findAll"));
        }
        return chargebackCodes;
    }

    public void setChargebackCodes(List<ChargebackCode> chargebackCodes) {
        this.chargebackCodes = chargebackCodes;
    }

    public List<ChargebackReason> getChargebackReasons() {
        if(chargebackReasons.isEmpty()){
            chargebackReasons.addAll(getCrud().getNamedList("ChargebackReason.findAll"));
        }
        return chargebackReasons;
    }

    public void setChargebackReasons(List<ChargebackReason> chargebackReasons) {
        this.chargebackReasons = chargebackReasons;
    }

    public List<OrderChargeback> getOrderChargebacks() {
        if(getOrderInquiry()!=null && getOrderInquiry().isManaged() && orderChargebacks.isEmpty()){
            HashMap<String,Object> offerMap = new HashMap<String,Object>();
            offerMap.put("orderinqid",getOrderInquiry().getId());
            orderChargebacks.addAll(getCrud().getNamedList("OrderChargeback.findOrderChargebacks",offerMap));
        }
        return orderChargebacks;
    }

    public void setOrderChargebacks(List<OrderChargeback> orderChargebacks) {
        this.orderChargebacks = orderChargebacks;
    }
    
    private void chargebackStatus2Closed(){
        if(getInstance().getProcessProgress()!=null && 
           getInstance().getProcessProgress().getChargebackClosed()!=null && 
           getInstance().getProcessProgress().getChargebackClosed().booleanValue()){
            
           getInstance().setChargebackStatus(ChargebackStatus.CLOSED);
        }
        
        if(getInstance().getProcessProgress()!=null &&            
           !getInstance().getProcessProgress().getSentLaw().booleanValue()){
            
           getInstance().setLawReason(null);
           getInstance().setCaseStatus(null);
        }
        
    }
    
    @Override
    public void save() {
       
        try{
            if(orderInquiry!=null && orderInquiry.isManaged()){
                /**
                 * ProcessProgress durumuna göre chargeback status'unu değiştirir.
                 */
                chargebackStatus2Closed();
                
                if(getInstance().isManaged()){                    
                    getCrud().updateObject(getInstance());
                    Helper.addMessage(Helper.getMessage("Global.Record.Updated"));
                }else{
                    getInstance().setOrderInquiry(orderInquiry);
                    getCrud().createObject(getInstance());
                    Helper.addMessage(Helper.getMessage("Global.Record.Added"));
                }
                if(processComment!=null && processComment.length()>3){
                    comment = new OrderChargebackComment();
                    comment.setComment(processComment);
                    saveComment();
                    processComment = "";
                }
                
            }
        }catch(Exception e){
            e.printStackTrace();
            Helper.addMessage("HATA..:" + e.getMessage());
        }
        
    }
    
    public void init(){
        if(!getInstance().isManaged() && getOrderInquiry()!=null && getOrderInquiry().isManaged()){
            HashMap<String,Object> params = new HashMap<String, Object>();
            params.put("orderinqid",getOrderInquiry().getId());
            List<OrderChargeback> chargebacks = getCrud().getNamedList("OrderChargeback.findOrderChargebacks", params);
            if(chargebacks!=null && chargebacks.size()>0){
                setInstance(chargebacks.get(0));
                loadProviderBankAndChargebackCodes();
                
            }else{
                PaymentInfo pay = getOrderInquiry().getPaymentInfo();
                getInstance().setCardBank(pay.getCardBank());
                getInstance().setCardHolder(pay.getCardHolder());
                getInstance().setCreditCardNo(pay.getCreditCardNo());
                getInstance().setCurrency(pay.getPayCurrency());
                getInstance().setPosBank(pay.getPosBank());
                getInstance().setTotal(pay.getPayAmount());
                getInstance().setProcessType(ChargebackProcessType.APPEAL);
                loadProviderBankAndChargebackCodes();
            }
            
            loadProcessProgressList(getInstance().getProcessType());
            checkRenderLaw(getInstance().getProcessProgress());
            
            chargebackFile = new OrderChargebackFile();
            chargebackFile.setOrderChargeback(getInstance());
            chargebackFiles.clear();
            chargebackFileAction.setInstance(chargebackFile);
        }
    }

    public List<Bank> getCardBanks() {
        return cardBanks;
    }

    public void setCardBanks(List<Bank> cardBanks) {
        this.cardBanks = cardBanks;
    }
    
    
    public CreditCardProvider getCardProviderByBin(String bin){
        HashMap<String,Object> prms = new HashMap<String,Object>();
        prms.put("bin",bin);
        List<CreditCardBin> list = getCrud().getNamedList("CreditCardBin.findCreditCardBinByBin", prms);
        if(list.size()>0){
          return list.get(0).getProvider();            
        }
        
        return null;
    }
    public void loadChargebackCodes(CreditCardProvider provider){
        HashMap<String,Object> prms = new HashMap<String,Object>();
        prms.put("prvdr",provider);
        List<ChargebackCode> list = getCrud().getNamedList("ChargebackCode.findAllByProvider", prms);
        if(list.size()>0){
            chargebackCodes.clear();
            chargebackCodes.addAll(list);
        }else{
            chargebackCodes.clear();
        }
    }
    public void loadProviderBankAndChargebackCodes(){
            cardBanks = new ArrayList<Bank>();
            if(getInstance()!=null && getInstance().getCreditCardNo()!=null && getInstance().getCreditCardNo().length()>6){
                String bin = getInstance().getCreditCardNo().substring(0,7).replace("-","");
                HashMap<String,Object> prms = new HashMap<String,Object>();
                prms.put("bin",bin);
                cardBanks.addAll(getCrud().getNamedList("CreditCardBin.findBankByBin",prms ));
                if(cardBanks.size()>0){
                    getInstance().setCardBank(cardBanks.get(0));
                }else{
                    cardBanks.addAll(getCrud().getNamedList("Bank.findAll"));
                }
                loadChargebackCodes(getCardProviderByBin(bin));
            }
    }
    public void handleCreditCardKeyEvent(){
        
        if( getInstance()!=null && 
            getInstance().getCreditCardNo()!=null && 
            getInstance().getCreditCardNo().length()>=7 ){
            loadProviderBankAndChargebackCodes();
        }
    }

    public OrderChargebackComment getComment() {
        return comment;
    }

    public void setComment(OrderChargebackComment comment) {
        this.comment = comment;
    }

    public List<OrderChargebackComment> getComments() {
        if(comments.isEmpty()){
            
        }
        return comments;
    }

    public void setComments(List<OrderChargebackComment> comments) {
        this.comments = comments;
    }
    
    public void saveComment(){
        try{
            if(getInstance()!=null && getInstance().getId()>0){
                if(comment!=null && comment.getOrderChargeback()==null){
                    comment.setOrderChargeback(getInstance());
                }
                if(comment!=null && comment.getProcessType()==null){
                    comment.setProcessType(getInstance().getProcessType());
                }
                if(comment!=null && comment.getId()!=null && comment.getId()>0){
                    getCrud().updateObject(comment);
                }else if(comment!=null && (comment.getId()==null || comment.getId()==0)){
                    getCrud().createObject(comment);
                    getInstance().getComments().add(comment);
                }
                comment = new OrderChargebackComment();
                comment.setOrderChargeback(getInstance());
                comment.setProcessType(getInstance().getProcessType());
            }
        }catch(Exception e){
            
        }
    }
    
    public void selectComment(OrderChargebackComment comm){
        comment = comm;
    }
    
    public void removeComment(OrderChargebackComment cmnt){
        if(cmnt!=null && cmnt.isManaged()){
            getInstance().getComments().remove(cmnt);
            getCrud().deleteObject(cmnt);            
        }
    }

    public String getProcessComment() {
        return processComment;
    }

    public void setProcessComment(String processComment) {
        this.processComment = processComment;
    }

    public File getAppealDocumentCover() {
        
        if(getInstance()!=null && getInstance().isManaged()){
            HashMap<String,Object> params = new HashMap<String,Object>();
            params.put("mrchntid",Helper.getCurrentUserMerchant().getId());
            params.put("mfiletype",MerchantFileType.CHARGEBACK_RESPONSE_TEMPLATE);
            List<MerchantFile> mfList = getCrud().getNamedList("MerchantFile.findMerchantFile", params);
            MerchantFile mf = null;
            if(mfList!=null && !mfList.isEmpty()){
                mf = mfList.get(0);
            }
            if(mf!=null){
                HashMap<String, String> keys = new HashMap<String, String>();
                try{
                    	        
                    keys.put("#today#", Helper.date2String(new Date()));
                    keys.put("#customer#", getOrderInquiry().getOrderInfo().getMemberName() + " " + getOrderInquiry().getOrderInfo().getMemberSurname());
                    keys.put("#paymenttotal#", String.valueOf(getOrderInquiry().getPaymentInfo().getPayAmount())); 
                    keys.put("#creditcardno#", Helper.checkNulls(getOrderInquiry().getPaymentInfo().getCreditCardNo(), "-")); 
                    keys.put("#agencyname#", getOrderInquiry().getOrderInfo().getAgent().getAgentName()); 
                    keys.put("#reservationdate#", Helper.date2String(getOrderInquiry().getOrderInfo().getOrderDate())); 

                    int productQuantity = getOrderInquiry().getOrderProducts().size();
                    int totalSize = 6;                    
                    int requiredSize = totalSize - productQuantity;
                    int i =0;
                    int size = 0;
                    for(OrderProduct op:getOrderInquiry().getOrderProducts()){
        		
        		keys.put("#productcode"+(i+1)+"#", op.getProductCode());
        		keys.put("#productname"+(i+1)+"#", op.getProductName());
        		keys.put("#count"+(i+1)+"#", op.getQuantity().toString()) ;
        		keys.put("#price"+(i+1)+"#", op.getPrice().toString());
        		if(i>=totalSize){
        			break;
        		}
                        i++;
                        size = i;
                    }
                    for(int a=size;a<=requiredSize; a++){
                        keys.put("#productcode"+(a+1)+"#", "");
                        keys.put("#productname"+(a+1)+"#", "");
                        keys.put("#count"+(a+1)+"#", "");
                        keys.put("#price"+(a+1)+"#", "");
                    }//end for
                }catch(Exception e){
                   e.printStackTrace();
                }
                if(keys!=null && keys.size()>0){
                    fileName = mf.getFileName();
                    fileType = mf.getFileType();
                    appealDocumentCover = WordDocumentReplace.readWordDoc(mf.getFileName(),mf.getFilePath(), keys);
                    
                }
            }
        }
        
        return appealDocumentCover;
    }

    public void setAppealDocumentCover(File appealDocumentCover) {
        this.appealDocumentCover = appealDocumentCover;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public JSFHelper getHelper() {
        return helper;
    }

    public void setHelper(JSFHelper helper) {
        this.helper = helper;
    }

    public List<ProcessProgress> getProcessProgresses() {
        return processProgresses;
    }

    public void setProcessProgresses(List<ProcessProgress> processProgresses) {
        this.processProgresses = processProgresses;
    }
    
    private void loadProcessProgressList(ChargebackProcessType cpt){
        HashMap<String, Object> params = Helper.getParamsHashByMerchant();
        params.put("prsstype",cpt);
        processProgresses.clear();
        processProgresses.addAll(getCrud().getNamedList("ProcessProgress.findByProcessTypeAndMerchant", params));
    }
    public void changeProcessType(ValueChangeEvent event){
        if(event.getNewValue()!=null){
            loadProcessProgressList((ChargebackProcessType)event.getNewValue());
        }
    }

    public List<LawReason> getLawReasons() {
        if(lawReasons.isEmpty()){
            lawReasons.addAll(getCrud().getNamedList("LawReason.findLawReasonByMerchant",Helper.getParamsHashByMerchant()));
        }
        return lawReasons;
    }

    public void setLawReasons(List<LawReason> lawReasons) {
        this.lawReasons = lawReasons;
    }

    /**
     * Ekranda Hukuk field'ların render olup olmamasını kontrol eder
     * @param pp 
     */
    private void checkRenderLaw(ProcessProgress pp){
        setLawRender(false);
        if(pp!=null && pp.getSentLaw().booleanValue()){
            setLawRender(true);
        }
    }
    
    public void changeProcessProgress(ValueChangeEvent event){
        if(event.getNewValue()!=null){
            checkRenderLaw((ProcessProgress)event.getNewValue());
        }
    }

    public boolean isLawRender() {
        return lawRender;
    }

    public void setLawRender(boolean lawRender) {
        this.lawRender = lawRender;
    }

    public CustomerCall getCustomerCall() {
        return customerCall;
    }

    public void setCustomerCall(CustomerCall customerCall) {
        this.customerCall = customerCall;
    }

    public List<CustomerCall> getCustomerCalls() {
        if(customerCalls.isEmpty() && getInstance().isManaged()){
            customerCall.setOrderChargeback(getInstance());
            customerCallAct.setInstance(customerCall);
            customerCalls.addAll(customerCallAct.getList());
        }
        return customerCalls;
    }

    public void setCustomerCalls(List<CustomerCall> customerCalls) {
        this.customerCalls = customerCalls;
    }
    public void newCustomerCall(){
        if(getInstance().isManaged()){
            customerCall.setOrderChargeback(getInstance());
        }
    }
    public void saveCustomerCall(){
        if(getInstance().isManaged()){
            customerCall.setOrderChargeback(getInstance());
            customerCallAct.setInstance(customerCall);
            customerCallAct.save();
            customerCall = new CustomerCall();
            customerCall.setOrderChargeback(getInstance());
            customerCalls.clear();
        }
    }

    public ChargebackFileAction getChargebackFileAction() {
        return chargebackFileAction;
    }

    public void setChargebackFileAction(ChargebackFileAction chargebackFileAction) {
        this.chargebackFileAction = chargebackFileAction;
    }

    public List<OrderChargebackFile> getChargebackFiles() {
        if(chargebackFiles.isEmpty() && getInstance().isManaged()){
            Map<String,Object> params = new HashMap<>();
            params.put("chrgbckid",getInstance().getId());
            chargebackFiles.addAll(getCrud().getNamedList("OrderChargebackFile.findOrderChargebackFiles",params));
        }
        return chargebackFiles;
    }

    public void setChargebackFiles(List<OrderChargebackFile> chargebackFiles) {
        this.chargebackFiles = chargebackFiles;
    }

    public CustomerCallAction getCustomerCallAct() {
        return customerCallAct;
    }

    public void setCustomerCallAct(CustomerCallAction customerCallAct) {
        this.customerCallAct = customerCallAct;
    }

    public OrderChargebackFile getChargebackFile() {
        return chargebackFile;
    }

    public void setChargebackFile(OrderChargebackFile chargebackFile) {
        this.chargebackFile = chargebackFile;
    }

    public List<DocumentType> getDocumentTypes() {
        if(documentTypes.isEmpty()){
            documentTypes.addAll(getCrud().getNamedList("DocumentType.findDocumentTypeByMerchant",Helper.getParamsHashByMerchant()));
        }
        return documentTypes;
    }

    public void setDocumentTypes(List<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public List<SelectItem> getDocumentTypeItems() {
        if(documentTypeItems==null || documentTypeItems.isEmpty()){
            documentTypeItems = new ArrayList<SelectItem>();

            List<SelectItem> list = new ArrayList<>();
            SelectItemGroup group1 = new SelectItemGroup("Gelen Evrak");
            for(DocumentType docType:getDocumentTypes()){
                if(!docType.getDocDirection().equals(DocDirection.INCOMING))continue;
                
                list.add(new SelectItem(docType,docType.getTitle()));
            }
            SelectItem[] items = new SelectItem[list.size()];
            int indx = 0;
            for(SelectItem si :list){
                items[indx] = si;
                indx++;
            }
            group1.setSelectItems(items);
            documentTypeItems.add(group1);
            
            list = new ArrayList<>();
            SelectItemGroup group2 = new SelectItemGroup("Giden Evrak");
            for(DocumentType docType:getDocumentTypes()){
                if(!docType.getDocDirection().equals(DocDirection.OUTGOING))continue;
                
                list.add(new SelectItem(docType,docType.getTitle()));
            }
            SelectItem[] items2 = new SelectItem[list.size()];
            indx = 0;
            for(SelectItem si :list){
                items2[indx] = si;
                indx++;
            }
            group2.setSelectItems(items2);
            documentTypeItems.add(group2);
            
            
        }
        return documentTypeItems;
    }

    public void setDocumentTypeItems(List<SelectItem> documentTypeItems) {
        this.documentTypeItems = documentTypeItems;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        if(getInstance()!=null && getInstance().isManaged()){
            chargebackFile.setOrderChargeback(getInstance());
            chargebackFileAction.setInstance(chargebackFile);
            boolean result = chargebackFileAction.handleFileUpload(event);
            if(result){
                chargebackFile = new OrderChargebackFile();
                chargebackFile.setOrderChargeback(getInstance());
                chargebackFiles.clear();
            }
        }
        
    }
    public void newChargebackFile(){
        chargebackFile = new OrderChargebackFile();
        chargebackFile.setOrderChargeback(getInstance());
    }
    public void deleteChargebackFiles(OrderChargebackFile cfile){
        try{
            cfile.setStatus(Status.DELETED);
            chargebackFileAction.delete(cfile);
            chargebackFiles.clear();
            Helper.addMessage(Helper.getMessage("Global.Record.Deleted"));
        }catch(Exception e){
            Helper.errorLogger(getClass(), e);
        }
    }
    
    
    
}
