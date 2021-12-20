package com.example.you_id_fix;

import android.content.Intent;
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

public class Legal extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Serializable {
    String userID;
    String displayEmail;
    ListView listLegalContacts;
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
    ArrayList<HashMap<String,String>> list = new ArrayList<>();
    Spinner legalSpinner;
    ArrayList<String> documentNames = new ArrayList<>();
    String selectedDocument;
    String selectedDocumentURL;
    ArrayAdapter<String> spinnerArrayAdapter;
    ImageView imageVAddContact;
    ImageView imageVBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        displayEmail = Amplify.Auth.getCurrentUser().getUsername();
        userID = Amplify.Auth.getCurrentUser().getUserId();
        imageVAddContact = findViewById(R.id.imageViewAddContact);
        imgView = findViewById(R.id.imageViewLegalDocument);
        imageVBack = findViewById(R.id.returnToMainFromLegal);
        editImages = findViewById(R.id.btn_editLegalImages);
        navView = findViewById(R.id.nav_view_legal);
        drawer = findViewById(R.id.drawerLayoutLegal);
        legalSpinner = findViewById(R.id.dropDownLegalDocuments);
        legalSpinner.setOnItemSelectedListener(this);

        navView.setCheckedItem(R.id.navLegal);
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
            DrawerLayout drawer = findViewById(R.id.drawerLayoutLegal);
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
                "legal",
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
        legalSpinner.setAdapter(spinnerArrayAdapter);

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
            intent.putExtra("type", "legal");
            startActivity(intent);
        });


        Amplify.DataStore.query(
                Contacts.class,
                Where.matches(Contacts.USER_ID.eq(userID).and(Contacts.TYPE.eq("Legal"))),
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

        listLegalContacts = findViewById(R.id.listViewLegalContacts);

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_contacts,
                new String[] {"line1","line2"},
                new int[] {R.id.textViewContactName, R.id.textViewContactProfession});
        listLegalContacts.setAdapter(adapter);

        imageVAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddContact.class);
            intent.putExtra("type", "Legal");
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        imageVBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        listLegalContacts.setOnItemClickListener((parent, view, position, id) -> {
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
            intent.putExtra("cType","Legal");
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
}