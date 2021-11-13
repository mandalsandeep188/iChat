package com.example.ichat.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichat.Adapters.ChatsAdapter;
import com.example.ichat.Models.ChatsModel;
import com.example.ichat.R;
import com.example.ichat.RecyclerViewListeners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    RecyclerView chatsList;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    RecyclerViewListeners listeners;
    List<ChatsModel> chats;
    TextView textChat1,textChat2;
    ProgressBar progressBar;
    String uId;

    public ChatsFragment(RecyclerViewListeners listeners) {
        this.listeners = listeners;
    }

    public ChatsFragment(){
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chats,container,false);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        chatsList = view.findViewById(R.id.chatsList);
        textChat1 = view.findViewById(R.id.textChat1);
        textChat2 = view.findViewById(R.id.textChat2);
        progressBar = view.findViewById(R.id.progressBarChats);
        chats = new ArrayList<>();
        uId = firebaseAuth.getUid();
        setChatsListeners();
        return view;
    }

    private void setChatsListeners() {
        db.collection("chats")
                .whereArrayContains("users",uId)
                .orderBy("lastUpdate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.d("TAG", "setMessageListener: "+error.getMessage());
                return;
            }
            chats.clear();
            assert value != null;
            for (QueryDocumentSnapshot doc : value) {
                ChatsModel chat = doc.toObject(ChatsModel.class);
                int i = chat.getUsers().indexOf(uId);
                i = (i+1) % 2;
                chat.setReceiver(chat.getUsers().get(i));
                chats.add(chat);
            }
            ChatsAdapter chatsAdapter = new ChatsAdapter(chats, listeners);
            chatsList.setAdapter(chatsAdapter);
            if(!chats.isEmpty()){
                textChat1.setVisibility(View.GONE);
                textChat2.setVisibility(View.GONE);
            }
            else{
                textChat1.setVisibility(View.VISIBLE);
                textChat2.setVisibility(View.VISIBLE);
            }
        });
    }
}
