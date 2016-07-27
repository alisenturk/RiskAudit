package com.riskaudit.entity.base;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author alisenturk
 */
@Embeddable
public class EMailInfo {
    
    private String  hostName;
    private String  smtpPort;
    private String  fromAddress;
    private String  username;
    private String  password;
    private boolean enableTLS       = false;
    private boolean authRequired    = false;

    @Column(length = 120,nullable = true)
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Column(length = 6,nullable = true)
    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    @Column(length = 60,nullable = true)
    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    @Column(length = 40)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")  
    public boolean isEnableTLS() {
        return enableTLS;
    }

    public void setEnableTLS(boolean enableTLS) {
        this.enableTLS = enableTLS;
    }

    @Column(nullable = false, columnDefinition = "TINYINT(1)")  
    public boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    
    
}
