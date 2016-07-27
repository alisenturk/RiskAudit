package com.riskaudit.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Postwoman {

    private String hostName;
    private int smtpPort;
    private String fromAddress;
    private String messageSubject;
    private String messageBody;
    private List<String> toList;
    private List<String> ccList;
    private List<String> bccList;
    private boolean result;
    private MimeMessage message;
    private boolean authRequired;
    private boolean enableTLS;
    private String username;
    private String password;
    private boolean noreply;

    public class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    private void init() {
        hostName = Helper.getAppParameterValue("mail.host");
        smtpPort = 25;
        fromAddress = "info@riskaudit.com.tr";
        messageSubject = "No Subject";
        messageBody = "No body";
        toList = new ArrayList<>();
        ccList = new ArrayList<>();
        bccList = new ArrayList<>();
        result = false;
        message = null;
        authRequired = false;
        enableTLS = false;
        username = Helper.getAppParameterValue("mail.username");
        password = Helper.getAppParameterValue("mail.passsword");
        noreply = true;
    }

    public Postwoman() {
        this(false, false, 25);
    }

    public Postwoman(boolean authRequired, boolean enableTLS, int smtpPort) {
        this("info@riskaudit.com.tr", authRequired, enableTLS, smtpPort);
    }

    public Postwoman(String fromAddress, boolean authRequired, boolean enableTLS, int smtpPort) {
        init();
        this.authRequired = authRequired;
        this.smtpPort = smtpPort;
        this.fromAddress = fromAddress;
        this.enableTLS = enableTLS;

    }

    private InternetAddress[] noReply() throws AddressException {
        InternetAddress reply[] = new InternetAddress[1];
        reply[0] = new InternetAddress("noreply");
        return reply;
    }

    private void loadSettings() {
        Session session;

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", hostName);
        props.put("mail.smtp.port", this.smtpPort);
        if (enableTLS) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        if (this.authRequired) {
            props.put("mail.smtp.auth", "true");
        }

        if (authRequired) {
            Authenticator auth = new SMTPAuthenticator();
            session = Session.getInstance(props, auth);
        } else {
            session = Session.getInstance(props);
        }
        message = new MimeMessage(session);
    }

    public void send() {
        try {
            loadSettings();

            InternetAddress from = new InternetAddress(fromAddress);
            message.setFrom(from);
            message.setContent(messageBody, "text/html; charset=UTF-8");
            message.setSubject(messageSubject, "UTF-8");

            for (String toAddress : toList) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            }
            for (String ccAddress : ccList) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress));
            }
            for (String bccAddress : bccList) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccAddress));
            }
            if (noreply) {
                message.setReplyTo(noReply());
            }
            Transport.send(message);
            result = true;
        } catch (Exception e) {
            Helper.errorLogger(getClass(), e);
            result = false;
        }
    }

    public void send(String filePath) {
        try {

            loadSettings();

            InternetAddress from = new InternetAddress(fromAddress);
            message.setFrom(from);

            if (filePath != null && filePath.length() > 3) {
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setContent(messageBody, "text/html; charset=UTF-8");

                MimeBodyPart attachFilePart = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(filePath);
                attachFilePart.setDataHandler(new DataHandler(fds));
                attachFilePart.setFileName(fds.getName());

                Multipart mp = new MimeMultipart();
                mp.addBodyPart(textPart);
                mp.addBodyPart(attachFilePart);
                message.setContent(mp);

            } else {
                message.setContent(messageBody, "text/html; charset=UTF-8");
            }

            message.setSubject(messageSubject, "UTF-8");

            for (String toAddress : toList) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            }
            for (String ccAddress : ccList) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress));
            }
            for (String bccAddress : bccList) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccAddress));
            }
            if (noreply) {
                message.setReplyTo(noReply());
            }

            Transport.send(message);
            result = true;
        } catch (RuntimeException | MessagingException e) {
            Helper.errorLogger(getClass(), e);
            result = false;
        }
    }

    public void send(String urlPath, String fileName) {
        try {

            loadSettings();

            InternetAddress from = new InternetAddress(fromAddress);
            message.setFrom(from);

            if (fileName != null) {
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setContent(messageBody, "text/html; charset=UTF-8");

                MimeBodyPart attachFilePart = new MimeBodyPart();
                attachFilePart.setDataHandler(new DataHandler(new URL(urlPath)));
                attachFilePart.setFileName(fileName);

                Multipart mp = new MimeMultipart();
                mp.addBodyPart(textPart);
                mp.addBodyPart(attachFilePart);
                message.setContent(mp);

            } else {
                message.setContent(messageBody, "text/html; charset=UTF-8");
            }

            message.setSubject(messageSubject, "UTF-8");

            for (String toAddress : toList) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            }
            for (String ccAddress : ccList) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress));
            }
            for (String bccAddress : bccList) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccAddress));
            }
            if (noreply) {
                message.setReplyTo(noReply());
            }

            Transport.send(message);
            result = true;
        } catch (RuntimeException | MessagingException | MalformedURLException e) {
            Helper.errorLogger(getClass(), e);
            result = false;
        }
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    public List<String> getCcList() {
        return ccList;
    }

    public void setCcList(List<String> ccList) {
        this.ccList = ccList;
    }

    public List<String> getBccList() {
        return bccList;
    }

    public void setBccList(List<String> bccList) {
        this.bccList = bccList;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public MimeMessage getMessage() {
        return message;
    }

    public void setMessage(MimeMessage message) {
        this.message = message;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
