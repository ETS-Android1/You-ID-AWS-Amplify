package com.example.you_id_fix;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

import java.util.concurrent.Executor;

public class FingerprintLogin extends AppCompatActivity {

    private static final int REQUEST_CODE =10100000 ;
    ImageView fingerprintView, backArrow;
    SharedPreferences sharedPreferences;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_login);

        fingerprintView = findViewById(R.id.fingerPrintImageView);
        backArrow = findViewById(R.id.backArrow);

        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);

        backArrow.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("FingerprintLogin", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("FingerprintLogin", "No biometric features available on this device.");
                Toast.makeText(this, "Fingerprint sensor not available.", Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("FingerprintLogin", "Biometric features are currently unavailable.");
                Toast.makeText(this, "Fingerprint sensor is busy or not available.", Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                break;
        }


        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(FingerprintLogin.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d("FingerprintLogin", "Authentication Error");
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Log.d("FingerprintLogin", "Authentication Successful");
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                String email = sharedPreferences.getString("email","");
                String password = sharedPreferences.getString("password","");

                Amplify.Auth.signIn(
                        email,
                        password,
                        task -> Log.i("LoginPage", task.isSignInComplete() ? gotoMainActivity() : "Sign in not complete"),
                        error -> Log.e("LoginPage", error.toString())
                );
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d("FingerprintScanner", "Authentication Failed");
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("FingerPrint login")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        // Button biometricLoginButton = findViewById(R.id.biometric_login);
        //biometricLoginButton.setOnClickListener(view -> {

        fingerprintView.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));
    }


    public String gotoMainActivity()
    {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
        return "Sign in  complete";
    }

}