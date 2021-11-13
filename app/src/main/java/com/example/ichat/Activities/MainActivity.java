package com.example.ichat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.ichat.Adapters.PagerAdapter;
import com.example.ichat.Models.UserModel;
import com.example.ichat.R;
import com.example.ichat.RecyclerViewListeners;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewListeners {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("iChat");

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Chats");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Contacts");
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText("Profile");
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    @Override
    public void contactOnClick(String name, String phoneNumber, UserModel user) {
        Intent chatRoom = new Intent(MainActivity.this,ChatRoomActivity.class);
        chatRoom.putExtra("name",name);
        chatRoom.putExtra("phoneNumber",phoneNumber);
        chatRoom.putExtra("userId",user.getUserId());
        chatRoom.putExtra("photoUrl",user.getPhotoUrl());
        chatRoom.putExtra("status",user.getStatus());
        startActivity(chatRoom);
    }

    @Override
    public void invite(String phoneNumber) {
        final String LINK = "https://github.com/mandalsandeep188/iChat#readme";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download iChat: "+LINK);
        sendIntent.putExtra(Intent.EXTRA_PHONE_NUMBER,phoneNumber);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}