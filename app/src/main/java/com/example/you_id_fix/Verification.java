package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.UserInfo;

import java.io.Serializable;

public class Verification extends AppCompatActivity implements Serializable {
    String newEmail, fullName, email, profilePic;
    EditText verification;
    Button verify, resend;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        verification = findViewById(R.id.editTextVerificationCode);
        verify = findViewById(R.id.buttonVerify);
        resend = findViewById(R.id.buttonResend);
        backArrow = findViewById(R.id.backArrow);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            newEmail = extras.getString("newEmail");
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        verify.setOnClickListener(v -> {
            String code = verification.getText().toString();
            Amplify.Auth.confirmUserAttribute(AuthUserAttributeKey.email(), code,
                    () -> {Log.i("Email Confirmation", "Confirmed user attribute with correct code.");
                        Amplify.DataStore.query(UserInfo.class, Where.matches(UserInfo.EMAIL.eq(Amplify.Auth.getCurrentUser().getUsername())),
                                matches -> {
                                    if (matches.hasNext()) {
                                        UserInfo original = matches.next();
                                        UserInfo edited = original.copyOfBuilder()
                                                .email(newEmail)
                                                .build();
                                        Amplify.DataStore.save(edited,
                                                updated -> Log.i("MyAmplifyApp", "Updated a user's email."),
                                                failure -> Log.e("MyAmplifyApp", "Update a user;s email failed.", failure)
                                        );
                                    }
                                },
                                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
                        );
                        Intent intent = new Intent(getApplicationContext(), UserSettings.class);
                        intent.putExtra("thisName", fullName);
                        intent.putExtra("thisEmail", newEmail);
                        intent.putExtra("thisProfilePic", profilePic);
                        startActivity(intent);
                        runOnUiThread(() -> Toast.makeText(Verification.this, "Verified New Email Successfully.", Toast.LENGTH_SHORT).show());
                        },
                    error -> {Log.e("Email Confirmation", "Failed to confirm user attribute. Bad code?", error);
                        runOnUiThread(() -> verification.setError("Incorrect Verification Code."));
            }
            );
        });

        resend.setOnClickListener(v -> Amplify.Auth.resendUserAttributeConfirmationCode(AuthUserAttributeKey.email(),
                result -> {Log.i("AuthDemo", "Code was sent again: " + result.toString());
                    runOnUiThread(() -> Toast.makeText(Verification.this, "Resent Code.", Toast.LENGTH_SHORT).show());
                    },
                error -> Log.e("AuthDemo", "Failed to resend code.", error)
        ));

       backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserSettings.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });
    }
}
