package com.example.ichat;

import com.example.ichat.Models.UserModel;

public interface RecyclerViewListeners {
    void contactOnClick(String name, String phoneNumber, UserModel user);
    void invite();
}

