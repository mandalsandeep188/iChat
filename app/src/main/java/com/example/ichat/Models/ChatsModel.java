package com.example.ichat.Models;

import java.util.List;

public class ChatsModel {
    String chatId;
    String lastMessage;
    String receiver;
    List<String> users;
    long lastUpdate;

    public ChatsModel() {
    }

    public ChatsModel(String chatId, String lastMessage, List<String> users, long lastUpdate) {
        this.chatId = chatId;
        this.lastMessage = lastMessage;
        this.users = users;
        this.lastUpdate = lastUpdate;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
