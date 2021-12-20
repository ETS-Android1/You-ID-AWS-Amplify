package com.example.you_id_fix;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageGetUrlOptions;
import com.amplifyframework.storage.options.StorageListOptions;
import com.amplifyframework.storage.options.StorageRemoveOptions;
import com.amplifyframework.storage.options.StorageUploadFileOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadProfilePicture extends AppCompatActivity implements Serializable {
    private static final String TAG = "Permissions";
    String fullName, email, profilePic;
    Button selectNewProfile, deleteProfile;
    Uri image_uri;
    CircleImageView profilePicture;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        selectNewProfile = findViewById(R.id.buttonChooseNewProfilePicture);
        deleteProfile = findViewById(R.id.buttonDeleteProfileImage);
        profilePicture = findViewById(R.id.currentProfilePicture);
        backArrow = findViewById(R.id.backArrow);

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        StorageGetUrlOptions URLOptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageRemoveOptions URLOptions2 = StorageRemoveOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageListOptions URLOptions3 = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.list("", URLOptions3,
                result -> {
                    for (StorageItem item : result.getItems()) {
                        if (item.getKey().equals("profile-picture")) {
                            Amplify.Storage.getUrl(
                                    "profile-picture",
                                    URLOptions,
                                    result2 -> {
                                        Log.i("Profile Picture", "Successfully generated: " + result2.getUrl());
                                        new Handler(Looper.getMainLooper()).post(() -> Picasso.get().load(result2.getUrl().toString()).into(profilePicture));
                                    },
                                    error2 -> Log.e("Profile Picture", "No Profile Picture Set", error2)
                            );
                        }
                    }
                },
                error -> Log.e("Profile Picture", "Something went wrong", error)
        );

        selectNewProfile.setOnClickListener(v -> Amplify.Storage.list("", URLOptions3,
                result -> {
                    for (StorageItem item : result.getItems()) {
                        if (item.getKey().equals("profile-picture")) {
                            Amplify.Storage.remove(
                                    "profile-picture", URLOptions2,
                                    result2 -> Log.i("Profile Image", "Successfully removed: " + result2.getKey()),
                                    error2 -> Log.e("Profile Image", "Remove failure", error2)
                            );
                        }
                    }
                    PickImageFromGallery();
                },
                error -> Log.e("Profile Picture", "Something went wrong", error)
        ));

        deleteProfile.setOnClickListener(v -> {
            Amplify.Storage.list("", URLOptions3,
                    result -> {
                        for (StorageItem item : result.getItems()) {
                            if (item.getKey().equals("profile-picture")) {
                                Amplify.Storage.remove(
                                        "profile-picture", URLOptions2,
                                        result2 -> {Log.i("Profile Image", "Successfully removed: " + result2.getKey());
                                            profilePic = null;},
                                        error2 -> Log.e("Profile Image", "Remove failure", error2)
                                );
                            }
                        }
                    },
                    error -> Log.e("Profile Picture", "Something went wrong", error)
            );
            finish();
            Intent intent = new Intent(getApplicationContext(), UserSettings.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserSettings.class);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });
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

    private void uploadFile(String uri, String imagename) {
        final String key = imagename; // image name for uploading image
        File file = new File(uri); // image path


        try {

            // Accessing and setting access level = Private
            StorageUploadFileOptions options = StorageUploadFileOptions.builder()
                    .accessLevel(StorageAccessLevel.PRIVATE)
                    .build();

            // Amplify code for image/file upload
            Amplify.Storage.uploadFile(key, file, options,
                    progress -> Log.i("uploadFile", "Fraction completed: " + progress.getFractionCompleted()),
                    result -> Log.i("uploadFile", "Successfully uploaded: " + key),
                    error -> Log.e("uploadFile", "Upload failed", error)

            );
        } catch (Exception exception) {
            Log.e("UploadFIle", "Upload failed", exception);
        }

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
                String picturePath = c.getString(columnIndex);

                c.close();

                // Set image name here
                String imageName = "profile-picture";

                //Calling uploadFile with image uri and image name
                uploadFile(picturePath,imageName);

                // Setting thumbnail
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = getResizedBitmap(thumbnail, 400);
                Log.w("Image path", picturePath + "");
                profilePicture.setImageBitmap(thumbnail);

            } catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == Activity.RESULT_CANCELED)
        {
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
}