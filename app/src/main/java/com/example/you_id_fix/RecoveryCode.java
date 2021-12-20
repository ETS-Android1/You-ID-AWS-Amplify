package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;

public class RecoveryCode extends AppCompatActivity {

    EditText codeInput, newPasswordInput, confirmNewPasswordInput;
    String code, newPassword, confirmNewPassword;
    Button callResetPassword;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_code);
        codeInput = findViewById(R.id.recoveryCodeInput);
        newPasswordInput = findViewById(R.id.newPassword);
        confirmNewPasswordInput = findViewById(R.id.confirmNewPassword);
        backArrow = findViewById(R.id.backArrow);

        callResetPassword = findViewById(R.id.btn_changePassword);

        callResetPassword.setOnClickListener(this::callConfirmPass);

        backArrow.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }

    public void callConfirmPass(View view) {
        code = codeInput.getText().toString();
        newPassword = newPasswordInput.getText().toString();
        confirmNewPassword = confirmNewPasswordInput.getText().toString();


        if (TextUtils.isEmpty(code))
        {
            codeInput.setError("Code Required.");
            return;
        }
        if (TextUtils.isEmpty(newPassword))
        {
            newPasswordInput.setError("New Password Required.");
            return;
        }
        if (TextUtils.isEmpty(confirmNewPassword))
        {
            confirmNewPasswordInput.setError("Confirm New Password Required.");
            return;
        }
        if (newPassword.length() >= 16 || newPassword.length() <= 7)
        {
            newPasswordInput.setError("Password length must be between 7 and 16 characters.");
            return;
        }
        if (!newPassword.equals(confirmNewPassword))
        {
            newPasswordInput.setError("Passwords do not match.");
            confirmNewPasswordInput.setError("Passwords do not match.");
            return;
        }

        Amplify.Auth.confirmResetPassword(
                newPassword,
                code,
                () ->  {
                    Log.i("AuthQuickstart", "New password confirmed");
                    Toast.makeText(this,"Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    },
                error -> {
                    Log.e("AuthQuickstart", error.toString());
                    codeInput.setError("Code Incorrect.");
                }
        );
    }
}
