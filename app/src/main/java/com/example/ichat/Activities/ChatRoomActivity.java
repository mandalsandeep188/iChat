package com.example.ichat.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ichat.Adapters.MessageAdapter;
import com.example.ichat.Models.ChatsModel;
import com.example.ichat.Models.MessageModel;
import com.example.ichat.Models.UserModel;
import com.example.ichat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    String name, phoneNumber, photoUrl, status;
    String sender,receiver;
    Toolbar toolbar;
    ActionBar actionBar;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    String chatID;
    ImageView send;
    TextView displayName;
    CircularImageView photo;
    EditText messageBox;
    List<MessageModel> messages;
    RecyclerView messageList;
    boolean init = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        name = getIntent().getStringExtra("name");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        receiver = getIntent().getStringExtra("userId");
        photoUrl = getIntent().getStringExtra("photoUrl");
        status = getIntent().getStringExtra("status");

        toolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        messages = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sender = firebaseAuth.getUid();
        String[] talkers = {sender, receiver};
        Arrays.sort(talkers);
        chatID = talkers[0] + ":" + talkers[1];

        send = findViewById(R.id.send);
        messageBox = findViewById(R.id.messageBox);
        messageList = findViewById(R.id.messageList);
        photo = findViewById(R.id.photo);
        displayName = findViewById(R.id.name);

        displayName.setText(name);
        if (photoUrl.length() > 0) {
            Glide.with(this)
                    .load(photoUrl)
                    .centerCrop()
                    .into(photo);
        }

        setMessageListener();

        send.setOnClickListener(view -> {
            String msg = messageBox.getText().toString().trim();
            if(msg.length() > 0)
                sendMessage(msg);
        });

    }

    private void setMessageListener() {
        db.collection("chats").document(chatID)
                .collection("messages")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.d("TAG", "setMessageListener: "+e.getMessage());
                        return;
                    }

                    assert value != null;
                    messages.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        messages.add(doc.toObject(MessageModel.class));
                    }

                    MessageAdapter messageAdapter = new MessageAdapter(messages);
                    messageList.setAdapter(messageAdapter);
                    messageList.scrollToPosition(messages.size()-1);

                    if(!init) {
                        // last Message update
                        db.collection("chats").document(chatID)
                                .update("lastMessage", messages.get(messages.size() - 1).getMsg(),
                                        "lastUpdate", new Date().getTime());
                    }
                    init = false;
                });
    }

    private void sendMessage(String msg) {
        messageBox.setText("");

        // first message in this chat (check if messages list will work)
        if(messages.isEmpty()){
            UserModel user = new UserModel(photoUrl,name,phoneNumber,status,receiver);
            ChatsModel chat = new ChatsModel(chatID,msg,user,new Date().getTime());
            db.collection("chats").document(chatID).set(chat)
                    .addOnCompleteListener(t ->
                            Toast.makeText(ChatRoomActivity.this,
                                    "New Chat", Toast.LENGTH_SHORT).show());
        }

        MessageModel message = new MessageModel(msg,sender,chatID,new Date().getTime());
        db.collection("chats").document(chatID).
                collection("messages").add(message);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}