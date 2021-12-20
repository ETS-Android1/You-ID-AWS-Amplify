package com.example.you_id_fix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.core.Amplify;

import java.io.Serializable;

public class ChangeEmail extends AppCompatActivity implements Serializable {
    String userEmail;
    TextView currentEmail;
    EditText newEmail;
    Button sendVerification;
    TextView haveCode;
    String fullName;
    String email;
    String profilePic;
    ImageView backArrow;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        currentEmail = findViewById(R.id.textCurrentEmail);
        newEmail = findViewById(R.id.editTextNewEmail);
        sendVerification = findViewById(R.id.buttonSendVerificationCode);
        haveCode = findViewById(R.id.alreadyHaveVerification);
        backArrow = findViewById(R.id.backArrow);

        userEmail = Amplify.Auth.getCurrentUser().getUsername();
        currentEmail.setText(userEmail);

        sendVerification.setOnClickListener(v -> {
            String nEmail = newEmail.getText().toString();

            if (TextUtils.isEmpty(nEmail)) {
                newEmail.setError("Email Required.");
                return;
            }
            if ( ((!nEmail.contains("@")) && !(nEmail.contains(".com"))) || (!(nEmail.contains("@")) && !(nEmail.contains(".edu"))))
            {
                newEmail.setError("Invalid Email!");
                return;
            }

            AuthUserAttribute newUserEmail =
                    new AuthUserAttribute(AuthUserAttributeKey.email(), nEmail);
            Amplify.Auth.updateUserAttribute(newUserEmail,
                    result -> {Log.i("Update Email", "Updated user attribute = " + result.toString());
                        Intent intent = new Intent(getApplicationContext(), Verification.class);
                        intent.putExtra("thisName", fullName);
                        intent.putExtra("thisEmail", email);
                        intent.putExtra("thisProfilePic", profilePic);
                        intent.putExtra("newEmail", nEmail);
                        startActivity(intent);
                        },
                    error -> Log.e("Update Email", "Failed to update user attribute.", error)
            );
        });

        haveCode.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Verification.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserSettings.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });
    }
}