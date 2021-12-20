package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Contacts;
import com.amplifyframework.datastore.generated.model.UserInfo;


import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddContact extends AppCompatActivity implements Serializable {
    String contactType, userID, loggedInEmail, firstName, lastName, profession, address, thisEmail, phone, fullName, email, profilePic;
    EditText contactFirstName, contactLastName, contactProfession, contactAddress, contactEmail, contactPhone;
    Button submitContact;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        submitContact = findViewById(R.id.buttonSubmitContactInfo);
        contactFirstName = findViewById(R.id.editTextContactFirstName);
        contactLastName = findViewById(R.id.editTextContactLastName);
        contactProfession = findViewById(R.id.editTextProfession);
        contactAddress = findViewById(R.id.editTextContactAddress);
        contactEmail = findViewById(R.id.editTextContactEmail);
        contactPhone = findViewById(R.id.editTextContactPhone);
        backArrow = findViewById(R.id.backArrow);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            contactType = extras.getString("type");
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        Amplify.DataStore.query(
                UserInfo.class,
                Where.matches(UserInfo.EMAIL.eq(loggedInEmail)),
                Users -> {
                    while (Users.hasNext()) {
                        UserInfo todo = Users.next();
                        userID = todo.getId();
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Something went wrong with fetching user ID.", failure)
        );

        submitContact.setOnClickListener(v -> {

            firstName = contactFirstName.getText().toString();
            lastName = contactLastName.getText().toString();
            profession = contactProfession.getText().toString();
            address = contactAddress.getText().toString();
            thisEmail = contactEmail.getText().toString();
            phone = contactPhone.getText().toString();

            // Validating user inputs
            if (TextUtils.isEmpty(firstName)) {
                contactFirstName.setError("First Name Required.");
                return;
            }
            if (firstName.length() < 3 || firstName.length() > 30) {
                contactFirstName.setError("First Name not valid!");
                return;
            }
            if (TextUtils.isEmpty(lastName)) {
                contactLastName.setError("Last Name Required.");
                return;
            }
            if (TextUtils.isEmpty(profession)) {
                contactProfession.setError("Profession Required.");
                return;
            }
            if (TextUtils.isEmpty(address)) {
                contactAddress.setError("Address Required.");
                return;
            }
            if (address.length() < 3) {
                contactAddress.setError("Address not valid!");
                return;
            }
            if (TextUtils.isEmpty(thisEmail)) {
                contactEmail.setError("Email Required.");
                return;
            }
            if (!(thisEmail.contains("@")) || !(thisEmail.contains(".com"))) {
                contactEmail.setError("Invalid Email!");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                contactPhone.setError("Phone Number Required.");
                return;
            }
            if (!isValidPhone(phone)) {
                contactPhone.setError("Invalid Phone Number!");
                return;
            }

            storeContactData(firstName, lastName, profession, thisEmail, phone, address, contactType);
            moveActivity();

        });

        backArrow.setOnClickListener(v -> {
            if (contactType.equals("Health")) {
                Intent intent = new Intent(getApplicationContext(), Health.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent); }
            if (contactType.equals("Legal")) {
                Intent intent = new Intent(getApplicationContext(), Legal.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
            if (contactType.equals("Travel")) {
                Intent intent = new Intent(getApplicationContext(), Travel.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
            if (contactType.equals("Merchant")) {
                Intent intent = new Intent(getApplicationContext(), Merchant.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
        });
    }

    private void storeContactData(String firstname, String lastname, String profession, String email, String phone, String address, String type) {

        // Setting time variable
        Date date = new Date();
        int offsetMillis = TimeZone.getDefault().getOffset(date.getTime());
        int offsetSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(offsetMillis);
        Temporal.DateTime temporalDateTime = new Temporal.DateTime(date, offsetSeconds);

        //Creating new object in our database
        Contacts contact = Contacts.builder()
                .firstname(firstname)
                .lastname(lastname)
                .profession(profession)
                .email(email)
                .phone(phone)
                .address(address)
                .type(type)
                .userId(Amplify.Auth.getCurrentUser().getUserId())
                .completedAt(temporalDateTime)
                .build();

        Amplify.DataStore.save(
                contact,
                success -> Log.i("Contact Table", "Saved item: " + success.item().getId()),
                error -> Log.e("Contact Table", "Could not save item to DataStore", error)
        );
    }

    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9+]|\\(\\d{1,3}\\))[0-9\\-. ]{3,15}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public void moveActivity() {
        if (contactType.equals("Health")) {
            Intent intent = new Intent(getApplicationContext(), Health.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        }
        if (contactType.equals("Legal")) {
            Intent intent = new Intent(getApplicationContext(), Legal.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        }
        if (contactType.equals("Travel")) {
            Intent intent = new Intent(getApplicationContext(), Travel.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        }
        if (contactType.equals("Merchant")) {
            Intent intent = new Intent(getApplicationContext(), Merchant.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        }
    }
}