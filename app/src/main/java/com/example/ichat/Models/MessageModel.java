package com.example.ichat.Models;

public class MessageModel {
    String msg;
    String chatId;
    String sender;
    Long time;

    public MessageModel() {
    }

    public MessageModel(String msg, String sender, String chatId, Long time) {
        this.msg = msg;
        this.chatId = chatId;
        this.time = time;
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
