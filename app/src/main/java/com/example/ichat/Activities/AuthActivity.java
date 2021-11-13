package com.example.ichat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ichat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextMobile;
    private EditText editCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        editTextMobile = findViewById(R.id.mobile);
        editCode = findViewById(R.id.code);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.nextButton).setOnClickListener(view -> {
            String mobile = editTextMobile.getText().toString().trim();
            String code = editCode.getText().toString().trim();

            if(mobile.isEmpty() || mobile.length() < 10 || code.isEmpty()){
                editTextMobile.setError("Enter valid mobile number");
                editTextMobile.requestFocus();
                return;
            }

            // Move to verify activity for OTP verification
            mobile = code + mobile;
            Intent intent = new Intent(AuthActivity.this,VerifyActivity.class );
            intent.putExtra("mobile", mobile);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}