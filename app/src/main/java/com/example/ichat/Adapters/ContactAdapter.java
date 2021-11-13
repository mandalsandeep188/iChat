package com.example.ichat.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ichat.Models.ContactsModel;
import com.example.ichat.Models.UserModel;
import com.example.ichat.R;
import com.example.ichat.RecyclerViewListeners;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter {

    List<ContactsModel> contacts;
    RecyclerViewListeners listeners;

    public ContactAdapter(List<ContactsModel> contacts, RecyclerViewListeners listeners) {
        this.contacts = contacts;
        this.listeners = listeners;
    }

    @Override
    public int getItemViewType(int position) {
        ContactsModel contact = contacts.get(position);
        if(contact.getUser() == null)
            return 0;
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType == 0){
            view = inflater.inflate(R.layout.contact_list_item1,parent,false);
            return new ViewHolder1(view);
        }
        else{
            view = inflater.inflate(R.layout.contact_list_item,parent,false);
            return new ViewHolder2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactsModel contact = contacts.get(position);
        if(contact.getUser() == null) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.name.setText(contact.getName());
            viewHolder1.phoneNumber.setText(contact.getPhoneNumber());
        }
        else{
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            viewHolder2.name.setText(contact.getName());
            viewHolder2.status.setText(contact.getUser().getStatus());
            viewHolder2.user = contact.getUser();
            if(!contact.getUser().getPhotoUrl().isEmpty()) {
                try {
                    Glide.with(viewHolder2.itemView)
                            .load(contact.getUser().getPhotoUrl())
                            .centerCrop()
                            .into(viewHolder2.photo);
                }catch (Exception ignored){
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder{
        TextView name,phoneNumber;
        Button button;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            button = itemView.findViewById(R.id.invite);

            button.setOnClickListener(view -> listeners.invite(phoneNumber.toString()));
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder{
        TextView name,status;
        CircularImageView photo;
        UserModel user;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            photo = itemView.findViewById(R.id.photo);

            itemView.setOnClickListener(view -> listeners.contactOnClick((String) name.
                    getText(),user.getPhoneNumber(),user));
        }
    }
}
