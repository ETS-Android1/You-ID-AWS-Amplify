package com.example.you_id_fix;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Contacts;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageGetUrlOptions;
import com.amplifyframework.storage.options.StorageListOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Merchant extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Serializable {
    private static final String TAG = "Permissions";
    String userID;
    String displayEmail;
    ListView listMerchantContacts;
    ImageView imgView;
    Button editImages;
    String fullName;
    String email;
    String profilePic;
    TextView navFirstName;
    TextView navEmail;
    CircleImageView navProfilePic;
    NavigationView navView;
    DrawerLayout drawer;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    Spinner merchantSpinner;
    ArrayList<String> documentNames = new ArrayList<>();
    String selectedDocument;
    String selectedDocumentURL;
    ArrayAdapter<String> spinnerArrayAdapter;
    ImageView imageVAddContact;
    ImageView imageVBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        displayEmail = Amplify.Auth.getCurrentUser().getUsername();
        userID = Amplify.Auth.getCurrentUser().getUserId();
       imageVAddContact = findViewById(R.id.imageViewAddContact);
        imgView = findViewById(R.id.imageViewMerchantDocument);
        imageVBack = findViewById(R.id.returnToMainFromMerchant);
        editImages = findViewById(R.id.buttonEditMerchantImage);
        navView = findViewById(R.id.nav_view_merchant);
        drawer = findViewById(R.id.drawerLayoutMerchant);
        merchantSpinner = findViewById(R.id.dropDownMerchantDocuments);
        merchantSpinner.setOnItemSelectedListener(this);

        navView.setCheckedItem(R.id.navMerchant);
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navHome) {
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navUpload) {
                Intent intent = new Intent(getApplicationContext(), Upload.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navDownload) {
                Intent intent = new Intent(getApplicationContext(), BucketImageList.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navHealth) {
                Intent intent = new Intent(getApplicationContext(), Health.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navMerchant) {
                Intent intent = new Intent(getApplicationContext(), Merchant.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navLegal) {
                Intent intent = new Intent(getApplicationContext(), Legal.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navTravel) {
                Intent intent = new Intent(getApplicationContext(), Travel.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navEmergency) {
                Intent intent = new Intent(getApplicationContext(), ICEActivity.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navSettings) {
                Intent intent = new Intent(getApplicationContext(), UserSettings.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navSupport) {
                Intent intent = new Intent(getApplicationContext(), Support.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            } else if (id == R.id.navSignOut) {
                Amplify.Auth.signOut(
                        () -> {
                            Log.i("AuthQuickstart", "Signed out successfully");
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
            DrawerLayout drawer = findViewById(R.id.drawerLayoutMerchant);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        View header = navView.getHeaderView(0);
        navFirstName = header.findViewById(R.id.nav_header_first_name);
        navEmail = header.findViewById(R.id.nav_header_email);
        navProfilePic = header.findViewById(R.id.nav_header_picture);

        navFirstName.setText(fullName);
        navEmail.setText(email);
        Picasso.get().load(profilePic).into(navProfilePic);

        // .accessLevel(StorageAccessLevel.PRIVATE)
        StorageListOptions options = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.list(
                "merchant",
                options,
                result -> {
                    Log.i("Getting List of Images", "From s3 Bucket:");
                    for (StorageItem item : result.getItems()) {
                        Log.i("List Files function", "Image Key = Item: " + item.getKey());
                        documentNames.add(item.getKey());
                        runOnUiThread(() -> spinnerArrayAdapter.notifyDataSetChanged());
                    }
                }, error -> Log.e("Storage URL", "URL generation failure", error));


        spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        documentNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        merchantSpinner.setAdapter(spinnerArrayAdapter);

        editImages.setOnClickListener(v -> {

            if (TextUtils.isEmpty(selectedDocument))
            {
                Toast.makeText(getApplicationContext(),"No Document found!", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(getApplicationContext(), EditImage.class);
            intent.putExtra("thisDocument", selectedDocument);
            intent.putExtra("thisDocumentURL", selectedDocumentURL);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            intent.putExtra("type", "merchant");
            startActivity(intent);
        });

        Amplify.DataStore.query(
                Contacts.class,
                Where.matches(Contacts.USER_ID.eq(userID).and(Contacts.TYPE.eq("Merchant"))),
                Contacts -> {
                    while (Contacts.hasNext()) {
                        Contacts contact = Contacts.next();
                        HashMap<String,String> item;
                        item = new HashMap<>();
                        item.put("line1", (contact.getFirstname() + " " + contact.getLastname()));
                        item.put("line2", contact.getProfession());
                        item.put("line3", contact.getEmail());
                        item.put("line4", contact.getAddress());
                        item.put("line5", contact.getPhone());
                        item.put("line6", contact.getId());
                        item.put("line7", contact.getFirstname());
                        item.put("line8", contact.getLastname());
                        list.add(item);
                        Log.i("MyAmplifyApp", "Contact with ID of " + contact.getId() + " is added to the list" );
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Something went wrong with fetching contact information.", failure)
        );

        listMerchantContacts = findViewById(R.id.listViewMerchantContacts);

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_contacts,
                new String[] {"line1","line2"},
                new int[] {R.id.textViewContactName, R.id.textViewContactProfession});
        listMerchantContacts.setAdapter(adapter);

        imageVAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddContact.class);
            intent.putExtra("type", "Merchant");
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        imageVBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.putExtra("type", "Merchant");
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        listMerchantContacts.setOnItemClickListener((parent, view, position, id) -> {
            String cName = list.get(position).get("line1");
            String cProfession = list.get(position).get("line2");
            String cEmail = list.get(position).get("line3");
            String cAddress = list.get(position).get("line4");
            String cPhone = list.get(position).get("line5");
            String cID = list.get(position).get("line6");
            String cFirst = list.get(position).get("line7");
            String cLast = list.get(position).get("line8");
            Intent intent = new Intent(getApplicationContext(), DisplayContact.class);
            intent.putExtra("cName", cName);
            intent.putExtra("cProfession", cProfession);
            intent.putExtra("cEmail", cEmail);
            intent.putExtra("cAddress", cAddress);
            intent.putExtra("cPhone", cPhone);
            intent.putExtra("cID", cID);
            intent.putExtra("cFirst", cFirst);
            intent.putExtra("cLast", cLast);
            intent.putExtra("cType","Merchant");
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });
    }
    public void onItemSelected (AdapterView < ? > parent, View view, int pos, long id){
        selectedDocument = documentNames.get(pos);

        // Access to private files URL
        StorageGetUrlOptions URLOptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.getUrl(
                documentNames.get(pos),
                URLOptions,
                find -> {
                    Log.i("Storage URL", "Successfully generated: " + find.getUrl());
                    selectedDocumentURL = find.getUrl().toString();
                    runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(imgView));
                },
                error -> Log.e("Storage URL", "URL generation failure", error));
    }

    public void onNothingSelected (AdapterView < ? > parent) {
        imgView.setImageResource(R.drawable.ic_baseline_broken_image_24);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                }

            case 3:
                Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                }
        }
    }
}
