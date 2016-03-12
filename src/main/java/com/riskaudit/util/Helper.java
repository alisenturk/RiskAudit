package com.riskaudit.util;

import com.google.gson.Gson;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.entity.base.User;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.UserType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;
import org.primefaces.json.JSONArray;

/**
 *
 * @author asenturk
 */
@Stateless
public class Helper implements Serializable {

    public static void addMessage(String msg) {
        addMessage("", msg, FacesMessage.SEVERITY_INFO);
    }
    public static void addMessage(String msg,FacesMessage.Severity severity) {
        addMessage("", msg, severity);
    }
    public static void addMessage(String summary,String msg,FacesMessage.Severity severity) {
        FacesMessage fMsg = new FacesMessage(severity,msg,"");
        FacesContext.getCurrentInstance().addMessage(null, fMsg);
    }

    public static Locale getLocale(String language) {
        String firstLang = "tr";
        String secondLang = "TR";

        if (language == null || language.equals("")) {
            language = "TR";
        }
        if (language != null && language.equals("EN")) {
            firstLang = "en";
            secondLang = "US";
        } else if (language != null && language.equals("IR")) {
            language = "IR";
            firstLang = "ir";
            secondLang = "IR";
        } else if (language != null && language.equals("UA")) {
            language = "UA";
            firstLang = "uk";
            secondLang = "UA";
        } else if (language != null && language.equals("RU")) {
            language = "RU";
            firstLang = "ru";
            secondLang = "RU";
        } else {
            language = "TR";
            firstLang = "tr";
            secondLang = "TR";
        }
        return new Locale(firstLang, secondLang);
    }

    public static String getMessage(String messageKey) {
        String msgValue = "";
        try {

            FacesContext ctx = FacesContext.getCurrentInstance();
            String bundleName = ctx.getApplication().getMessageBundle();
            ResourceBundle message = ResourceBundle.getBundle(bundleName);
            msgValue = message.getString(messageKey);

            return msgValue;
        } catch (Exception e) {
            msgValue = messageKey;
            e.printStackTrace();
        }
        return messageKey;
    }

    public static String getMessage(String messageKey, String lang) {
        try {
            String msgValue = "";
            FacesContext ctx = FacesContext.getCurrentInstance();
            String bundleName = ctx.getApplication().getMessageBundle();

            ResourceBundle message = ResourceBundle.getBundle(bundleName, getLocale(lang));
            msgValue = message.getString(messageKey);

            return msgValue;
        } catch (Exception e) {
            //Utils.errorLogger(Utils.class, e);
            e.printStackTrace();
        }
        return messageKey;
    }

    public static User getCurrentUserFromSession() {
        User user = null;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (session != null && session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
        }

        return user;
    }

    public static Merchant getCurrentUserMerchant() {
        Merchant merchant = null;
        User user = Helper.getCurrentUserFromSession();
        if (user != null && user.getMerchant() != null) {
            merchant = user.getMerchant();
        }
        return merchant;
    }

    public static EntityManager getEntityManager() {

        EntityManager entityManager = Persistence.createEntityManagerFactory("RISKAUDIT_PU").createEntityManager();
        return entityManager;
    }

    public static String getUserNameAndSurname(Long userId) {
        String nameAndSurname = "";
        /*try{
         EntityManager em = getEntityManager();
         if(em!=null && em.isOpen()){
         User user = em.find(User.class, userId);
         nameAndSurname = user.getName() + " " + user.getLastname();
         }
         }catch(Exception e){
         e.printStackTrace();
         }*/

        return nameAndSurname;
    }

    public static JSONArray readRestService(String urlPath) {
        String result = "";
        Client client = null;
        WebResource webResource = null;
        ClientResponse response = null;
        try {
            client = Client.create();
            webResource = client.resource(urlPath);
            response = webResource.accept("application/json").get(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            result = response.getEntity(String.class);
            return new JSONArray(result);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            webResource = null;
            response = null;
            client = null;
        }

        return null;
    }

    public static String utfConvStrEng(String str) {
        int unicodeIntValue = 0;
        String unicodeString = "";
        String ucValue = "";
        if (str == null) {
            return null;
        }
        try {
            int length = str.length();

            for (int i = 0; i < length; i++) {
                unicodeIntValue = str.charAt(i);

                if (unicodeIntValue == 38) {//& ile basliyorsa
                    ucValue = "" + str.charAt(i) + str.charAt(i + 1) + str.charAt(i + 2) + str.charAt(i + 3) + str.charAt(i + 4);
                    if (ucValue != null && ucValue.equals("&#304")) {
                        unicodeString += "I";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#305")) {
                        unicodeString += "i";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#214")) {
                        unicodeString += "O";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#246")) {
                        unicodeString += "o";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#220")) {
                        unicodeString += "U";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#252")) {
                        unicodeString += "u";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#199")) {
                        unicodeString += "C";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#231")) {
                        unicodeString += "c";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#286")) {
                        unicodeString += "G";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#287")) {
                        unicodeString += "g";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#350")) {
                        unicodeString += "S";
                        i = i + 5;
                        continue;
                    }
                    if (ucValue != null && ucValue.equals("&#351")) {
                        unicodeString += "s";
                        i = i + 5;
                        continue;
                    }
                }

                if (unicodeIntValue == 221 || unicodeIntValue == 304) {
                    unicodeString += "I";
                    continue;
                }
                if (unicodeIntValue == 253 || unicodeIntValue == 305) {
                    unicodeString += "i";
                    continue;
                }
                if (unicodeIntValue == 254 || unicodeIntValue == 351) {
                    unicodeString += "s";
                    continue;
                }
                if (unicodeIntValue == 222 || unicodeIntValue == 350) {
                    unicodeString += "S";
                    continue;
                }
                if (unicodeIntValue == 208 || unicodeIntValue == 286) {
                    unicodeString += "G";
                    continue;
                }
                if (unicodeIntValue == 240 || unicodeIntValue == 287) {
                    unicodeString += "g";
                    continue;
                }
                if (unicodeIntValue == 231) {
                    unicodeString += "c";
                    continue;
                }
                if (unicodeIntValue == 199) {
                    unicodeString += "C";
                    continue;
                }
                if (unicodeIntValue == 252) {
                    unicodeString += "u";
                    continue;
                }
                if (unicodeIntValue == 220) {
                    unicodeString += "U";
                    continue;
                }
                if (unicodeIntValue == 246) {
                    unicodeString += "o";
                    continue;
                }
                if (unicodeIntValue == 214) {
                    unicodeString += "O";
                    continue;
                }

                unicodeString += (char) unicodeIntValue;
            }
            return unicodeString;
        } catch (Exception e) {

            return "";
        }
    }

    public static Date dateAddMinute(Date date, int minute) {
        Date newdate = date;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, minute);
            newdate = cal.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newdate;
    }

    public static Date dateAdd(Date date, int day) {
        Date newdate = date;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, day);
            newdate = cal.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newdate;
    }

    public static boolean getCurrentUserIsAdmin() {
        boolean isAdmin = false;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.getUserType().equals(UserType.ADMIN)) {
                isAdmin = true;
            }
        }

        return isAdmin;
    }

    public static HashMap<String, Object> getParamsHashByMerchant() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        Merchant merchant = getCurrentUserMerchant();
        params.put("mrchntid", merchant.getId());
        return params;

    }

    public static Date dateAddMonth(Date date, int month) {
        Date newdate = date;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, month);
            newdate = cal.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newdate;
    }

    public static Currency getCurrency(String currencyCode) {
        Currency currency = null;
        try {
            currency = Currency.valueOf(currencyCode);
        } catch (IllegalArgumentException e) {
            currency = null;
        }

        return currency;
    }

    public static double roundDecimal(double val) {
        double newval = new BigDecimal(val).setScale(2, RoundingMode.HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("#.##");
        return new Double(df.format(newval));
    }

    public static boolean checkUserLicense(String merchantName, Date expireDate, String hash) {
        boolean isValid = false;
        if (merchantName != null && expireDate != null && hash != null) {
            String newHash = Helper.generateMD5(Helper.date2String(expireDate) + merchantName);
            if (newHash.equals(hash) && Helper.dateDifferent(new Date(), expireDate,Calendar.DATE)>=0){
                isValid = true;
            }
        }
        return isValid;
    }

    public static String generateMD5(String password) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1	        
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String date2String(Date date) {
        return date2String(date, "dd/MM/yyyy");
    }

    public static String date2String(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public static double dateDifferent(Date from, Date to, int differentType) {

        if (from == null) {
            from = new Date();
        }
        if (to == null) {
            to = new Date();
        }

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(from);
        calendar2.setTime(to);
        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();
        double diff = milliseconds2 - milliseconds1;

        double left = 0;
        if (differentType == Calendar.SECOND) {
            left = (diff / 1000l);
        } else if (differentType == Calendar.MINUTE) {
            left = (diff / (60l * 1000l));
        } else if (differentType == Calendar.HOUR) {
            left = (diff / (60l * 60l * 1000l));
        } else if (differentType == Calendar.DATE) {
            left = Helper.roundDecimal((diff / (24d * 60d * 60d * 1000d)));
        } else if (differentType == Calendar.MILLISECOND) {
            left = diff;
        } else if (differentType == Calendar.YEAR) {
            left = Helper.roundDouble((diff / (365d * 24d * 60d * 60d * 1000d)));
        }

        return left;
    }

    public static double roundDouble(double input) {
        return Math.round(input * Math.pow(10, (double) 2.0)) / Math.pow(10, (double) 2.0);
    }
    
    public static Date string2Date(String date,String format){
        Date newDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            newDate = sdf.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return newDate; 
    }
    
    public static String object2Json(Object obj){
        String json = "";
        Gson gson = new Gson();
        json = gson.toJson(obj);
        return json;
    }
}
