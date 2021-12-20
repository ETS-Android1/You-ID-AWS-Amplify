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
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.UserInfo;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeUserInfo extends AppCompatActivity implements Serializable {
    String fullName;
    String email;
    String profilePic;
    Button updateUserInfo;
    EditText textFirstName;
    EditText textLastName;
    EditText textAge;
    EditText textPhone;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        textFirstName = findViewById(R.id.editTextCurrentFirstName);
        textLastName = findViewById(R.id.editTextCurrentLastName);
        textAge = findViewById(R.id.editTextCurrentAge);
        textPhone = findViewById(R.id.editTextCurrentPhone);
        updateUserInfo = findViewById(R.id.buttonUpdateUserInfo);
        backArrow = findViewById(R.id.backArrow);


        Amplify.DataStore.query(
                UserInfo.class,
                Where.matches(UserInfo.EMAIL.eq(Amplify.Auth.getCurrentUser().getUsername())),
                Users -> {
                    while (Users.hasNext()) {
                        UserInfo user = Users.next();
                        Log.i("MyAmplifyApp", "User: " +  user);
                        textFirstName.setText(user.getFirstname());
                        textLastName.setText(user.getLastname());
                        textAge.setText(user.getAge());
                        textPhone.setText(user.getPhone());
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
        );

        updateUserInfo.setOnClickListener(v -> {
            String fName = textFirstName.getText().toString();
            String lName = textLastName.getText().toString();
            String age = textAge.getText().toString();
            String phoneNum = textPhone.getText().toString();

            // Validating user inputs
            if (TextUtils.isEmpty(fName)) {
                textFirstName.setError("First Name Required.");
                return;
            }
            if (fName.length() < 3 || fName.length() > 30) {
                textFirstName.setError("First Name not valid!");
                return;
            }
            if (TextUtils.isEmpty(lName)) {
                textLastName.setError("Last Name Required.");
                return;
            }
            if (TextUtils.isEmpty(age))
            {
                textAge.setError("Age Required.");
                return;
            } else {
                int age_num =  Integer.parseInt(textAge.getText().toString());
                if (age_num < 5 || age_num > 80)
                {
                    textAge.setError("Age Not Valid");
                    return;
                }
            }
            if (!isValidPhone(phoneNum)) {
                textPhone.setError("Invalid Phone Number!");
                return;
            }

            Amplify.DataStore.query(UserInfo.class, Where.matches(UserInfo.EMAIL.eq(Amplify.Auth.getCurrentUser().getUsername())),
                    Users -> {
                        if (Users.hasNext()) {
                            UserInfo original = Users.next();
                            UserInfo edited = original.copyOfBuilder()
                                    .firstname(textFirstName.getText().toString())
                                    .lastname(textLastName.getText().toString())
                                    .age(textAge.getText().toString())
                                    .phone(textPhone.getText().toString())
                                    .build();
                            Amplify.DataStore.save(edited,
                                    updated -> {
                                        Log.i("MyAmplifyApp", "Updated user information.");
                                        String newFullName = textFirstName.getText().toString() + " " + textLastName.getText().toString();
                                        Intent intent = new Intent(getApplicationContext(), UserSettings.class);
                                        intent.putExtra("thisName", newFullName);
                                        intent.putExtra("thisEmail", email);
                                        intent.putExtra("thisProfilePic", profilePic);
                                        startActivity(intent);
                                        runOnUiThread(() -> Toast.makeText(ChangeUserInfo.this, "Changed User Information Successfully.", Toast.LENGTH_SHORT).show());
                                    },
                                    failure -> Log.e("MyAmplifyApp", "Update failed.", failure)
                            );
                        }
                    },
                    failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
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

    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9+]|\\(\\d{1,3}\\))[0-9\\-. ]{3,15}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
