package com.riskaudit.action.schedule;

import com.riskaudit.action.base.CrudService;
import com.riskaudit.entity.base.DailyExchange;
import com.riskaudit.enums.Currency;
import com.riskaudit.util.Helper;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author asenturk
 */
@Named("TCMBDailyExchangeReader")
public class TCMBDailyExchangeReader implements Serializable {

    private Date exchangeDate = new Date();
    
    @Inject
    CrudService crud;
   
    private String          tcmbTodayURL    = "http://www.tcmb.gov.tr/kurlar/today.xml";
    private String          tcmbKurlaURL    = "http://www.tcmb.gov.tr/kurlar/";
    private XMLInputFactory inputFactory    = null;
    private XMLEventReader  xmlEventReader  = null;

    public TCMBDailyExchangeReader() {
        inputFactory = XMLInputFactory.newInstance();
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }
    
    
    
    @Transactional
    public void read() throws Exception {
        try {
    
            if (inputFactory == null) {
                inputFactory = XMLInputFactory.newInstance();
            }
            Date today = new Date();
            Date date   = getExchangeDate();
            
            if(date.compareTo(today)==0 && (date.getHours() > 15 || (date.getHours() == 15 && date.getMinutes() > 29)) ) {
               date = Helper.dateAdd(date, 1);
            }
            
            String path = tcmbTodayURL;
            if(date.compareTo(today)!=0){
                Date prevDay = Helper.dateAdd(date, -1);
                int     dateOfMonth = prevDay.getDate();
                if(prevDay.getDay()==0){
                    dateOfMonth = prevDay.getDate()-2;
                }
                if(prevDay.getDay()==6){
                    dateOfMonth = prevDay.getDate()-1;
                }
                String  strYear = String.valueOf(prevDay.getYear()+1900);
                String  strMonth= String.valueOf((prevDay.getMonth()+1)<10?"0"+(prevDay.getMonth()+1):(prevDay.getMonth()+1));
                String  strDate = String.valueOf(dateOfMonth<10?"0"+dateOfMonth:dateOfMonth);
                path = tcmbKurlaURL + strYear+strMonth+"/"+strDate+strMonth+strYear+".xml";
            }
            URL u = new URL(path);
            URLConnection conn = u.openConnection();
            boolean isHoliday = false;
            try{
                InputStreamReader isr = new InputStreamReader((InputStream) conn.getContent());
                xmlEventReader = inputFactory.createXMLEventReader(isr);
            }catch(java.io.FileNotFoundException e){
                conn = null;
                u    = null;
                isHoliday = true;
            }
            
                
            if(!isHoliday){
                HashMap<String,Object> params = new HashMap<String,Object>();
                params.put("currdate", date);
                crud.excutedUpdateNamedQuery("DailyExchange.deleteDateExchange", params);
                double forexBuying = 0d, forexSelling = 0d, banknoteBuying = 0d, banknoteSelling = 0d, crossRateUSD = 0d;
                String currencyCode = "";
                String nodeName = "";
                while (xmlEventReader.hasNext()) {

                    XMLEvent xmlEvent = xmlEventReader.nextEvent();

                    if (xmlEvent.isStartElement()) {
                        nodeName = xmlEvent.asStartElement().getName().toString();

                        Attribute codeAtt = xmlEvent.asStartElement().getAttributeByName(new QName("CurrencyCode"));

                        if (codeAtt != null) {
                            currencyCode = codeAtt.getValue();
                        }

                    } else if (xmlEvent.isCharacters()) {

                        if (nodeName.equalsIgnoreCase("ForexBuying")) {
                            forexBuying = Double.valueOf(xmlEvent.asCharacters().getData());
                        } else if (nodeName.equalsIgnoreCase("ForexSelling")) {
                            forexSelling = Double.valueOf(xmlEvent.asCharacters().getData());
                        } else if (nodeName.equalsIgnoreCase("BanknoteBuying")) {
                            banknoteBuying = Double.valueOf(xmlEvent.asCharacters().getData());
                        } else if (nodeName.equalsIgnoreCase("BanknoteSelling")) {
                            banknoteSelling = Double.valueOf(xmlEvent.asCharacters().getData());
                        } else if (nodeName.equalsIgnoreCase("CrossRateUSD")) {
                            crossRateUSD = Double.valueOf(xmlEvent.asCharacters().getData());
                        } else if (nodeName.equalsIgnoreCase("CrossRateOther") && crossRateUSD == 0) {
                            crossRateUSD = (1d / Double.valueOf(xmlEvent.asCharacters().getData()));
                        }

                    } else if (xmlEvent.isEndElement()) {
                        if (xmlEvent.asEndElement().getName().toString().equalsIgnoreCase("Currency")) {

                            Currency currencyType = Helper.getCurrency(currencyCode);
                            if (currencyType != null && forexBuying > 0 && forexSelling > 0 && banknoteBuying > 0 && banknoteSelling > 0) {
                                DailyExchange de = new DailyExchange();
                                de.setDate(date);
                                de.setCurrency(currencyType);
                                de.setBuyingPrice(forexBuying);
                                de.setSellingPrice(forexSelling);
                                de.setEffectiveBuyingPrice(banknoteBuying);
                                de.setEffectiveSellingPrice(banknoteSelling);
                                de.setCrossRateUSD(crossRateUSD);
                                crud.createObject(de);

                                currencyCode = "";
                                forexBuying = 0d;
                                forexSelling = 0d;
                                banknoteBuying = 0d;
                                banknoteSelling = 0d;
                                crossRateUSD = 0d;

                            } else {
                                currencyCode = "";
                                forexBuying = 0d;
                                forexSelling = 0d;
                                banknoteBuying = 0d;
                                banknoteSelling = 0d;
                                crossRateUSD = 0d;
                            }
                        }
                    }
                }

                xmlEventReader.close();

                DailyExchange de = new DailyExchange();
                de.setDate(date);
                de.setCurrency(Helper.getCurrency("TRY"));
                de.setBuyingPrice(1d);
                de.setSellingPrice(1d);
                de.setEffectiveBuyingPrice(1d);
                de.setEffectiveSellingPrice(1d);

                crud.createObject(de);
            } //end of else 
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}
