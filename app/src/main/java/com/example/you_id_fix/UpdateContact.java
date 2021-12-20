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
import com.amplifyframework.datastore.generated.model.Contacts;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateContact extends AppCompatActivity implements Serializable {
    String contactName;
    String contactProfession;
    String contactEmail;
    String contactAddress;
    String contactPhone;
    String contactID;
    String contactFirst;
    String contactLast;
    String type;
    String fullName;
    String email;
    String profilePic;
    EditText updateContactFirstName;
    EditText updateContactLastName;
    EditText updateContactProfession;
    EditText updateContactEmail;
    EditText updateContactAddress;
    EditText updateContactPhone;
    Button updateContact;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            contactName = extras.getString("cName");
            contactProfession = extras.getString("cProfession");
            contactEmail = extras.getString("cEmail");
            contactAddress = extras.getString("cAddress");
            contactPhone = extras.getString("cPhone");
            contactID = extras.getString("cID");
            contactFirst = extras.getString("cFirst");
            contactLast = extras.getString("cLast");
            type = extras.getString("cType");
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        updateContactFirstName = findViewById(R.id.updateTextContactFirstName);
        updateContactLastName = findViewById(R.id.updateTextContactLastName);
        updateContactProfession = findViewById(R.id.updateTextContactProfession);
        updateContactEmail = findViewById(R.id.updateTextContactEmail);
        updateContactAddress = findViewById(R.id.updateTextContactAddress);
        updateContactPhone = findViewById(R.id.updateTextContactPhone);
        updateContact = findViewById(R.id.buttonUpdateContactInfo);
        backArrow = findViewById(R.id.backArrow);

        updateContactFirstName.setText(contactFirst);
        updateContactLastName.setText(contactLast);
        updateContactProfession.setText(contactProfession);
        updateContactAddress.setText(contactAddress);
        updateContactEmail.setText(contactEmail);
        updateContactPhone.setText(contactPhone);

        updateContact.setOnClickListener(v -> {
            String firstName = updateContactFirstName.getText().toString();
            String lastName = updateContactFirstName.getText().toString();
            String profession = updateContactProfession.getText().toString();
            String address = updateContactAddress.getText().toString();
            String email2 = updateContactEmail.getText().toString();
            String phone = updateContactPhone.getText().toString();

            // Validating user inputs
            if (TextUtils.isEmpty(firstName)) {
                updateContactFirstName.setError("First Name Required.");
                return;
            }
            if (firstName.length() < 3 || firstName.length() > 30) {
                updateContactFirstName.setError("First Name not valid!");
                return;
            }
            if (TextUtils.isEmpty(lastName)) {
                updateContactLastName.setError("Last Name Required.");
                return;
            }
            if (TextUtils.isEmpty(profession)) {
                updateContactProfession.setError("Profession Required.");
                return;
            }
            if (TextUtils.isEmpty(address)) {
                updateContactAddress.setError("Address Required.");
                return;
            }
            if (address.length() < 3) {
                updateContactAddress.setError("Address not valid!");
                return;
            }
            if (TextUtils.isEmpty(email2)) {
                updateContactEmail.setError("Email Required.");
                return;
            }
            if (!(email2.contains("@")) || !(email2.contains(".com"))) {
                updateContactEmail.setError("Invalid Email!");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                updateContactPhone.setError("Phone Number Required.");
                return;
            }
           if (!isValidPhone(phone)) {
              updateContactPhone.setError("Invalid Phone Number!");
              return;
           }

            Amplify.DataStore.query(Contacts.class, Where.id(contactID),
                    Contacts -> {
                        if (Contacts.hasNext()) {
                            Contacts original = Contacts.next();
                            Contacts edited = original.copyOfBuilder()
                                    .firstname(updateContactFirstName.getText().toString())
                                    .lastname(updateContactLastName.getText().toString())
                                    .profession(updateContactProfession.getText().toString())
                                    .email(updateContactEmail.getText().toString())
                                    .address(updateContactAddress.getText().toString())
                                    .phone(updateContactPhone.getText().toString())
                                    .build();
                            Amplify.DataStore.save(edited,
                                    updated -> {Log.i("MyAmplifyApp", "Updated a Contact.");
                                        if (type.equals("Health")) {
                                            Intent intent = new Intent(getApplicationContext(), Health.class);
                                            intent.putExtra("thisName", fullName);
                                            intent.putExtra("thisEmail", email);
                                            intent.putExtra("thisProfilePic", profilePic);
                                            startActivity(intent); }
                                        if (type.equals("Legal")) {
                                            Intent intent = new Intent(getApplicationContext(), Legal.class);
                                            intent.putExtra("thisName", fullName);
                                            intent.putExtra("thisEmail", email);
                                            intent.putExtra("thisProfilePic", profilePic);
                                            startActivity(intent); }
                                        if (type.equals("Travel")) {
                                            Intent intent = new Intent(getApplicationContext(), Travel.class);
                                            intent.putExtra("thisName", fullName);
                                            intent.putExtra("thisEmail", email);
                                            intent.putExtra("thisProfilePic", profilePic);
                                            startActivity(intent); }
                                        if (type.equals("Merchant")) {
                                            Intent intent = new Intent(getApplicationContext(), Merchant.class);
                                            intent.putExtra("thisName", fullName);
                                            intent.putExtra("thisEmail", email);
                                            intent.putExtra("thisProfilePic", profilePic);
                                            startActivity(intent); }
                                        },
                                    failure -> Log.e("MyAmplifyApp", "Update failed.", failure)
                            );
                        }
                    },
                    failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
            );
        });

        backArrow.setOnClickListener(v -> {
            if (type.equals("Health")) {
                Intent intent = new Intent(getApplicationContext(), Health.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent); }
            if (type.equals("Legal")) {
                Intent intent = new Intent(getApplicationContext(), Legal.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent); }
            if (type.equals("Travel")) {
                Intent intent = new Intent(getApplicationContext(), Travel.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent); }
            if (type.equals("Merchant")) {
                Intent intent = new Intent(getApplicationContext(), Merchant.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent); }
        });

    }
    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9+]|\\(\\d{1,3}\\))[0-9\\-. ]{3,15}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
