package com.example.ichat.Models;

public class ChatsModel {
    String chatId;
    String lastMessage;
    UserModel user;
    long lastUpdate;

    public ChatsModel() {
    }

    public ChatsModel(String chatId, String lastMessage, UserModel user, long lastUpdate) {
        this.chatId = chatId;
        this.lastMessage = lastMessage;
        this.user = user;
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

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
