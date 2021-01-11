package com.example.i4u_.ChatAPI;

import java.security.Timestamp;

public class Message {
    private String messageText;
    private String receiverID;
    private String senderID;
    private Long time;

    public Message() {
    }

    public Message(String messageText, Long time) {
        this.messageText = messageText;
        this.time = time;
    }

    public Message(String messageText, String receiverID, String senderID, Long time) {
        this.messageText = messageText;
        this.receiverID = receiverID;
        this.senderID = senderID;
        this.time = time;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
