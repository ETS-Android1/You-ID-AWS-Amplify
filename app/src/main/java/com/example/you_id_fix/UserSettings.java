package com.example.you_id_fix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageGetUrlOptions;
import com.amplifyframework.storage.options.StorageListOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettings extends AppCompatActivity implements Serializable {
    CardView cardViewProfilePic, cardViewChangePass, cardViewChangeEmail, cardViewChangeUserInfo, cardViewGoIdentification;
    ImageView backArrow;
    String fullName, email, profilePic;
    TextView navFirstName, navEmail, currentlyLoggedInEmail;
    CircleImageView navProfilePic, currentProfile;
    NavigationView navView;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
        }

        currentlyLoggedInEmail = findViewById(R.id.userSettingsSignedInEmail);
        currentProfile = findViewById(R.id.userSettingsProfilePic);
        cardViewProfilePic = findViewById(R.id.cardViewUploadProfile);
        cardViewChangePass = findViewById(R.id.cardViewChangePassword);
        cardViewChangeEmail = findViewById(R.id.cardViewChangeEmail);
        cardViewChangeUserInfo = findViewById(R.id.cardViewUserInfo);
        cardViewGoIdentification = findViewById(R.id.cardViewIdentification);
        backArrow = findViewById(R.id.backArrow);
        navView = findViewById(R.id.nav_view_user_settings);
        drawer = findViewById(R.id.drawerLayoutUserSettings);

        View header = navView.getHeaderView(0);
        navFirstName = header.findViewById(R.id.nav_header_first_name);
        navEmail = header.findViewById(R.id.nav_header_email);
        navProfilePic = header.findViewById(R.id.nav_header_picture);

        navFirstName.setText(fullName);
        navEmail.setText(email);
        currentlyLoggedInEmail.setText(email);

        StorageGetUrlOptions URLOptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageListOptions URLOptions2 = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.list("", URLOptions2,
                result -> {
                    for (StorageItem item : result.getItems()) {
                        if (item.getKey().equals("profile-picture")) {
                            Amplify.Storage.getUrl(
                                    "profile-picture",
                                    URLOptions,
                                    result2 -> {
                                        Log.i("Profile Picture", "Successfully generated: " + result2.getUrl());
                                        new Handler(Looper.getMainLooper()).post(() -> {
                                            Picasso.get().load(result2.getUrl().toString()).into(navProfilePic);
                                            Picasso.get().load(result2.getUrl().toString()).into(currentProfile);
                                            profilePic = result2.getUrl().toString();
                                        });
                                    },
                                    error2 -> Log.e("Profile Picture", "No Profile Picture Set", error2)
                            );
                        }
                    }
                },
                error -> Log.e("Profile Picture", "Something went wrong", error)
        );

        cardViewProfilePic.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UploadProfilePicture.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        cardViewChangePass.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        cardViewChangeEmail.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChangeEmail.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        cardViewChangeUserInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChangeUserInfo.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        cardViewGoIdentification.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UploadIdentification.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        navView.setCheckedItem(R.id.navSettings);
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
            }else if (id == R.id.navSettings) {
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
            DrawerLayout drawer = findViewById(R.id.drawerLayoutUserSettings);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}