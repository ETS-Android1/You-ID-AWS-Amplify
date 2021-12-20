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
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageGetUrlOptions;
import com.amplifyframework.storage.options.StorageListOptions;
import com.amplifyframework.storage.options.StorageRemoveOptions;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class IceDisplayContacts extends AppCompatActivity implements Serializable {
    String contactName, contactProfession, contactEmail, contactAddress, contactPhone, contactID, contactFirst, contactLast, type, fullName, email, profilePic;
    CircleImageView contactPicture;
    TextView displayContactName, displayContactTitle, displayContactEmail, displayContactAddress, displayContactPhone;
    Button editContact;
    Button removeContact;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_display_contacts);

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

        displayContactName = findViewById(R.id.textDisplayIceContactName);
        displayContactTitle = findViewById(R.id.textDisplayIceContactTitle);
        displayContactEmail = findViewById(R.id.textDisplayIceContactEmail);
        displayContactAddress = findViewById(R.id.textDisplayIceContactAddress);
        displayContactPhone= findViewById(R.id.textDisplayIceContactPhone);
        editContact = findViewById(R.id.buttonEditIceContact);
        removeContact = findViewById(R.id.buttonDeleteIceContact);
        contactPicture = findViewById(R.id.imageViewContactPic);
        backArrow = findViewById(R.id.backArrow);

        // .accessLevel(StorageAccessLevel.PRIVATE)
        StorageListOptions options = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        // Access to private files URL
        StorageGetUrlOptions URLOptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageRemoveOptions URLOptions2 = StorageRemoveOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageListOptions URLOptions3 = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.list(
                "",
                options,
                result -> {
                    Log.i("Getting List of Images", "From s3 Bucket:");
                    for (StorageItem item : result.getItems()) {
                        Log.i("List Files function", "Checking for ID image - Image Key = Item: " + item.getKey());
                        if (item.getKey().equals("contact_" + type))
                        {
                            Amplify.Storage.getUrl(
                                    item.getKey(),
                                    URLOptions,
                                    find -> {
                                        Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                        runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(contactPicture));
                                    },
                                    error -> Log.e("Storage URL", "URL generation failure", error));
                        }
                    }
                } ,error -> Log.e("Storage URL", "URL generation failure", error));

        displayContactName.setText(contactName);
        displayContactTitle.setText(contactProfession);
        displayContactEmail.setText(contactEmail);
        displayContactAddress.setText(contactAddress);
        displayContactPhone.setText(contactPhone);

        editContact.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), IceEditContacts.class);
            intent.putExtra("cName", contactName);
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

        removeContact.setOnClickListener(v -> {
            Amplify.DataStore.query(Contacts.class, Where.id(contactID),
                    Contacts -> {
                        if (Contacts.hasNext()) {
                            Contacts contact = Contacts.next();
                            Amplify.DataStore.delete(contact,
                                    deleted -> Log.i("MyAmplifyApp", "Deleted a contact."),
                                    failure -> Log.e("MyAmplifyApp", "Delete failed.", failure)
                            );
                        }
                    },
                    failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
            );
            Amplify.Storage.list("", URLOptions3,
                    result -> {
                        for (StorageItem item : result.getItems()) {
                            if (item.getKey().equals("contact_" + type)) {
                                Amplify.Storage.remove(
                                        "contact_" + type, URLOptions2,
                                        result2 -> {Log.i("Contact Image", "Successfully removed: " + result2.getKey()); },
                                        error2 -> Log.e("Contact Image", "Remove failure", error2)
                                );
                            }
                        }
                    },
                    error -> Log.e("Contact Picture", "Something went wrong", error)
            );
            Intent intent = new Intent(getApplicationContext(), ICEActivity.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);;
            intent.putExtra("update", "y");
            intent.putExtra("deletedContact", "y");
            intent.putExtra("prevType", type);
            startActivity(intent);
        });

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ICEActivity.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });
    }
}
