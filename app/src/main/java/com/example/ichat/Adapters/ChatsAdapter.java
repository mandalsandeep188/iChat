package com.example.ichat.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ichat.Models.ChatsModel;
import com.example.ichat.Models.UserModel;
import com.example.ichat.R;
import com.example.ichat.RecyclerViewListeners;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    List<ChatsModel> chats;
    RecyclerViewListeners listeners;
    FirebaseFirestore db;

    public ChatsAdapter(List<ChatsModel> chats, RecyclerViewListeners listeners) {
        this.chats = chats;
        this.listeners = listeners;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chats_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatsModel chat = chats.get(position);
        db.collection("users").document(chat.getReceiver())
                .addSnapshotListener((value, error) -> {
                    if(error == null){
                        assert value != null;
                        UserModel user = value.toObject(UserModel.class);
                        assert user != null;
                        holder.name.setText(user.getName());
                        holder.lastMessage.setText(chat.getLastMessage());
                        holder.user = user;
                        if(!user.getPhotoUrl().isEmpty()) {
                            try {
                                Glide.with(holder.itemView)
                                        .load(user.getPhotoUrl())
                                        .centerCrop()
                                        .into(holder.photo);
                            }catch (Exception ignored){
                            }
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,lastMessage;
        CircularImageView photo;
        UserModel user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            photo = itemView.findViewById(R.id.photo);

            itemView.setOnClickListener(view ->
                    listeners.contactOnClick((String) name.getText(),(String) lastMessage.getText(),user));
        }
    }
}
