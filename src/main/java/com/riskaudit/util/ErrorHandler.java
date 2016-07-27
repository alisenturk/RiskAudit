package com.riskaudit.util;

import java.io.Serializable;
import java.util.Date;


import com.riskaudit.enums.Constants;
import java.util.ArrayList;
import java.util.List;

public class ErrorHandler implements Serializable,Runnable {
	private String strClassName 	= "";
	private String strFileName  	= "";
	private String strLineNumber 	= "";
	private String strMethodName	= "";
	private String strErrorMessage	= "";
	private Class  className;	
	private Exception exception;
	private String	userName;
	private String	logtype 		= "error";
	private String	serverName		= "";
	private String	extraInfo		= "";
	
	public ErrorHandler() {
		super();
	}
		
	public ErrorHandler(Exception e){
		this(ErrorHandler.class, e);	
	}
	public ErrorHandler(Class c,Exception e){
		this(c, e,"");
	}	
	public ErrorHandler(Class c,Exception e,String username){
		this(c, e, username, "error");
	}
	public ErrorHandler(Class c,Exception e,String username,String logtype){
		this.className = c;
		this.exception = e;
		this.userName  = username;
		this.logtype   = logtype;	
	}
	
	@Override
	public void run() {
		logwrite();		
	}
	
	public void logwrite(){
		
            StackTraceElement[] errs = exception.getStackTrace();
            Date 	today 	= new Date();
            String	strDate	= Helper.date2String(today);
            String	time	= Helper.date2String(today, "HH:mm:ss"); 
            ErrorClass  error   = null;
            
            String      errorProvider   = Helper.getAppParameterValue("errorlog.provider");
            
            List<ErrorClass>    errorList = new ArrayList<>();
            
            for(StackTraceElement er:errs){ 			

                strClassName 	= er.getClassName();
                strFileName		= er.getFileName();
                strLineNumber	= String.valueOf(er.getLineNumber());
                strMethodName	= er.getMethodName();
                strErrorMessage	= exception.getMessage();

                if(strClassName==null || strClassName.length()==0 || 
                    (strClassName.indexOf("com.riskaudit")==-1 && 
                     strClassName.indexOf("com.alisenturk")==-1 &&   strClassName.indexOf("com.penna")==-1  && 
                     strClassName.indexOf("com.sirena")==-1 &&  strClassName.indexOf("com.ads")==-1 && 
                     strClassName.indexOf("PageExpiryFilter")==-1 &&   strClassName.indexOf("com.kartaca")==-1 )
                )continue;
                    
                error = new ErrorClass();
                error.setProject(Helper.getAppParameterValue("project.name"));
                error.setClassName(strClassName);
                error.setFileName(strFileName);
                error.setMethodName(strMethodName);
                error.setLineNumber(strLineNumber);
                error.setUserName(userName);
                error.setErrorMessage(strErrorMessage);
                error.setExtraInfo(extraInfo);
                error.setErrorDate(strDate);
                error.setErrorTime(time);
                error.setProcessDate(Helper.date2String(today,"yyyyMMddHHmmss"));
                error.setServerIP(Helper.getServerIPAddress());
                
                errorList.add(error);
            }
            
            if(errorProvider.equalsIgnoreCase("elastic")){
                ElasticLogger eLogger = new ElasticLogger();
                eLogger.write(errorList);
            }else{
                String strErrorMsgFormat = "%s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s ";
                for(ErrorClass err:errorList){
                    System.err.printf(strErrorMsgFormat, err.getProject(),err.getServerIP(),err.getErrorDate(),err.getErrorTime(),
                                      err.getFileName(),err.getClassName(),err.getMethodName(),err.getLineNumber(),
                                      err.getUserName(),err.getErrorMessage(),exception,err.getExtraInfo());
                }
            }


	}
	
	
	public String getStrClassName() {
		return strClassName;
	}

	public void setStrClassName(String strClassName) {
		this.strClassName = strClassName;
	}

	public String getStrFileName() {
		return strFileName;
	}

	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}

	public String getStrLineNumber() {
		return strLineNumber;
	}

	public void setStrLineNumber(String strLineNumber) {
		this.strLineNumber = strLineNumber;
	}

	public String getStrMethodName() {
		return strMethodName;
	}

	public void setStrMethodName(String strMethodName) {
		this.strMethodName = strMethodName;
	}

	public String getStrErrorMessage() {
		return strErrorMessage;
	}

	public void setStrErrorMessage(String strErrorMessage) {
		this.strErrorMessage = strErrorMessage;
	}

	public Class getClassName() {
		return className;
	}

	public void setClassName(Class className) {
		this.className = className;
	}

	public String getLogtype() {
		return logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	

}
