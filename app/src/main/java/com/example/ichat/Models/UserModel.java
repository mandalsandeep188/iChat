package com.example.ichat.Models;

public class UserModel {
    String photoUrl;
    String name;
    String phoneNumber;
    String status;
    String userId;

    public UserModel() {
    }

    public UserModel(String photoUrl, String name, String phoneNumber, String status, String userId) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
