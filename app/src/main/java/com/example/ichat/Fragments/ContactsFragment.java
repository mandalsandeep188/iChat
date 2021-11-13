package com.example.ichat.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichat.Adapters.ContactSectionsAdapter;
import com.example.ichat.Models.ContactsModel;
import com.example.ichat.Models.SectionModel;
import com.example.ichat.Models.UserModel;
import com.example.ichat.R;
import com.example.ichat.RecyclerViewListeners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContactsFragment extends Fragment {
    private static final String TAG = "Contacts";
    List<ContactsModel> contacts1,contacts2;
    RecyclerView contactRecyclerView;
    RecyclerViewListeners recyclerViewListeners;
    FirebaseFirestore db;
    ArrayList<UserModel> users;
    FirebaseAuth firebaseAuth;
    List<SectionModel> sections;
    ProgressBar progressBar;

    public ContactsFragment(RecyclerViewListeners listeners){
        recyclerViewListeners = listeners;
    }

    public ContactsFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_contacts,container,false);
        contactRecyclerView = view.findViewById(R.id.contactList);
        progressBar = view.findViewById(R.id.progressBar);

        users = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //Get user permission for reading contacts in manifest file
        // Marshmallow and bigger versions
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M
                && requireContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            // request for permission
            mPermissionResult.launch(Manifest.permission.READ_CONTACTS);
        }
        else{
            retrieveUsers();
        }
        return view;
    }

    private void retrieveUsers() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            users.add(document.toObject(UserModel.class));
                        }
                        extractContacts();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    // This runs when asked permission get response
    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if(result) {
                    Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                    retrieveUsers();
                    extractContacts();
                } else {
                    Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                }
            });

    private void extractContacts() {
        contacts1 = new ArrayList<>();
        contacts2 = new ArrayList<>();
        sections = new ArrayList<>();

        Map<String,Boolean> map = new HashMap<>();
        Cursor cursor = requireContext().getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        assert cursor != null;
        while(cursor.moveToNext()){
            String name = cursor.
                    getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.
                    getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phoneNumber = phoneNumber.replaceAll("\\s+","");
            phoneNumber = phoneNumber.replaceAll("-","");

            if(phoneNumber.length() >= 10  && !map.containsKey(phoneNumber)
                    && !map.containsKey("+91"+phoneNumber)){
                map.put(phoneNumber,true);
                UserModel user = null;

                if(phoneNumber.charAt(0) != '+'){
                    phoneNumber = "+91"+phoneNumber;
                    map.put(phoneNumber,true);
                }

                for(UserModel u: users){
                    if(u.getPhoneNumber().equals(phoneNumber)){
                        user = u;
                        break;
                    }
                }

                if(!phoneNumber.
                        equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getPhoneNumber())) {
                    if (user != null) {
                        contacts1.add(new ContactsModel(name,phoneNumber,user));
                    }
                    else{
                        contacts2.add(new ContactsModel(name,phoneNumber,null));
                    }
                }
            }
        }
        cursor.close();

        if(!contacts1.isEmpty()) sections.add(new SectionModel("Users",contacts1));
        if(!contacts2.isEmpty()) sections.add(new SectionModel("Contacts",contacts2));

        ContactSectionsAdapter contactSectionsAdapter =
                new ContactSectionsAdapter(sections,recyclerViewListeners);
        contactRecyclerView.setAdapter(contactSectionsAdapter);
        progressBar.setVisibility(View.GONE);
    }
}
