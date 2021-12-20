package com.example.you_id_fix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageListOptions;
import com.amplifyframework.storage.options.StorageUploadFileOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Upload extends AppCompatActivity {

    private static final String TAG = "Permissions";
    ImageView imageview, backArrow;
    Button btn_imageSelection, buttonGoToImageList;
    Uri image_uri;
    EditText imageName;
    String imageString, picturePath, fullName, email, profilePic;
    RadioGroup radioGroup;
    RadioButton radioHealth, radioTravel, radioLegal, radioMerchant, btn_select;
    TextView navFirstName, navEmail;
    CircleImageView navProfilePic;
    NavigationView navView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        navView = findViewById(R.id.nav_view_upload);
        drawer = findViewById(R.id.drawerLayoutUpload);
        radioGroup = findViewById(R.id.radiogroupbuttons);
        imageview = findViewById(R.id.imageView2);
        btn_imageSelection = findViewById(R.id.btn_imageUpload);
        buttonGoToImageList = findViewById(R.id.btn_gotoImageList);
        imageName = findViewById(R.id.imageNameInput);
        radioHealth  = findViewById(R.id.radioButtonHealth);
        radioTravel= findViewById(R.id.radioButtonTravel);
        radioLegal= findViewById(R.id.radioButtonLegal);
        radioMerchant= findViewById(R.id.radioButtonMerchant);
        backArrow = findViewById(R.id.backArrow);

        navView.setCheckedItem(R.id.navUpload);
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
            DrawerLayout drawer = findViewById(R.id.drawerLayoutUpload);
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

        //Storage permission - Device
        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        buttonGoToImageList.setOnClickListener( v -> {
            Intent intent = new Intent(this, BucketImageList.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        backArrow.setOnClickListener( v -> {
            Intent intent = new Intent(this, MainMenu.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        imageview.setOnClickListener(v-> PickImageFromGallery());

        btn_imageSelection.setOnClickListener(v -> uploadFile(picturePath));
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

    private void uploadFile(String uri) {

        Log.i("UploadFile Method", "Checking Radio Button");
        // Checking radio button selection
        int radioID = radioGroup.getCheckedRadioButtonId();
        btn_select = findViewById(radioID);
        String folder = btn_select.getText().toString().toLowerCase(Locale.ROOT);
        Log.i("Folder Name", "Got folder name:");

        // Generating random number to add in image name
        Random randomNumber  = new Random();
        imageString = imageName.getText().toString().trim();


        if (TextUtils.isEmpty(uri))
        {
            Toast.makeText(this, "Error: No Image selected", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(imageString) || imageString.length() < 5)
        {
            imageName.setError("invalid image name!");
            return;
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
                        Log.i("Checking Images Names", "Image Key = Item: " + item.getKey());

                        if ((folder + "\\" + imageName.getText().toString()).equals(item.getKey())) {
                            runOnUiThread(() -> imageName.setError("An image with this name already exists"));
                            return;
                        }
                    }
                    imageString = folder + "\\" + imageString;
                    Log.i("Folder / Image Name", "Image Name = " + imageString);
                    final String key = imageString; // image name for uploading image
                    File file = new File(uri); // image path

                    // Accessing and setting access level = Private
                    StorageUploadFileOptions uploadOptions = StorageUploadFileOptions.builder()
                            .accessLevel(StorageAccessLevel.PRIVATE)
                            .build();

                    // Amplify code for image/file upload
                    Amplify.Storage.uploadFile(key, file, uploadOptions,
                            progress -> Log.i("uploadFile", "Fraction completed: " + progress.getFractionCompleted()),
                            finish -> {
                                Log.i("uploadFile", "Successfully uploaded: " + key);
                                Toast.makeText(Upload.this, "Image upload successfully", Toast.LENGTH_LONG).show();
                                imageName.setText("");
                                imageview.setImageResource(R.drawable.upload_images);
                                // Clearing the selection for previously selected radio button
                                radioGroup.clearCheck();
                            },
                            error -> Log.e("uploadFile", "Upload failed", error));

                }, error -> Log.e("Checking Images Names", "Checking Images Names Failure", error));

    }

    private void PickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 2) {
            try {
                assert data != null;
                image_uri = data.getData();

                //    File file = new File(image_uri.getPath());
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(image_uri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);

                c.close();

                // Setting thumbnail
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = getResizedBitmap(thumbnail, 400);
                Log.w("Image path", picturePath + "");
                imageview.setImageBitmap(thumbnail);

            } catch (Exception e)  {
                e.printStackTrace();
                Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {

            Log.e("onActivityResult", "Image Upload Cancelled");
        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void isReadStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is granted1");
        } else {

            Log.v(TAG,"Permission is revoked1");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
    }

    public void isWriteStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is granted2");
        } else {

            Log.v(TAG,"Permission is revoked2");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }
}