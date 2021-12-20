package com.example.you_id_fix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageDownloadFileOptions;
import com.amplifyframework.storage.options.StorageListOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BucketImageList extends AppCompatActivity {

    ArrayList<String> files = new ArrayList<>();
    ArrayList<String> imageUrls = new ArrayList<>();
    ArrayList<String> imageFilesList = new ArrayList<>();
    RecyclerView recyclerView;
    Button btnRetrieve, btn_downloadAll;
    String folder, getFolder, fullName, email, profilePic;
    TextView navFirstName, navEmail;
    CircleImageView navProfilePic;
    NavigationView navView;
    DrawerLayout drawer;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_image_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        recyclerView = findViewById(R.id.recycleView_image_list);
        btnRetrieve = findViewById(R.id.btn_retrieveImages);
        btn_downloadAll = findViewById(R.id.btn_downloadImages);
        navView = findViewById(R.id.nav_view_download);
        drawer = findViewById(R.id.drawerLayoutDownload);
        backArrow = findViewById(R.id.backArrow);

        navView.setCheckedItem(R.id.navDownload);
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
            DrawerLayout drawer = findViewById(R.id.drawerLayoutDownload);
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

        folder = "";

        Intent intentReceiver = getIntent();
        getFolder = intentReceiver.getStringExtra("folder");

        if (!TextUtils.isEmpty(getFolder))
        {
            folder = getFolder;
        }
        // .accessLevel(StorageAccessLevel.PRIVATE)
        StorageListOptions options = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.list(
                "",
                options,
                result -> {

                    Log.i("Getting List of Images", "From s3 Bucket:");
                    for (StorageItem item : result.getItems()) {
                        Log.i("List Files Function", "Image Key = Item: " + item.getKey());
                        // adding images/files detail like size name etc in the files in Files Array
                       // imageFilesList.add(item.getKey() + " - " + item.getSize() / 1000 + " KB");
                        runOnUiThread(() -> imageFilesList.add(item.getKey()));
                        // adding images/files name in Files Array [getKey() = image name]
                        runOnUiThread(() -> files.add(item.getKey()));
                    }
                }, error -> Log.e("Storage URL", "URL generation failure", error) );


        // Setting images name from list to recycler view
        btnRetrieve.setOnClickListener( v -> {
            recyclerAdapter adapter = new recyclerAdapter(this,imageFilesList, imageUrls, BucketImageList.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });

        // Download All Button - Downloading all files
        btn_downloadAll.setOnClickListener( v -> downloadFiles());

        backArrow.setOnClickListener( v -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });
    }

    private void downloadFiles() {

        StorageDownloadFileOptions options = StorageDownloadFileOptions.builder()
                .accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        // Download folder path
        // File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        for (String item : files)
        {
            String revisedString = null;

            if (item.startsWith("travel"))
            {
                revisedString = item.substring(7);
            }
            if (item.startsWith("health"))
            {
                revisedString = item.substring(7);
            }
            if (item.startsWith("legal"))
            {
                revisedString = item.substring(6);
            }
            if (item.startsWith("merchant"))
            {
                revisedString = item.substring(9);
            }
            if (item.startsWith("contact_ICE"))
            {
                revisedString = item.substring(12);
            }

            Amplify.Storage.downloadFile(
                    // Each file/image name in files array
                    item,
                    // Downloading files in download folder with different names and .jpg extension
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"+ revisedString +".jpg"),

                    // using access level here [Public, Private, Protected] which we set up above
                    options,
                    progress -> Log.i("downloadFiles", "Fraction completed: " + progress.getFractionCompleted()),
                    result -> {
                        Log.i("downloadFiles", "Successfully downloaded: " + result.getFile().getName());
                        //DataS3 ileobj = new DataS3(result.getFile().getAbsolutePath(), result.getFile().getName(), item);
                        Log.i("getResult : Path", result.getFile().getAbsolutePath() + " key: " + result.getFile().getName());

                    },
                    error -> Log.e("downloadFiles", "Download Failure", error)
            );
        }
    }

}