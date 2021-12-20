package com.example.you_id_fix;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageListOptions;
import com.amplifyframework.storage.options.StorageUploadFileOptions;


import java.io.File;
import java.io.Serializable;


public class UploadIdentification extends AppCompatActivity implements Serializable {
    private static final String TAG = "Permissions";
    ImageView imageview;
    Button btn_imageSelection, btn_Skip;
    Uri image_uri;
    String imageString, picturePath, fullName, email, profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_identification);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        imageview = findViewById(R.id.imageView2);
        btn_imageSelection = findViewById(R.id.btn_imageUpload);
        btn_Skip = findViewById(R.id.btn_Skip);

        //Storage permission - Device
        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        imageview.setOnClickListener(v-> PickImageFromGallery());

        btn_imageSelection.setOnClickListener(v -> {
            if (picturePath == null){
                Toast.makeText(UploadIdentification.this, "You must have a picture selected.", Toast.LENGTH_SHORT).show();
            }
            else {
                uploadFile(picturePath);
            }});

        btn_Skip.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
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

    private void uploadFile(String uri) {

        Log.i("UploadFile Method", "Setting folder name");

        String folder = "merchant";

        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(getApplicationContext());
        // Generating random number to add in image name
        imageString = "identification";
        imageString = folder + "\\" + imageString;
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

                        if (imageString.equals(item.getKey())) {
                            Toast.makeText(getApplicationContext(), "Identification image already exist!", Toast.LENGTH_LONG).show();


                            // Dialogbox visuals
                            confirmationDialog.setTitle("Image Already Exist");
                            confirmationDialog.setMessage("Please Select Yes To Upload A New Identification Image");

                            confirmationDialog.setPositiveButton("Yes", (dialog, which) -> {

                            });

                            confirmationDialog.setNegativeButton("Cancel", (dialog, which) -> {
                            });
                            confirmationDialog.create().show();




                        }
                    }

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
                                Toast.makeText(UploadIdentification.this, "Image upload successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainMenu.class));
                                // Clearing the selection for previously selected radio button

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