package com.example.ichat.Models;

public class ContactsModel {
    String name;
    String phoneNumber;
    UserModel user;

    public ContactsModel(){}

    public ContactsModel(String name, String phoneNumber, UserModel  user) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
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

}
