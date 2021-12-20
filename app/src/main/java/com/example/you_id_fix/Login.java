package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageListOptions;

import java.io.Serializable;

public class Login extends AppCompatActivity implements Serializable {
    EditText inputEmail, inputPassword;
    TextView forgotPass, SignUp, HelpPage;
    String inputUserEmail, inputUserPassword;
    Button Login;
    ImageView fingerPrintImageV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fingerPrintImageV = findViewById(R.id.fingerprintImageView);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        Login = findViewById(R.id.btn_signin);
        forgotPass =  findViewById(R.id.textForgotPassword);
        SignUp =  findViewById(R.id.textSignUp2);
        HelpPage = findViewById(R.id.textClickableLearnMore);

        Login.setOnClickListener(v -> {
                // Getting user input as strings
                inputUserEmail = inputEmail.getText().toString();
                inputUserPassword = inputPassword.getText().toString();

                // Validating User Input
                if (!(inputUserEmail.contains("@")) && (!(inputUserEmail.contains(".com")) || !(inputUserEmail.contains(".edu")))) {
                    inputEmail.setError("Invalid Email!");
                    return;
                }
                if (TextUtils.isEmpty(inputUserPassword)) {
                    inputPassword.setError("Password Required.");
                    return;
                }

                Amplify.Auth.signIn(
                        inputUserEmail,
                        inputUserPassword,
                        result -> {
                            Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                            if (result.isSignInComplete()) {
                                StorageListOptions options = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                                        .build();
                                Amplify.Storage.list(
                                        "",
                                        options,
                                        result2 -> {
                                            Log.i("Getting List of Images", "From s3 Bucket:");
                                            for (StorageItem item : result2.getItems()) {
                                                Log.i("List Files Function", "Image Key = Item: " + item.getKey());

                                                if (("merchant\\identification").equals(item.getKey()))
                                                {
                                                    startActivity(new Intent(getApplicationContext(), MainMenu.class));
                                                }
                                            }
                                        }, error -> Log.e("Storage Item", "Item failure", error) );

                                startActivity(new Intent(getApplicationContext(), UploadIdentification.class));
                            } else {
                                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            Log.e("AuthQuickstart", error.toString());
                            runOnUiThread(() -> Toast.makeText(Login.this, "Incorrect Sign-In Credentials", Toast.LENGTH_SHORT).show());
                        }
                );
        });

        fingerPrintImageV.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FingerprintLogin.class)));

        forgotPass.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ForgotPassword.class)));

        SignUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), com.example.you_id_fix.SignUp.class)));

        HelpPage.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SupportLogin.class)));
    }
}