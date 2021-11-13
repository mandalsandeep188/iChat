package com.example.ichat.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ichat.Models.UserModel;
import com.example.ichat.R;
import com.example.ichat.RecyclerViewListeners;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    private static final String TAG = "Profile";
    CircularImageView profilePhoto;
    FirebaseFirestore db;
    private StorageReference mStorageRef;
    FirebaseAuth firebaseAuth;
    TextView userName,status,phoneNumber;
    FloatingActionButton editPhoto,editName,editStatus;
    RecyclerViewListeners listeners;
    ProgressDialog progressDialog;
    Activity activity;

    public ProfileFragment(RecyclerViewListeners listeners) {
        this.listeners = listeners;
        activity = (Activity) listeners;
    }

    public ProfileFragment(){
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePhoto = view.findViewById(R.id.profilePhoto);
        userName = view.findViewById(R.id.userName);
        status = view.findViewById(R.id.status);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        phoneNumber.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getPhoneNumber());
        editStatus = view.findViewById(R.id.editStatus);
        editName = view.findViewById(R.id.editName);
        editPhoto = view.findViewById(R.id.addPhoto);
        editPhoto.setOnClickListener(view12 -> changePhoto());
        editName.setOnClickListener(view13 -> changeName());
        editStatus.setOnClickListener(view1 -> changeStatus());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Changing your profile image");
        setUpProfile();
        return view;
    }

    private void changeStatus() {
        getInput("status");
    }

    private void changeName() {
        getInput("name");
    }

    private void getInput(String field) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Update "+field);
            final EditText input = new EditText(activity);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Update", (dialog, which) -> {
                String newValue = input.getText().toString();
                db.collection("users")
                        .document(Objects.requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()))
                        .update(field, newValue);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        }
        catch (Exception ignored){
        }
    }

    private void setUpProfile() {
        Log.d(TAG, "setUpProfile: "+"Profile");
        db.collection("users")
                .document(Objects.
                        requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()))
                .addSnapshotListener((value, error) -> {
                    if(error == null) {
                        assert value != null;
                        UserModel user = value.toObject(UserModel.class);
                        assert user != null;
                        userName.setText(user.getName());
                        status.setText(user.getStatus());
                        String uri = user.getPhotoUrl();
                        try {
                            if (!uri.isEmpty()) {
                                Glide.with((Activity) listeners)
                                        .load(uri)
                                        .centerCrop()
                                        .into(profilePhoto);
                            }
                        }catch (Exception ignored){
                        }
                    }
                });
    }

    private void changePhoto() {
        try {
            Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
            gallery.setType("image/*");
            launchSomeActivity.launch(gallery);
        }catch (Exception ignored){
        }
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                try {
                    progressDialog.show();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageURI = data.getData();
                            profilePhoto.setImageURI(imageURI);

                            final StorageReference profileRef = mStorageRef.
                                    child("users/" + Objects.requireNonNull(firebaseAuth.getCurrentUser())
                                            .getUid() + ".jpg");

                            assert imageURI != null;

                            profileRef.putFile(imageURI)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(@NotNull UploadTask.TaskSnapshot taskSnapshot) {
                                            profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                                Log.d(TAG, "onComplete: " + uri);

                                                db.collection("users")
                                                        .document(Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid()))
                                                        .update("photoUrl", uri.toString())
                                                        .addOnCompleteListener(task -> progressDialog.dismiss());
                                            });
                                        }
                                    });
                        }
                    }
                }catch(Exception ignored){
                }
            });
}