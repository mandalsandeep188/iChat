package com.example.ichat.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ichat.Fragments.ChatsFragment;
import com.example.ichat.Fragments.ContactsFragment;
import com.example.ichat.Fragments.ProfileFragment;
import com.example.ichat.RecyclerViewListeners;

public class PagerAdapter extends FragmentPagerAdapter {

    RecyclerViewListeners listeners;

    public PagerAdapter(@NonNull FragmentManager fm, RecyclerViewListeners listeners) {
        super(fm);
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ChatsFragment(listeners);
            case 1: return new ContactsFragment(listeners);
            case 2: return new ProfileFragment(listeners);
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
