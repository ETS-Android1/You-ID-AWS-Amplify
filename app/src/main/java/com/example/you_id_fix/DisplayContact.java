package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Contacts;

import java.io.Serializable;

public class DisplayContact extends AppCompatActivity implements Serializable {
    String contactName, contactProfession, contactEmail, contactAddress, contactPhone, contactID, contactFirst, contactLast, type, fullName, email, profilePic;
    TextView displayContactName;
    TextView displayContactProfession;
    TextView displayContactEmail;
    TextView displayContactAddress;
    TextView displayContactPhone;
    Button editContact;
    Button removeContact;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

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


        displayContactName = findViewById(R.id.textDisplayContactName);
        displayContactProfession = findViewById(R.id.textDisplayContactProfession);
        displayContactEmail = findViewById(R.id.textDisplayContactEmail);
        displayContactAddress = findViewById(R.id.textDisplayContactAddress);
        displayContactPhone= findViewById(R.id.textDisplayContactPhone);
        editContact = findViewById(R.id.buttonEditContact);
        removeContact = findViewById(R.id.buttonDeleteContact);
        backArrow = findViewById(R.id.backArrow);

        displayContactName.setText(contactName);
        displayContactProfession.setText(contactProfession);
        displayContactEmail.setText(contactEmail);
        displayContactAddress.setText(contactAddress);
        displayContactPhone.setText(contactPhone);

        editContact.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UpdateContact.class);
            intent.putExtra("cFirst", contactFirst);
            intent.putExtra("cLast", contactLast);
            intent.putExtra("cProfession", contactProfession);
            intent.putExtra("cEmail", contactEmail);
            intent.putExtra("cAddress", contactAddress);
            intent.putExtra("cPhone", contactPhone);
            intent.putExtra("cID", contactID);
            intent.putExtra("cType",type);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        removeContact.setOnClickListener(v -> Amplify.DataStore.query(Contacts.class, Where.id(contactID),
                Contacts -> {
                    if (Contacts.hasNext()) {
                        Contacts contact = Contacts.next();
                        Amplify.DataStore.delete(contact,
                                deleted -> {Log.i("MyAmplifyApp", "Deleted a contact.");
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
                                failure -> Log.e("MyAmplifyApp", "Delete failed.", failure)
                        );
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
        ));

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
}