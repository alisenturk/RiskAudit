package com.riskaudit.servlet;

import java.io.Serializable;

/**
 *
 * @author alisenturk
 */
public class FileDownload implements Serializable{
    
    private String contentType  = "";
    private String fileName     = "";
    private String filePath     = "";

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    
    
}
