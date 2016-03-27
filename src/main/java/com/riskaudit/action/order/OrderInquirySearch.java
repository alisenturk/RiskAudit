package com.riskaudit.action.order;

import com.riskaudit.action.base.CrudService;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.base.User;
import com.riskaudit.entity.order.OrderInquiry;
import com.riskaudit.enums.ChargebackProcessType;
import com.riskaudit.enums.Constants;
import com.riskaudit.util.Helper;
import com.riskaudit.util.JSFHelper;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author asenturk
 */
@Named(value = "inquirySearch")
@ViewScoped
public class OrderInquirySearch implements Serializable{
    @Inject
    CrudService crud;
        
    @Inject
    JSFHelper   jsfHelper;
    
    private String      orderNo;
    private Date        orderBeginDate;
    private Date        orderEndDate;
    private Date        objectionBeginDate;
    private Date        objectionEndDate;
    private Date        chargebackBeginDate;
    private Date        chargebackEndDate;
    private String      memberName;
    private String      memberSurname;
    private String      memberUsername;
    private User        fraudController;
    private Merchant    merchant = Helper.getCurrentUserMerchant();
    
    private ChargebackProcessType processType;
            
    private List<User>              merchantFraudControllers    = new ArrayList<User>();
    private List<OrderInquiry>      inquiries                   = new ArrayList<OrderInquiry>();

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public List<User> getMerchantFraudControllers() {
        if(merchantFraudControllers.isEmpty()){
            merchantFraudControllers.addAll(crud.getNamedList("User.findMerchantFraudControllers",Helper.getParamsHashByMerchant()));
        }
        return merchantFraudControllers;
    }

    public void setMerchantFraudControllers(List<User> merchantFraudControllers) {
        this.merchantFraudControllers = merchantFraudControllers;
    }

    public List<OrderInquiry> getInquiries() {
        if(inquiries.isEmpty() && jsfHelper.getSessionValue(Constants.InquirySearchResult.getValue())!=null){
            inquiries = (List)jsfHelper.getSessionValue(Constants.InquirySearchResult.getValue());
        }
        return inquiries;
    }
    
    public void setInquiries(List<OrderInquiry> inquiries) {
        this.inquiries = inquiries;
    }
    
    
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderBeginDate() {
        return orderBeginDate;
    }

    public void setOrderBeginDate(Date orderBeginDate) {
        this.orderBeginDate = orderBeginDate;
    }

    public Date getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(Date orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberSurname() {
        return memberSurname;
    }

    public void setMemberSurname(String memberSurname) {
        this.memberSurname = memberSurname;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public User getFraudController() {
        return fraudController;
    }

    public void setFraudController(User fraudController) {
        this.fraudController = fraudController;
    }

    public Date getObjectionBeginDate() {
        return objectionBeginDate;
    }

    public void setObjectionBeginDate(Date objectionBeginDate) {
        this.objectionBeginDate = objectionBeginDate;
    }

    public Date getObjectionEndDate() {
        return objectionEndDate;
    }

    public void setObjectionEndDate(Date objectionEndDate) {
        this.objectionEndDate = objectionEndDate;
    }

    public Date getChargebackBeginDate() {
        return chargebackBeginDate;
    }

    public void setChargebackBeginDate(Date chargebackBeginDate) {
        this.chargebackBeginDate = chargebackBeginDate;
    }

    public Date getChargebackEndDate() {
        return chargebackEndDate;
    }

    public void setChargebackEndDate(Date chargebackEndDate) {
        this.chargebackEndDate = chargebackEndDate;
    }

    public ChargebackProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ChargebackProcessType processType) {
        this.processType = processType;
    }
    
    
    
    
    public void searchInquiry(){
        inquiries = new ArrayList<>();
        
        try{   
            jsfHelper.removeSessionValue(Constants.InquirySearchResult.getValue());
            OrderInquiryQuery query = new OrderInquiryQuery();
            query.setOrderNo(orderNo);
            query.setOrderBeginDate(orderBeginDate);
            query.setOrderEndDate(orderEndDate);
            query.setMemberUsername(memberUsername);
            query.setMemberName(memberName);
            query.setMemberSurname(memberSurname);
            query.setFraudController(fraudController);
            query.setMerchant(merchant);
            query.setProcessType(processType);
            query.setAppealBeginDate(objectionBeginDate);
            query.setAppealEndDate(objectionEndDate);
            query.setChargebackBeginDate(chargebackBeginDate);
            query.setChargebackEndDate(chargebackEndDate);
            
            inquiries.addAll(crud.getList(query.getInqueryQuery(),query.getParams()));
            if(inquiries!=null && inquiries.size()>0){
                jsfHelper.setSessionValue(Constants.InquirySearchResult.getValue(),inquiries);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public StreamedContent export2Excel(){
        StreamedContent file = null;
        try{
            List<String> labels = new ArrayList<String>();
            labels.add("Sipariş No");
            labels.add("Sipariş Tarihi");
            labels.add("Müşteri Adı Soyadı");
            labels.add("Müşteri E-Posta");
            labels.add("Pazar Yeri");
            labels.add("Acenta");
            labels.add("Sipariş Tutarı");
            labels.add("Sipariş Para Birimi");
            labels.add("Sipariş Durumu");
            labels.add("Ödeme Tipi");
            labels.add("Ödeme Güvenlik Tipi");
            labels.add("Fraud Kontrolcü");
            labels.add("Pos Bankası");
            labels.add("Kredi Kart No");
            labels.add("Kart Bankası");
            labels.add("Kart Sahibi");
            labels.add("Ödeme Tutarı");
            labels.add("Ödeme Para Birimi");
            labels.add("Chargeback Kodu");
            labels.add("İtiraz Bildirim Tarihi");
            labels.add("İşlem Tipi");
            
            HSSFWorkbook workbook   = new HSSFWorkbook();
            HSSFSheet   sheet       = workbook.createSheet("ChargebackRaporu");
            
            Cell            cell            = null;
            HSSFCellStyle   headerStyle     = workbook.createCellStyle();
            HSSFFont        headerStyleFont = workbook.createFont();
            headerStyleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            headerStyle.setFont(headerStyleFont);
            headerStyle.setAlignment((short)3); //right
            headerStyle.setFillPattern(HSSFCellStyle.FINE_DOTS);
            headerStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
            headerStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            
            CellStyle cellDateStyle = workbook.createCellStyle();
            DataFormat poiFormat = workbook.createDataFormat();
            cellDateStyle.setDataFormat(poiFormat.getFormat("dd/MM/yyyy"));
            
            int rownum  = 0;
            int colnum  = 0;
            Row row = sheet.createRow(rownum);
            for(String label:labels){
                cell = row.createCell(colnum);
                cell.setCellValue(label);
                cell.setCellStyle(headerStyle);
                colnum++;
            }
            colnum=0;
            rownum++;
            searchInquiry();
            for(OrderInquiry order:inquiries){
                row = sheet.createRow(rownum);
                
                cell = row.createCell(colnum);
                cell.setCellValue(order.getOrderInfo().getOrderNo());
                colnum++;
                
                cell = row.createCell(colnum);
                cell.setCellValue(order.getOrderInfo().getOrderDate());
                cell.setCellStyle(cellDateStyle);
                colnum++;
                
                cell = row.createCell(colnum);
                if(order.getOrderInfo().getMemberName()!=null && order.getOrderInfo().getMemberSurname()!=null){
                    cell.setCellValue(order.getOrderInfo().getMemberName() + " " + order.getOrderInfo().getMemberSurname());
                }else{
                    cell.setCellValue("");
                }
                colnum++;
                
                cell = row.createCell(colnum);
                if(order.getOrderInfo().getMemberUsername()!=null){
                    cell.setCellValue(order.getOrderInfo().getMemberUsername());
                }else{
                    cell.setCellValue("");
                }
                colnum++;
                
                cell = row.createCell(colnum);
                if(order.getOrderInfo().getMarketPlace()!=null){
                    cell.setCellValue(order.getOrderInfo().getMarketPlace().getLabel());
                }else{
                    cell.setCellValue("");
                }
                colnum++;
                
                cell = row.createCell(colnum);
                if(order.getOrderInfo().getAgent()!=null){
                    cell.setCellValue(order.getOrderInfo().getAgent().getAgentName());
                }else{
                    cell.setCellValue("");
                }
                colnum++;

                cell = row.createCell(colnum);
                if(order.getOrderInfo().getOrderTotal()!=null){
                    cell.setCellValue(order.getOrderInfo().getOrderTotal());
                }else{
                    cell.setCellValue("");
                }
                colnum++;
                
                cell = row.createCell(colnum);
                cell.setCellValue(order.getOrderInfo().getOrderCurrency().getKey());
                colnum++;
                
                cell = row.createCell(colnum);
                if(order.getOrderInfo().getOrderStatus()!=null){
                    cell.setCellValue(order.getOrderInfo().getOrderStatus().getStatusDescription());
                }else{
                    cell.setCellValue("");
                }
                colnum++;

                if(order.getPaymentInfo()!=null){
                    cell = row.createCell(colnum);
                    if(order.getPaymentInfo().getPaymentMethod()!=null){
                        cell.setCellValue(order.getPaymentInfo().getPaymentMethod().getLabel());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;

                    cell = row.createCell(colnum);
                    if(order.getPaymentInfo().getPaymentSecureType()!=null){
                        cell.setCellValue(order.getPaymentInfo().getPaymentSecureType().getLabel());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;

                    cell = row.createCell(colnum);
                    if(order.getPaymentInfo().getFraudController()!=null){
                        cell.setCellValue(order.getPaymentInfo().getFraudController().getNameSurname());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;

                    cell = row.createCell(colnum);
                    if(order.getPaymentInfo().getPosBank()!=null){
                        cell.setCellValue(order.getPaymentInfo().getPosBank().getBankName());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;

                    cell = row.createCell(colnum);
                    if(order.getPaymentInfo().getCreditCardNo()!=null){
                        cell.setCellValue(order.getPaymentInfo().getCreditCardNo());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;
                    
                    cell = row.createCell(colnum);
                    if(order.getPaymentInfo().getCardBank()!=null){
                        cell.setCellValue(order.getPaymentInfo().getCardBank().getBankName());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;
                    
                    cell = row.createCell(colnum);                
                    cell.setCellValue(order.getPaymentInfo().getCardHolder());
                    colnum++;

                    cell = row.createCell(colnum);
                    if(order.getPaymentInfo().getPayAmount()!=null){
                        cell.setCellValue(order.getPaymentInfo().getPayAmount());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;

                    cell = row.createCell(colnum);
                    if(order.getPaymentInfo().getPayCurrency()!=null){
                        cell.setCellValue(order.getPaymentInfo().getPayCurrency().getKey());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;
                }
                if(order.getOrderChargeback()!=null){
                    cell = row.createCell(colnum);                
                    if(order.getOrderChargeback().getChargebackCode()!=null){
                        cell.setCellValue(order.getOrderChargeback().getChargebackCode().getTitle());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;

                    cell = row.createCell(colnum);
                    if(order.getOrderChargeback().getAppealDeclarationDate()!=null){
                        cell.setCellValue(order.getOrderChargeback().getAppealDeclarationDate());
                        cell.setCellStyle(cellDateStyle);
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;

                    cell = row.createCell(colnum);
                    if(order.getOrderChargeback().getProcessType()!=null){
                        cell.setCellValue(order.getOrderChargeback().getProcessType().getLabel());
                    }else{
                        cell.setCellValue("");
                    }
                    colnum++;
                }
                rownum++;
                colnum=0;
            }
            String excelFileName = "chargeback.xls";
            FileOutputStream fos = new FileOutputStream(excelFileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            InputStream stream = new BufferedInputStream(new FileInputStream(excelFileName));
            file = new DefaultStreamedContent(stream, "application/xls", excelFileName);

           
        }catch(Exception e){
            e.printStackTrace();
        }
        return file;
    }
    
    
}
