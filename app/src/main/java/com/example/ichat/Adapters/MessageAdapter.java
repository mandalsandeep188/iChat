package com.example.ichat.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichat.Models.MessageModel;
import com.example.ichat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter {
    List<MessageModel> messages;
    FirebaseAuth firebaseAuth;

    public MessageAdapter(List<MessageModel> messages) {
        this.messages = messages;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getSender().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType == 0){
            view = inflater.inflate(R.layout.sent_message,parent,false);
            return new ViewHolderSent(view);
        }
        else {
            view = inflater.inflate(R.layout.recieved_message,parent,false);
            return new ViewHolderReceived(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message = messages.get(position);
        if(message.getSender().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())){
            ViewHolderSent viewHolderSent = (ViewHolderSent) holder;
            viewHolderSent.msg.setText(message.getMsg());
        }
        else{
            ViewHolderReceived viewHolderReceived = (ViewHolderReceived) holder;
            viewHolderReceived.msg.setText(message.getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolderSent extends RecyclerView.ViewHolder{
        TextView msg;
        public ViewHolderSent(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.sent);
        }
    }

    static class ViewHolderReceived extends RecyclerView.ViewHolder{
        TextView msg;
        public ViewHolderReceived(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.recieved);
        }
    }
}

