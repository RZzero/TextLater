package com.gestion.textlater.textlater;

/**
 * Created by kyonru on 4/04/17.
 */

public class Message {

    private String platform;
    private String sender;
    private String ToM;
    private String subject;
    private String content;
    private String timeToSend;
    private String messageStatus;


    public Message(String platform, String sender, String toM, String subject, String content, String timeToSend, String messageStatus) {
        this.platform = platform;
        this.sender = sender;
        ToM = toM;
        this.subject = subject;
        this.content = content;
        this.timeToSend = timeToSend;
        this.messageStatus = messageStatus;
    }


    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        sender = sender;
    }

    public String getToM() {
        return ToM;
    }

    public void setToM(String toM) {
        ToM = toM;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeToSend() {
        return timeToSend;
    }

    public void setTimeToSend(String timeToSend) {
        this.timeToSend = timeToSend;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

}
