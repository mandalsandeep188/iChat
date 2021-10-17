package com.example.ichat.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichat.R;
import com.example.ichat.Models.SectionModel;
import com.example.ichat.RecyclerViewListeners;

import java.util.List;

public class ContactSectionsAdapter extends RecyclerView.Adapter<ContactSectionsAdapter.ViewHolder>  {

    List<SectionModel> sections;
    RecyclerViewListeners listeners;

    public ContactSectionsAdapter(List<SectionModel> sections, RecyclerViewListeners listeners) {
        this.sections = sections;
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.section_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(sections.get(position).getTitle());
        ContactAdapter contactAdapter = new ContactAdapter(sections.get(position).getContacts(),listeners);
        holder.recyclerView.setAdapter(contactAdapter);
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.sectionTitle);
            recyclerView = itemView.findViewById(R.id.list);
        }
    }
}
