package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    EditText firstname, lastname, ageX, pass, email, phone, confirmPass;
    Button signup;
    String firstName, lastName, e_mail, password, age, phoneNumber, confirmPassword;
    ProgressBar progressbarSignup;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressbarSignup = findViewById(R.id.progressBarSignUp);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        ageX = findViewById(R.id.age);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_Password);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phoneNumber);
        signup = findViewById(R.id.btn_signup);
        backArrow = findViewById(R.id.backArrow);

        signup.setOnClickListener((e) -> {

            // getting user input into string and int objects
             firstName = firstname.getText().toString();
             lastName = lastname.getText().toString();
             e_mail = email.getText().toString();
             phoneNumber = phone.getText().toString();
             password = pass.getText().toString();
             confirmPassword = confirmPass.getText().toString();

             age = ageX.getText().toString();
             int age_num;

            // Validating user inputs
            if (TextUtils.isEmpty(age))
            {
                ageX.setError("Age Required.");
                return;
            } else {
                age_num =  Integer.parseInt(ageX.getText().toString());
                if (age_num < 5 || age_num > 80)
                {
                    ageX.setError("Age Not Valid");
                    return;
                }
            }
            if (TextUtils.isEmpty(firstName))
            {
                firstname.setError("First Name Required.");
                return;
            }
            if (firstName.length() < 3 || firstName.length() > 30)
            {
                firstname.setError("First Name not valid!");
                return;
            }
            if (TextUtils.isEmpty(lastName))
            {
                lastname.setError("Last Name Required.");
                return;
            }
            if (TextUtils.isEmpty(e_mail))
            {
                email.setError("Email Required.");
                return;
            }
            if ( ((!e_mail.contains("@")) && !(e_mail.contains(".com"))) || (!(e_mail.contains("@")) && !(e_mail.contains(".edu"))))
            {
                email.setError("Invalid Email!");
                return;
            }
            if (TextUtils.isEmpty(password))
            {
                pass.setError("Password Required.");
                return;
            }
            if (password.length() > 15 || password.length() < 8)
            {
                pass.setError("Password Required.");
                return;
            }
            if (!(password.equals(confirmPassword)))
            {
                pass.setError("Passwords do not match.");
                confirmPass.setError("Passwords do not match.");
                return;
            }
            // Creating new amplify user
            saveData();

        });

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });

    }


    public void saveData() {

        ArrayList<AuthUserAttribute> attributes = new ArrayList<>();
        attributes.add(new AuthUserAttribute(AuthUserAttributeKey.email(), e_mail));

        Amplify.Auth.signUp(
                e_mail,
                password,
                AuthSignUpOptions.builder().userAttributes(attributes).build(),
                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
        );

        progressbarSignup.setVisibility(View.VISIBLE);

        Toast.makeText(this,"Account Created!", Toast.LENGTH_SHORT).show();

        // Moving to Login Activity
       Intent intent = new Intent(this, ConfirmSignUp.class);
        intent.putExtra("userFirstName", "" + firstName);
        intent.putExtra("userPhone", "" + phoneNumber);
        intent.putExtra("userLastName", "" +lastName);
        intent.putExtra("userAge",       "" +  age);
        intent.putExtra("userEmail",    "" + e_mail);
        intent.putExtra("userPassword",    "" + password);
       startActivity(intent);
    }
}
