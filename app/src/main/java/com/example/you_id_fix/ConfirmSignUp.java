package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.UserInfo;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ConfirmSignUp extends AppCompatActivity {
    EditText confirmation_code;
    String firstName, lastName, age, phone, email, confirmationCode;
    ImageView backArrow;
    Button confirmCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_sign_up);
        backArrow = findViewById(R.id.backArrow);
        confirmation_code = findViewById(R.id.editTextCode);
        confirmCode = findViewById(R.id.buttonConfirmSignUp);

        confirmCode.setOnClickListener(this::getConfirmation);

        backArrow.setOnClickListener(v -> gotoLogin());
    }

    private void gotoLogin()
    {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

    public void getConfirmation(View view) {

        Intent receiverIntent = getIntent();
        email = receiverIntent.getStringExtra("userEmail");
        firstName = receiverIntent.getStringExtra("userFirstName");
        lastName = receiverIntent.getStringExtra("userLastName");
        age = receiverIntent.getStringExtra("userAge");
        phone = receiverIntent.getStringExtra("userPhone");
        confirmationCode = confirmation_code.getText().toString();

        Amplify.Auth.confirmSignUp(
                email,
                confirmationCode,
                result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? storeUserData(firstName, lastName, age, email, phone) : "Confirm sign up not complete"),
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }

    private String storeUserData(String fName, String lName, String age, String email, String phone) {

        // Setting time variable
        Date date = new Date();
        int offsetMillis = TimeZone.getDefault().getOffset(date.getTime());
        int offsetSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(offsetMillis);
        Temporal.DateTime temporalDateTime = new Temporal.DateTime(date, offsetSeconds);

        // Creating and storing data in new object in our database
        UserInfo item = UserInfo.builder()
                .firstname(fName)
                .lastname(lName)
                .email(email)
                .age(age)
                .phone(phone)
                .completedAt(temporalDateTime)
                .build();

        Amplify.DataStore.save(item,
                success -> Log.i(" User data saved", "Saved item: " + success.item().getFirstname()),
                error -> Log.e("Tutorial", "Could not save item to DataStore", error)
        );

        gotoLogin();
        return "Confirm SignUp complete! User data saved in database! ";
    }
}