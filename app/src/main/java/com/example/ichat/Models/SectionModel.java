package com.example.ichat.Models;

import java.util.List;

public class SectionModel {
    String title;
    List<ContactsModel> contacts;

    public SectionModel(String title, List<ContactsModel> contacts) {
        this.title = title;
        this.contacts = contacts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ContactsModel> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsModel> contacts) {
        this.contacts = contacts;
    }
}