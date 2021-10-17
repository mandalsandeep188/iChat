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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    List<ChatsModel> chats;
    RecyclerViewListeners listeners;

    public ChatsAdapter(List<ChatsModel> chats, RecyclerViewListeners listeners) {
        this.chats = chats;
        this.listeners = listeners;
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
        holder.name.setText(chat.getUser().getName());
        holder.lastMessage.setText(chat.getLastMessage());
        holder.user = chat.getUser();
        Glide.with(holder.itemView)
                .load(chat.getUser().getPhotoUrl())
                .centerCrop()
                .into(holder.photo);
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
