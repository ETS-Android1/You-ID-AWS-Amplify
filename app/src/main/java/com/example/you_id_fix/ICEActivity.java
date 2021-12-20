package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class ICEActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Serializable {
    String userID, id_image_url, fullName, email, profilePic, tProfession, tEmail, tAddress, tPhone, tID, tFirst, tLast, tName, tPicture, prevType, update, selectedDocument, selectedDocumentURL, deletedContact;
    ConstraintLayout documentLayout;
    CircleImageView ImageV_contactOne, ImageV_contactTwo, ImageV_contactThree, navProfilePic;
    ImageView backArrow, ImageV_identification, ICEDocumentImage;
    TextView TV_contactOne,TV_contactTwo,TV_contactThree, navFirstName, navEmail;
    ArrayList<HashMap<String,String>> Contact1list = new ArrayList<>();
    ArrayList<HashMap<String,String>> Contact2list = new ArrayList<>();
    ArrayList<HashMap<String,String>> Contact3list = new ArrayList<>();
    NavigationView navView;
    DrawerLayout drawer;
    Spinner IceSpinner;
    ArrayList<String> documentNames = new ArrayList<>();
    ArrayAdapter<String> spinnerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iceactivity);
        documentLayout=findViewById(R.id.documentLayout);
        documentLayout.setVisibility(View.GONE);
        userID = Amplify.Auth.getCurrentUser().getUserId();

        ImageV_contactOne = findViewById(R.id.IV_contactOne);
        TV_contactOne = findViewById(R.id.TV_contactOne);
        ImageV_contactTwo = findViewById(R.id.IV_contactTwo);
        TV_contactTwo = findViewById(R.id.TV_contactTwo);
        ImageV_contactThree = findViewById(R.id.IV_contactThree);
        TV_contactThree = findViewById(R.id.TV_contactThree);
        ImageV_identification = findViewById(R.id.IV_identification);
        ICEDocumentImage = findViewById(R.id.iceDocumentImageView);
        backArrow = findViewById(R.id.backArrow);
        IceSpinner = findViewById(R.id.dropDownICEDocuments);
        IceSpinner.setOnItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
            tProfession = extras.getString("cProfession");
            tEmail = extras.getString("cEmail");
            tAddress = extras.getString("cAddress");
            tPhone = extras.getString("cPhone");
            tID = extras.getString("cID");
            tFirst = extras.getString("cFirst");
            tLast = extras.getString("cLast");
            tName = extras.getString("cName");
            tPicture = extras.getString("cPicture");
            prevType = extras.getString("prevType");
            update = extras.getString("update");
            deletedContact = extras.getString("deletedContact");

            if (update != null) {
                // .accessLevel(StorageAccessLevel.PRIVATE)
                StorageListOptions options = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                        .build();

                // Access to private files URL
                StorageGetUrlOptions URLOptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                        .build();

                Amplify.DataStore.query(
                        Contacts.class,
                        Where.matches(Contacts.TYPE.eq("ICE1").and(Contacts.USER_ID.eq(userID))),
                        Contacts -> {
                            while (Contacts.hasNext()) {
                                Contacts contact = Contacts.next();
                                Log.i("MyAmplifyApp", "Contact 1: " +  contact);
                                HashMap<String,String> item;
                                item = new HashMap<>();
                                item.put("line2", contact.getProfession());
                                item.put("line3", contact.getEmail());
                                item.put("line4", contact.getAddress());
                                item.put("line5", contact.getPhone());
                                item.put("line6", contact.getId());
                                item.put("line7", contact.getFirstname());
                                item.put("line8", contact.getLastname());
                                item.put("line9", contact.getFirstname() + " " + contact.getLastname());
                                Contact1list.add(item);
                                new Handler(Looper.getMainLooper()).post(() -> TV_contactOne.setText(contact.getProfession()));
                            }
                        },
                        failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
                );

                Amplify.DataStore.query(
                        Contacts.class,
                        Where.matches(Contacts.TYPE.eq("ICE2").and(Contacts.USER_ID.eq(userID))),
                        Contacts -> {
                            while (Contacts.hasNext()) {
                                Contacts contact = Contacts.next();
                                Log.i("MyAmplifyApp", "Contact 2: " +  contact);
                                HashMap<String,String> item;
                                item = new HashMap<>();
                                item.put("line2", contact.getProfession());
                                item.put("line3", contact.getEmail());
                                item.put("line4", contact.getAddress());
                                item.put("line5", contact.getPhone());
                                item.put("line6", contact.getId());
                                item.put("line7", contact.getFirstname());
                                item.put("line8", contact.getLastname());
                                item.put("line9", contact.getFirstname() + " " + contact.getLastname());
                                Contact2list.add(item);
                                new Handler(Looper.getMainLooper()).post(() -> TV_contactTwo.setText(contact.getProfession()));
                            }
                        },
                        failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
                );

                Amplify.DataStore.query(
                        Contacts.class,
                        Where.matches(Contacts.TYPE.eq("ICE3").and(Contacts.USER_ID.eq(userID))),
                        Contacts -> {
                            while (Contacts.hasNext()) {
                                Contacts contact = Contacts.next();
                                Log.i("MyAmplifyApp", "Contact 3: " +  contact);
                                HashMap<String,String> item;
                                item = new HashMap<>();
                                item.put("line2", contact.getProfession());
                                item.put("line3", contact.getEmail());
                                item.put("line4", contact.getAddress());
                                item.put("line5", contact.getPhone());
                                item.put("line6", contact.getId());
                                item.put("line7", contact.getFirstname());
                                item.put("line8", contact.getLastname());
                                item.put("line9", contact.getFirstname() + " " + contact.getLastname());
                                Contact3list.add(item);
                                new Handler(Looper.getMainLooper()).post(() -> TV_contactThree.setText(contact.getProfession()));
                            }
                        },
                        failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
                );

                    Amplify.Storage.list(
                            "",
                            options,
                            result -> {
                                Log.i("Getting List of Images", "From s3 Bucket:");
                                for (StorageItem item : result.getItems()) {
                                    Log.i("List Files function", "Checking for ID image - Image Key = Item: " + item.getKey());
                                    if (("merchant\\identification").equals(item.getKey()))
                                    {
                                        Amplify.Storage.getUrl(
                                                item.getKey(),
                                                URLOptions,
                                                find -> {
                                                    Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                                    id_image_url = find.getUrl().toString();
                                                    runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(ImageV_identification));
                                                },
                                                error -> Log.e("Storage URL", "URL generation failure", error));
                                    }
                                    if (item.getKey().equals("contact_ICE1")) {
                                        Amplify.Storage.getUrl(
                                                item.getKey(),
                                                URLOptions,
                                                find -> {
                                                    Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                                    runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(ImageV_contactOne));
                                                    if (TV_contactOne.getText().toString().equals(""))
                                                        new Handler(Looper.getMainLooper()).post(() -> ImageV_contactOne.setImageResource(R.drawable.ic_baseline_person_24));
                                                },
                                                error -> Log.e("Storage URL", "URL generation failure", error));
                                    }
                                    if (item.getKey().equals("contact_ICE2")) {
                                        Amplify.Storage.getUrl(
                                                item.getKey(),
                                                URLOptions,
                                                find -> {
                                                    Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                                    runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(ImageV_contactTwo));
                                                },
                                                error -> Log.e("Storage URL", "URL generation failure", error));
                                    }
                                    if (item.getKey().equals("contact_ICE3")) {
                                        Amplify.Storage.getUrl(
                                                item.getKey(),
                                                URLOptions,
                                                find -> {
                                                    Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                                    runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(ImageV_contactThree));
                                                },
                                                error -> Log.e("Storage URL", "URL generation failure", error));
                                    }
                                }
                            }, error -> Log.e("Storage URL", "URL generation failure", error));
                    Log.i("ID Image URL:" , " Received ID imageURL: " + id_image_url );
                    Picasso.get().load(id_image_url).into( ImageV_identification);

                if (TV_contactOne.getText().toString().equals("")) {
                    ImageV_contactOne.invalidate();
                }
            }
        }

        navView = findViewById(R.id.nav_view_ice);
        drawer = findViewById(R.id.drawerLayoutICE);
        navView.setCheckedItem(R.id.navEmergency);
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
            DrawerLayout drawer = findViewById(R.id.drawerLayoutICE);
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

        // Access to private files URL
        StorageGetUrlOptions URLOptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.DataStore.query(
                Contacts.class,
                Where.matches(Contacts.TYPE.eq("ICE1").and(Contacts.USER_ID.eq(userID))),
                Contacts -> {
                    while (Contacts.hasNext()) {
                        Contacts contact = Contacts.next();
                        Log.i("MyAmplifyApp", "Contact 1: " +  contact);
                        HashMap<String,String> item;
                        item = new HashMap<>();
                        item.put("line2", contact.getProfession());
                        item.put("line3", contact.getEmail());
                        item.put("line4", contact.getAddress());
                        item.put("line5", contact.getPhone());
                        item.put("line6", contact.getId());
                        item.put("line7", contact.getFirstname());
                        item.put("line8", contact.getLastname());
                        item.put("line9", contact.getFirstname() + " " + contact.getLastname());
                        Contact1list.add(item);
                        new Handler(Looper.getMainLooper()).post(() -> TV_contactOne.setText(contact.getProfession()));
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
        );

        Amplify.DataStore.query(
                Contacts.class,
                Where.matches(Contacts.TYPE.eq("ICE2").and(Contacts.USER_ID.eq(userID))),
                Contacts -> {
                    while (Contacts.hasNext()) {
                        Contacts contact = Contacts.next();
                        Log.i("MyAmplifyApp", "Contact 2: " +  contact);
                        HashMap<String,String> item;
                        item = new HashMap<>();
                        item.put("line2", contact.getProfession());
                        item.put("line3", contact.getEmail());
                        item.put("line4", contact.getAddress());
                        item.put("line5", contact.getPhone());
                        item.put("line6", contact.getId());
                        item.put("line7", contact.getFirstname());
                        item.put("line8", contact.getLastname());
                        item.put("line9", contact.getFirstname() + " " + contact.getLastname());
                        Contact2list.add(item);
                        new Handler(Looper.getMainLooper()).post(() -> TV_contactTwo.setText(contact.getProfession()));
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
        );

        Amplify.DataStore.query(
                Contacts.class,
                Where.matches(Contacts.TYPE.eq("ICE3").and(Contacts.USER_ID.eq(userID))),
                Contacts -> {
                    while (Contacts.hasNext()) {
                        Contacts contact = Contacts.next();
                        Log.i("MyAmplifyApp", "Contact 3: " +  contact);
                        HashMap<String,String> item;
                        item = new HashMap<>();
                        item.put("line2", contact.getProfession());
                        item.put("line3", contact.getEmail());
                        item.put("line4", contact.getAddress());
                        item.put("line5", contact.getPhone());
                        item.put("line6", contact.getId());
                        item.put("line7", contact.getFirstname());
                        item.put("line8", contact.getLastname());
                        item.put("line9", contact.getFirstname() + " " + contact.getLastname());
                        Contact3list.add(item);
                        new Handler(Looper.getMainLooper()).post(() -> TV_contactThree.setText(contact.getProfession()));
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
        );

        Amplify.Storage.list(
                "",
                options,
                result -> {
                    Log.i("Getting List of Images", "From s3 Bucket:");
                    for (StorageItem item : result.getItems()) {
                        Log.i("List Files function", "Checking for ID image - Image Key = Item: " + item.getKey());
                        if (("merchant\\identification").equals(item.getKey()))
                        {
                            Amplify.Storage.getUrl(
                                    item.getKey(),
                                    URLOptions,
                                    find -> {
                                        Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                        id_image_url = find.getUrl().toString();
                                        runOnUiThread(() ->Picasso.get().load(find.getUrl().toString()).into(ImageV_identification));
                                    },
                                    error -> Log.e("Storage URL", "URL generation failure", error));
                        }
                        if (item.getKey().equals("contact_ICE1"))
                        {
                            Amplify.Storage.getUrl(
                                    item.getKey(),
                                    URLOptions,
                                    find -> {
                                        Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                        runOnUiThread(() ->Picasso.get().load(find.getUrl().toString()).into(ImageV_contactOne));
                                        if (TV_contactOne.getText().toString().equals(""))
                                        new Handler(Looper.getMainLooper()).post(() -> ImageV_contactOne.setImageResource(R.drawable.ic_baseline_person_24));
                                    },
                                    error -> Log.e("Storage URL", "URL generation failure", error));
                        }
                        if (item.getKey().equals("contact_ICE2"))
                        {
                            Amplify.Storage.getUrl(
                                    item.getKey(),
                                    URLOptions,
                                    find -> {
                                        Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                        runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(ImageV_contactTwo));
                                    },
                                    error -> Log.e("Storage URL", "URL generation failure", error));
                        }
                        if (item.getKey().equals("contact_ICE3"))
                        {
                            Amplify.Storage.getUrl(
                                    item.getKey(),
                                    URLOptions,
                                    find -> {
                                        Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                        runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(ImageV_contactThree));
                                    },
                                    error -> Log.e("Storage URL", "URL generation failure", error));
                        }
                    }
                } ,error -> Log.e("Storage URL", "URL generation failure", error));

        Log.i("ID Image URL:" , " Received ID imageURL: " + id_image_url );
        Picasso.get().load(id_image_url).into( ImageV_identification);

        Amplify.Storage.list(
                "health",
                options,
                result -> {
                    Log.i("Getting List of Images", "From s3 Bucket:");
                    for (StorageItem item : result.getItems()) {
                        Log.i("List Files Function", "Image Key = Item: " + item.getKey());
                        documentNames.add(item.getKey());
                        runOnUiThread(() -> spinnerArrayAdapter.notifyDataSetChanged());
                    }
                }, error -> Log.e("Storage URL", "URL generation failure", error));

        spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        documentNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        IceSpinner.setAdapter(spinnerArrayAdapter);

        ImageV_contactOne.setOnClickListener(v -> {
            if (TV_contactOne.getText().toString().equals("")) {
                finish();
                Intent intent = new Intent(getApplicationContext(), IceAddContacts.class);
                intent.putExtra("type", "ice");
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                intent.putExtra("contact", "ICE1");
                startActivity(intent);
            }
            else {
                finish();
                String cProfession = Contact1list.get(0).get("line2");
                String cEmail = Contact1list.get(0).get("line3");
                String cAddress = Contact1list.get(0).get("line4");
                String cPhone = Contact1list.get(0).get("line5");
                String cID = Contact1list.get(0).get("line6");
                String cFirst = Contact1list.get(0).get("line7");
                String cLast = Contact1list.get(0).get("line8");
                String cName = Contact1list.get(0).get("line9");
                Intent intent = new Intent(getApplicationContext(), IceDisplayContacts.class);
                intent.putExtra("cProfession", cProfession);
                intent.putExtra("cEmail", cEmail);
                intent.putExtra("cAddress", cAddress);
                intent.putExtra("cPhone", cPhone);
                intent.putExtra("cID", cID);
                intent.putExtra("cFirst", cFirst);
                intent.putExtra("cLast", cLast);
                intent.putExtra("cName", cName);
                intent.putExtra("cType","ICE1");
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
        });

        ImageV_contactTwo.setOnClickListener(v -> {
            if (TV_contactTwo.getText().toString().equals("")) {
                finish();
                Intent intent = new Intent(getApplicationContext(), IceAddContacts.class);
                intent.putExtra("type", "ice");
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                intent.putExtra("contact", "ICE2");
                startActivity(intent);
            }
            else {
                finish();
                String cProfession = Contact2list.get(0).get("line2");
                String cEmail = Contact2list.get(0).get("line3");
                String cAddress = Contact2list.get(0).get("line4");
                String cPhone = Contact2list.get(0).get("line5");
                String cID = Contact2list.get(0).get("line6");
                String cFirst = Contact2list.get(0).get("line7");
                String cLast = Contact2list.get(0).get("line8");
                String cName = Contact2list.get(0).get("line9");
                Intent intent = new Intent(getApplicationContext(), IceDisplayContacts.class);
                intent.putExtra("cProfession", cProfession);
                intent.putExtra("cEmail", cEmail);
                intent.putExtra("cAddress", cAddress);
                intent.putExtra("cPhone", cPhone);
                intent.putExtra("cID", cID);
                intent.putExtra("cFirst", cFirst);
                intent.putExtra("cLast", cLast);
                intent.putExtra("cName", cName);
                intent.putExtra("cType","ICE2");
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
        });

        ImageV_contactThree.setOnClickListener(v -> {
            if (TV_contactThree.getText().toString().equals("")) {
                finish();
                Intent intent = new Intent(getApplicationContext(), IceAddContacts.class);
                intent.putExtra("type", "ice");
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                intent.putExtra("contact", "ICE3");
                startActivity(intent);
            }
            else {
                finish();
                String cProfession = Contact3list.get(0).get("line2");
                String cEmail = Contact3list.get(0).get("line3");
                String cAddress = Contact3list.get(0).get("line4");
                String cPhone = Contact3list.get(0).get("line5");
                String cID = Contact3list.get(0).get("line6");
                String cFirst = Contact3list.get(0).get("line7");
                String cLast = Contact3list.get(0).get("line8");
                String cName = Contact3list.get(0).get("line9");
                Intent intent = new Intent(getApplicationContext(), IceDisplayContacts.class);
                intent.putExtra("cProfession", cProfession);
                intent.putExtra("cEmail", cEmail);
                intent.putExtra("cAddress", cAddress);
                intent.putExtra("cPhone", cPhone);
                intent.putExtra("cID", cID);
                intent.putExtra("cFirst", cFirst);
                intent.putExtra("cLast", cLast);
                intent.putExtra("cName", cName);
                intent.putExtra("cType","ICE3");
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
        });

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });
    }

    public void onItemSelected (AdapterView< ? > parent, View view, int pos, long id){
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
                    runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(ICEDocumentImage));
                },
                error -> Log.e("Storage URL", "URL generation failure", error));
    }

    public void onNothingSelected (AdapterView < ? > parent) {
        ICEDocumentImage.setImageResource(R.drawable.ic_baseline_broken_image_24);
    }

    public void showDocumentList(View view) {

         if(documentLayout.getVisibility()==View.VISIBLE){
             documentLayout.setVisibility(View.GONE);
         }
         else{
             documentLayout.setVisibility(View.VISIBLE);
         }
    }
}