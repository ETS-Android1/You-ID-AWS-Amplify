package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;

public class ForgotPassword extends AppCompatActivity {

    EditText inputRecoveryEmail;
    String username;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputRecoveryEmail = findViewById(R.id.recoveryEmailInput);
        backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }

    public void getRecoveryCode(View view) {
        username = inputRecoveryEmail.getText().toString(); //email as username

        Log.i("UserName Entered is", username);

        Amplify.Auth.resetPassword(
                username,
                result -> Log.i("AuthQuickstart", result.toString()),
                error -> Log.e("AuthQuickstart", error.toString())
        );

        Intent intent = new Intent(this, RecoveryCode.class);
        startActivity(intent);
    }
}
