package com.example.you_id_fix;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageDownloadFileOptions;
import com.amplifyframework.storage.options.StorageListOptions;
import com.amplifyframework.storage.options.StorageRemoveOptions;
import com.amplifyframework.storage.options.StorageUploadFileOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;

public class EditImage extends AppCompatActivity implements Serializable {
    private static final String TAG = "Permissions";
    String fullName, email, profilePic, type, selectedDocument, selectedDocumentURL;
    EditText pictureName;
    ImageView thisImage, backArrow;
    Button btnRename, btnUploadNew, btnDelete;
    Uri image_uri;
    File thisFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
            type = extras.getString("type");
            selectedDocument = extras.getString("thisDocument");
            selectedDocumentURL = extras.getString("thisDocumentURL");
        }

        pictureName = findViewById(R.id.editTextPictureName);
        thisImage = findViewById(R.id.imageViewEditPicture);
        btnRename = findViewById(R.id.buttonImageRename);
        btnUploadNew = findViewById(R.id.buttonImageUpdate);
        btnDelete = findViewById(R.id.buttonImageDelete);
        backArrow = findViewById(R.id.backArrow);

        Picasso.get().load(selectedDocumentURL).into(thisImage);

        if (type.equals("health") || type.equals("travel"))
            pictureName.setText(selectedDocument.substring(7));
        if (type.equals("legal"))
            pictureName.setText(selectedDocument.substring(6));
        if (type.equals("merchant"))
            pictureName.setText(selectedDocument.substring(9));

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        StorageUploadFileOptions uploadOptions = StorageUploadFileOptions.builder()
                .accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageDownloadFileOptions downloadOptions = StorageDownloadFileOptions.builder()
                .accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageRemoveOptions URLOptions2 = StorageRemoveOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageListOptions URLOptions3 = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        btnRename.setOnClickListener(v -> Amplify.Storage.downloadFile(
                                            selectedDocument,
                                            thisFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "placeholder" +".jpg"),
                                            downloadOptions,
                                            result -> {Log.i("Download File", "Successfully downloaded: " + result.getFile().getName());
                                                String renamedKey = type + "\\" + pictureName.getText().toString();
                                                Amplify.Storage.uploadFile(
                                                        renamedKey,
                                                        thisFile,
                                                        uploadOptions,
                                                        result2 -> {Log.i("MyAmplifyApp", "Successfully uploaded: " + result2.getKey());
                                                            thisFile.delete();
                                                            Amplify.Storage.remove(
                                                                    selectedDocument, URLOptions2,
                                                                    result3 -> {Log.i("Remove Image", "Successfully removed: " + result3.getKey());
                                                                        if (type.equals("health")) {
                                                                            Intent intent = new Intent(getApplicationContext(), Health.class);
                                                                            intent.putExtra("thisName", fullName);
                                                                            intent.putExtra("thisEmail", email);
                                                                            intent.putExtra("thisProfilePic", profilePic);
                                                                            startActivity(intent);
                                                                            runOnUiThread(() -> Toast.makeText(EditImage.this, "Renamed the file successfully.", Toast.LENGTH_SHORT).show());
                                                                        }
                                                                        if (type.equals("legal")) {
                                                                            Intent intent = new Intent(getApplicationContext(), Legal.class);
                                                                            intent.putExtra("thisName", fullName);
                                                                            intent.putExtra("thisEmail", email);
                                                                            intent.putExtra("thisProfilePic", profilePic);
                                                                            startActivity(intent);
                                                                            runOnUiThread(() -> Toast.makeText(EditImage.this, "Renamed the file successfully.", Toast.LENGTH_SHORT).show());
                                                                        }
                                                                        if (type.equals("travel")) {
                                                                            Intent intent = new Intent(getApplicationContext(), Travel.class);
                                                                            intent.putExtra("thisName", fullName);
                                                                            intent.putExtra("thisEmail", email);
                                                                            intent.putExtra("thisProfilePic", profilePic);
                                                                            startActivity(intent);
                                                                            runOnUiThread(() -> Toast.makeText(EditImage.this, "Renamed the file successfully.", Toast.LENGTH_SHORT).show());
                                                                        }
                                                                        if (type.equals("merchant")) {
                                                                            Intent intent = new Intent(getApplicationContext(), Merchant.class);
                                                                            intent.putExtra("thisName", fullName);
                                                                            intent.putExtra("thisEmail", email);
                                                                            intent.putExtra("thisProfilePic", profilePic);
                                                                            startActivity(intent);
                                                                            runOnUiThread(() -> Toast.makeText(EditImage.this, "Renamed the file successfully.", Toast.LENGTH_SHORT).show());
                                                                        }
                                                                    },
                                                                    error2 -> Log.e("Remove Image", "Remove failure", error2)
                                                            );
                                                        },
                                                        storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
                                                );
                                            },
                                            error -> Log.e("Download File",  "Download Failure", error)
                                    ));

        btnUploadNew.setOnClickListener(v -> Amplify.Storage.list("", URLOptions3,
                result -> {
                    for (StorageItem item : result.getItems()) {
                        if (item.getKey().equals(selectedDocument)) {
                            Amplify.Storage.remove(
                                    selectedDocument, URLOptions2,
                                    result2 -> Log.i("Remove Image", "Successfully removed: " + result2.getKey()),
                                    error2 -> Log.e("Remove Image", "Remove failure", error2)
                            );
                        }
                    }
                    PickImageFromGallery();
                },
                error -> Log.e("List Picture", "Something went wrong", error)
        ));

        btnDelete.setOnClickListener(v -> Amplify.Storage.list("", URLOptions3,
                result -> {
                    for (StorageItem item : result.getItems()) {
                        if (item.getKey().equals(selectedDocument)) {
                            Amplify.Storage.remove(
                                    selectedDocument, URLOptions2,
                                    result2 -> {Log.i("Remove Image", "Successfully removed: " + result2.getKey());
                                        if (type.equals("health")) {
                                            Intent intent = new Intent(getApplicationContext(), Health.class);
                                            intent.putExtra("thisName", fullName);
                                            intent.putExtra("thisEmail", email);
                                            intent.putExtra("thisProfilePic", profilePic);
                                            startActivity(intent);
                                            runOnUiThread(() -> Toast.makeText(EditImage.this, "Removed " + selectedDocument.substring(7) + " successfully.", Toast.LENGTH_SHORT).show());
                                        }
                                        if (type.equals("legal")) {
                                            Intent intent = new Intent(getApplicationContext(), Legal.class);
                                            intent.putExtra("thisName", fullName);
                                            intent.putExtra("thisEmail", email);
                                            intent.putExtra("thisProfilePic", profilePic);
                                            startActivity(intent);
                                            runOnUiThread(() -> Toast.makeText(EditImage.this, "Removed " + selectedDocument.substring(6) + " successfully.", Toast.LENGTH_SHORT).show());
                                        }
                                        if (type.equals("travel")) {
                                            Intent intent = new Intent(getApplicationContext(), Travel.class);
                                            intent.putExtra("thisName", fullName);
                                            intent.putExtra("thisEmail", email);
                                            intent.putExtra("thisProfilePic", profilePic);
                                            startActivity(intent);
                                            runOnUiThread(() -> Toast.makeText(EditImage.this, "Removed " + selectedDocument.substring(7) + " successfully.", Toast.LENGTH_SHORT).show());
                                        }
                                        if (type.equals("merchant")) {
                                            Intent intent = new Intent(getApplicationContext(), Merchant.class);
                                            intent.putExtra("thisName", fullName);
                                            intent.putExtra("thisEmail", email);
                                            intent.putExtra("thisProfilePic", profilePic);
                                            startActivity(intent);
                                            runOnUiThread(() -> Toast.makeText(EditImage.this, "Removed " + selectedDocument.substring(9) + " successfully.", Toast.LENGTH_SHORT).show());
                                        }},
                                    error2 -> Log.e("Remove Image", "Remove failure", error2)
                            );
                        }
                    }
                },
                error -> Log.e("List Picture", "Something went wrong", error)
        ));


        backArrow.setOnClickListener(v -> {
            if (type.equals("health")) {
                Intent intent = new Intent(getApplicationContext(), Health.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
            if (type.equals("legal")) {
                Intent intent = new Intent(getApplicationContext(), Legal.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
            if (type.equals("travel")) {
                Intent intent = new Intent(getApplicationContext(), Travel.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
            if (type.equals("merchant")) {
                Intent intent = new Intent(getApplicationContext(), Merchant.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", email);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                }

            case 3:
                Log.d(TAG, "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                }
        }
    }

    public void isReadStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission is granted1");
        } else {

            Log.v(TAG, "Permission is revoked1");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
    }

    public void isWriteStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission is granted2");
        } else {

            Log.v(TAG, "Permission is revoked2");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    private void uploadFile(String uri) {
        File file = new File(uri); // image path

        try {
            StorageUploadFileOptions options = StorageUploadFileOptions.builder()
                    .accessLevel(StorageAccessLevel.PRIVATE)
                    .build();

            Amplify.Storage.uploadFile(selectedDocument, file, options,
                    progress -> Log.i("uploadFile", "Fraction completed: " + progress.getFractionCompleted()),
                    result -> Log.i("uploadFile", "Successfully uploaded: " + selectedDocument),
                    error -> Log.e("uploadFile", "Upload failed", error)

            );
        } catch (Exception exception) {
            Log.e("UploadFIle", "Upload failed", exception);
        }

    }

    private void PickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //noinspection deprecation
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 2) {
            try {
                assert data != null;
                image_uri = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(image_uri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);

                c.close();

                uploadFile(picturePath);

                if (type.equals("health")) {
                    Intent intent = new Intent(getApplicationContext(), Health.class);
                    intent.putExtra("thisName", fullName);
                    intent.putExtra("thisEmail", email);
                    intent.putExtra("thisProfilePic", profilePic);
                    startActivity(intent);
                }
                if (type.equals("legal")) {
                    Intent intent = new Intent(getApplicationContext(), Legal.class);
                    intent.putExtra("thisName", fullName);
                    intent.putExtra("thisEmail", email);
                    intent.putExtra("thisProfilePic", profilePic);
                    startActivity(intent);
                }
                if (type.equals("travel")) {
                    Intent intent = new Intent(getApplicationContext(), Travel.class);
                    intent.putExtra("thisName", fullName);
                    intent.putExtra("thisEmail", email);
                    intent.putExtra("thisProfilePic", profilePic);
                    startActivity(intent);
                }
                if (type.equals("merchant")) {
                    Intent intent = new Intent(getApplicationContext(), Merchant.class);
                    intent.putExtra("thisName", fullName);
                    intent.putExtra("thisEmail", email);
                    intent.putExtra("thisProfilePic", profilePic);
                    startActivity(intent);
                }

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
}
