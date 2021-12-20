package com.example.you_id_fix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.UserInfo;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageGetUrlOptions;
import com.amplifyframework.storage.options.StorageListOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenu extends AppCompatActivity implements Serializable {
    String displayEmail, userID, picture;
    TextView loggedInEmail, navFirstName, navEmail;
    CircleImageView profilePicture;
    CardView cardViewHealth, cardViewLegal, cardViewMerchant, cardViewTravel, cardViewUpload, cardViewDownload, cardViewICE, cardViewSettings, cardViewSupport, cardViewSignOut;
    Toolbar toolbar;
    CircleImageView navProfilePic;
    NavigationView navView;
    DrawerLayout drawer;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = findViewById(R.id.app_Bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);

        loggedInEmail = findViewById(R.id.homePageSignedInEmail);
        profilePicture = findViewById(R.id.homePageProfilePic);
        cardViewHealth = findViewById(R.id.cardViewHealth);
        cardViewLegal = findViewById(R.id.cardViewLegal);
        cardViewMerchant = findViewById(R.id.cardViewMerchant);
        cardViewTravel = findViewById(R.id.cardViewTravel);
        cardViewUpload = findViewById(R.id.cardViewUpload);
        cardViewDownload = findViewById(R.id.cardViewDownload);
        cardViewICE = findViewById(R.id.cardViewICE);
        cardViewSettings = findViewById(R.id.cardViewSettings);
        cardViewSupport = findViewById(R.id.cardViewSupport);
        cardViewSignOut = findViewById(R.id.cardViewSignOut);
        navView = findViewById(R.id.nav_view_main_menu);
        drawer = findViewById(R.id.drawerLayoutHome);

        View header = navView.getHeaderView(0);
        navFirstName = header.findViewById(R.id.nav_header_first_name);
        navEmail = header.findViewById(R.id.nav_header_email);
        navProfilePic = header.findViewById(R.id.nav_header_picture);

        displayEmail = Amplify.Auth.getCurrentUser().getUsername();
        loggedInEmail.setText(displayEmail);
        navEmail.setText(displayEmail);

        userID = Amplify.Auth.getCurrentUser().getUserId();

        Amplify.DataStore.query(
                UserInfo.class,
                Where.matches(UserInfo.EMAIL.eq(displayEmail)),
                Users -> {
                    while (Users.hasNext()) {
                        UserInfo user = Users.next();
                        Log.i("MyAmplifyApp", "User: " +  user);
                        navFirstName.setText(user.getFirstname() + " " + user.getLastname());
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
        );

        StorageGetUrlOptions StorageOptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageListOptions URLOptions2 = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.list("", URLOptions2,
                result -> {
                    for (StorageItem item : result.getItems()) {
                        if (item.getKey().equals("profile-picture")) {
                            Amplify.Storage.getUrl(
                                    "profile-picture",
                                    StorageOptions,
                                    result2 -> {
                                        Log.i("Profile Picture", "Successfully generated: " + result2.getUrl());
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            Picasso.get().load(result2.getUrl().toString()).into(profilePicture);
                                            Picasso.get().load(result2.getUrl().toString()).into(navProfilePic);
                                            picture = result2.getUrl().toString();
                                        });
                                    },
                                    error2 -> Log.e("Profile Picture", "No Profile Picture Set", error2)
                            );
                        }
                    }
                },
                error -> Log.e("Profile Picture", "Something went wrong", error)
        );

        cardViewHealth.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Health.class);
            intent.putExtra("thisName", navFirstName.getText().toString());
            intent.putExtra("thisEmail", navEmail.getText().toString());
            intent.putExtra("thisProfilePic", picture);
            startActivity(intent);
        });

        cardViewLegal.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Legal.class);
            intent.putExtra("thisName", navFirstName.getText().toString());
            intent.putExtra("thisEmail", navEmail.getText().toString());
            intent.putExtra("thisProfilePic", picture);
            startActivity(intent);
        });

        cardViewMerchant.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Merchant.class);
            intent.putExtra("thisName", navFirstName.getText().toString());
            intent.putExtra("thisEmail", navEmail.getText().toString());
            intent.putExtra("thisProfilePic", picture);
            startActivity(intent);
        });

        cardViewTravel.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Travel.class);
            intent.putExtra("thisName", navFirstName.getText().toString());
            intent.putExtra("thisEmail", navEmail.getText().toString());
            intent.putExtra("thisProfilePic", picture);
            startActivity(intent);
        });

        cardViewUpload.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Upload.class);
            intent.putExtra("thisName", navFirstName.getText().toString());
            intent.putExtra("thisEmail", navEmail.getText().toString());
            intent.putExtra("thisProfilePic", picture);
            startActivity(intent);
        });

        cardViewDownload.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), BucketImageList.class);
            intent.putExtra("thisName", navFirstName.getText().toString());
            intent.putExtra("thisEmail", navEmail.getText().toString());
            intent.putExtra("thisProfilePic", picture);
            startActivity(intent);
        });

        cardViewICE.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ICEActivity.class);
            intent.putExtra("thisName", navFirstName.getText().toString());
            intent.putExtra("thisEmail", navEmail.getText().toString());
            intent.putExtra("thisProfilePic", picture);
            startActivity(intent);
        });

        cardViewSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserSettings.class);
            intent.putExtra("thisName", navFirstName.getText().toString());
            intent.putExtra("thisEmail", navEmail.getText().toString());
            intent.putExtra("thisProfilePic", picture);
            startActivity(intent);
        });

        cardViewSupport.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Support.class);
            intent.putExtra("thisName", navFirstName.getText().toString());
            intent.putExtra("thisEmail", navEmail.getText().toString());
            intent.putExtra("thisProfilePic", picture);
            startActivity(intent);
        });

        cardViewSignOut.setOnClickListener(v -> Amplify.Auth.signOut(
                () -> {
                    Log.i("AuthQuickstart", "Signed out successfully");
                    startActivity(new Intent(getApplicationContext(), Login.class));
                },
                error -> Log.e("AuthQuickstart", error.toString())
        ));

        navView.setCheckedItem(R.id.navHome);
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();


            if (id == R.id.navHome) {
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
                startActivity(intent);
            } else if (id == R.id.navUpload) {
                Intent intent = new Intent(getApplicationContext(), Upload.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
                startActivity(intent);
            } else if (id == R.id.navDownload) {
                Intent intent = new Intent(getApplicationContext(), BucketImageList.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
                startActivity(intent);
            } else if (id == R.id.navHealth) {
                Intent intent = new Intent(getApplicationContext(), Health.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
                startActivity(intent);
            } else if (id == R.id.navMerchant) {
                Intent intent = new Intent(getApplicationContext(), Merchant.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
                startActivity(intent);
            } else if (id == R.id.navLegal) {
                Intent intent = new Intent(getApplicationContext(), Legal.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
                startActivity(intent);
            } else if (id == R.id.navTravel) {
                Intent intent = new Intent(getApplicationContext(), Travel.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
                startActivity(intent);
            } else if (id == R.id.navEmergency) {
                Intent intent = new Intent(getApplicationContext(), ICEActivity.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
                startActivity(intent);
            } else if (id == R.id.navSettings) {
                Intent intent = new Intent(getApplicationContext(), UserSettings.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
                startActivity(intent);
            } else if (id == R.id.navSupport) {
                Intent intent = new Intent(getApplicationContext(), Support.class);
                intent.putExtra("thisName", navFirstName.getText().toString());
                intent.putExtra("thisEmail", navEmail.getText().toString());
                intent.putExtra("thisProfilePic", picture);
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
            DrawerLayout drawer = findViewById(R.id.drawerLayoutHome);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            navView.getMenu().getItem(item.getOrder()).setChecked(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
