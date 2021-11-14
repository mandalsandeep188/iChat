package com.example.ichat.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ichat.Models.UserModel;
import com.example.ichat.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;

public class UserActivity extends AppCompatActivity {
    EditText name,status;
    FloatingActionButton addPhoto;
    Button submit;
    CircularImageView profilePhoto;
    FirebaseFirestore db;
    private StorageReference mStorageRef;
    FirebaseAuth firebaseAuth;
    String photoUri = "";
    ProgressDialog progressDialog,photo;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        submit = findViewById(R.id.submit);
        addPhoto = findViewById(R.id.addPhoto);
        profilePhoto = findViewById(R.id.profilePhoto);
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        photo = new ProgressDialog(this);
        progressDialog.setMessage("Creating your account");
        progressDialog.setCancelable(false);
        photo.setMessage("Getting your image");

        addPhoto.setOnClickListener(view -> addProfilePhoto());
        submit.setOnClickListener(view -> submitCredentials());
    }

    public void addProfilePhoto() {
        try {
            Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
            gallery.setType("image/*");
            launchSomeActivity.launch(gallery);
        }catch(Exception ignored){
        }
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                try {
                    photo.show();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageURI = data.getData();
                            profilePhoto.setImageURI(imageURI);

                            final StorageReference profileRef = mStorageRef.child("users/" +
                                    Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid() + ".jpg");

                            assert imageURI != null;

                            profileRef.putFile(imageURI)
                                    .addOnSuccessListener(taskSnapshot -> profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        photoUri = uri.toString();
                                        photo.dismiss();
                                    }));
                        }
                    }
                }catch (Exception ignored) {
                }
            });

    private void submitCredentials() {
        progressDialog.show();
        String Name = name.getText().toString().trim();
        String Status = status.getText().toString().trim();
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        String phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        UserModel user = new UserModel(photoUri,Name,phoneNumber,Status,userId);
        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    Intent intent = new Intent(UserActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("users").whereEqualTo("phoneNumber",phoneNumber)
                .addSnapshotListener((value, error) -> {
                    if(error == null) {
                        assert value != null;
                        UserModel user = value.getDocuments().get(0).toObject(UserModel.class);

                        assert user != null;
                        name.setText(user.getName());
                        status.setText(user.getStatus());
                        String uri = user.getPhotoUrl();
                        if (!uri.isEmpty()) {
                            photoUri = uri;
                            try {
                                Glide.with(this)
                                        .load(uri)
                                        .centerCrop()
                                        .into(profilePhoto);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                });
    }
}
