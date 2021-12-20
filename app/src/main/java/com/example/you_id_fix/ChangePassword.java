package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;

import java.io.Serializable;

public class ChangePassword extends AppCompatActivity implements Serializable {
    String fullName;
    String email;
    String profilePic;
    Button changePass;
    EditText oldPass;
    EditText newPass;
    EditText confirmNewPass;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        changePass = findViewById(R.id.buttonChangePassword);
        oldPass = findViewById(R.id.editTextOldPassword);
        newPass = findViewById(R.id.editTextNewPassword);
        confirmNewPass = findViewById(R.id.editTextConfirmNewPassword);
        backArrow = findViewById(R.id.backArrow);

        changePass.setOnClickListener(v -> {
            String oldPassword = oldPass.getText().toString();
            String newPassword = newPass.getText().toString();
            String confirmNewPassword = confirmNewPass.getText().toString();

            if (TextUtils.isEmpty(oldPassword))
            {
                oldPass.setError("Current Password Required.");
                return;
            }
            if (TextUtils.isEmpty(newPassword))
            {
                newPass.setError("New Password Required.");
                return;
            }
            if (TextUtils.isEmpty(confirmNewPassword))
            {
                confirmNewPass.setError("Confirm New Password Required.");
                return;
            }
            if (newPassword.length() >= 16 || newPassword.length() <= 7)
            {
                newPass.setError("Password length must be between 7 and 16 characters.");
                return;
            }
            if (!newPassword.equals(confirmNewPassword))
            {
                newPass.setError("Passwords do not match.");
                confirmNewPass.setError("Passwords do not match.");
                return;
            }

            Amplify.Auth.updatePassword(
                    oldPassword,
                    newPassword,
                    () -> {
                        Log.i("Change Password", "Updated password successfully");
                        Intent intent = new Intent(getApplicationContext(), UserSettings.class);
                        intent.putExtra("thisName", fullName);
                        intent.putExtra("thisEmail", email);
                        intent.putExtra("thisProfilePic", profilePic);
                        startActivity(intent);
                        runOnUiThread(() -> Toast.makeText(ChangePassword.this, "Updated password successfully.", Toast.LENGTH_SHORT).show());
                        },
                    error -> {
                        Log.e("Change Password", error.toString());
                        runOnUiThread(() -> oldPass.setError("Current Password is incorrect or the current password is the same as the new password."));
                    }
            );
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