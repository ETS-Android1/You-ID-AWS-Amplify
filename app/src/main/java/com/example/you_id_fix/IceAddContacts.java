package com.example.you_id_fix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Contacts;
import com.amplifyframework.datastore.generated.model.UserInfo;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageGetUrlOptions;
import com.amplifyframework.storage.options.StorageListOptions;
import com.amplifyframework.storage.options.StorageRemoveOptions;
import com.amplifyframework.storage.options.StorageUploadFileOptions;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IceAddContacts extends AppCompatActivity implements Serializable {
    public static final String TAG ="LOG";
    String contactType, userID, loggedInEmail, title, firstName, lastName, address, thisEmail, phone, fullName, thisUserEmail, profilePic, imageString, picturePath, ice_contact;
    EditText contactTitle, contactFirstName, contactLastName, contactProfession, contactAddress, contactEmail, contactPhone;
    ImageView iv_contactImage, backArrow;
    Button submitContact;
    Uri image_uri;
    boolean hasPicture = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_add_contacts);

        submitContact = findViewById(R.id.buttonSubmitContactInfo);
        contactTitle = findViewById(R.id.contactTitle);
        contactFirstName = findViewById(R.id.editTextContactFirstName);
        contactLastName = findViewById(R.id.editTextContactLastName);
        contactProfession = findViewById(R.id.editTextProfession);
        contactAddress = findViewById(R.id.editTextContactAddress);
        contactEmail = findViewById(R.id.editTextContactEmail);
        contactPhone = findViewById(R.id.editTextContactPhone);
        backArrow = findViewById(R.id.backArrow);
        iv_contactImage = findViewById(R.id.IV_contactImage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            contactType = extras.getString("type");
            fullName = extras.getString("thisName");
            thisUserEmail = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
            ice_contact = extras.getString("contact");
        }

        Amplify.DataStore.query(
                UserInfo.class,
                Where.matches(UserInfo.EMAIL.eq(loggedInEmail)),
                Users -> {
                    while (Users.hasNext()) {
                        UserInfo todo = Users.next();
                        userID = todo.getId();
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Something went wrong with fetching user ID.", failure)
        );

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        iv_contactImage.setOnClickListener(v-> PickImageFromGallery());

        submitContact.setOnClickListener(v -> {
            title = contactTitle.getText().toString();
            firstName = contactFirstName.getText().toString();
            lastName = contactLastName.getText().toString();
            address = contactAddress.getText().toString();
            thisEmail = contactEmail.getText().toString();
            phone = contactPhone.getText().toString();

            // Validating user inputs
            if (TextUtils.isEmpty(title)) {
                contactTitle.setError("Image Title Required.");
                return;
            }
            if (TextUtils.isEmpty(firstName)) {
                contactFirstName.setError("First Name Required.");
                return;
            }
            if (firstName.length() < 3 || firstName.length() > 30) {
                contactFirstName.setError("First Name not valid!");
                return;
            }
            if (TextUtils.isEmpty(lastName)) {
                contactLastName.setError("Last Name Required.");
                return;
            }
            if (TextUtils.isEmpty(address)) {
                contactAddress.setError("Address Required.");
                return;
            }
            if (address.length() < 3) {
                contactAddress.setError("Address not valid!");
                return;
            }
            if (TextUtils.isEmpty(thisEmail)) {
                contactEmail.setError("Email Required.");
                return;
            }
            if (!(thisEmail.contains("@")) || !(thisEmail.contains(".com"))) {
                contactEmail.setError("Invalid Email!");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                contactPhone.setError("Phone Number Required.");
                return;
            }
            if (!isValidPhone(phone)) {
                contactPhone.setError("Invalid Phone Number!");
                return;
            }

            uploadFile(picturePath);
        });

        backArrow.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), ICEActivity.class);
                intent.putExtra("thisName", fullName);
                intent.putExtra("thisEmail", thisUserEmail);
                intent.putExtra("thisProfilePic", profilePic);
                startActivity(intent);
        });
    }

    private void storeContactData(String firstname, String lastname, String profession, String email, String phone, String address, String type, String imgURL) {
        // Setting time variable
        Date date = new Date();
        int offsetMillis = TimeZone.getDefault().getOffset(date.getTime());
        int offsetSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(offsetMillis);
        Temporal.DateTime temporalDateTime = new Temporal.DateTime(date, offsetSeconds);

        //Creating new object in our database
        Contacts contact = Contacts.builder()
                .firstname(firstname)
                .lastname(lastname)
                .profession(profession)
                .email(email)
                .phone(phone)
                .address(address)
                .type(type)
                .userId(Amplify.Auth.getCurrentUser().getUserId())
                .completedAt(temporalDateTime)
                .imageUrl(imgURL)
                .build();

        Amplify.DataStore.save(
                contact,
                success -> {Log.i("Contact Table", "Saved item: " + success.item().getId());
                    Intent intent = new Intent(getApplicationContext(), ICEActivity.class);
                    intent.putExtra("thisName", fullName);
                    intent.putExtra("thisEmail", thisUserEmail);
                    intent.putExtra("thisProfilePic", profilePic);
                    intent.putExtra("cProfession", profession);
                    intent.putExtra("cEmail", email);
                    intent.putExtra("cAddress", address);
                    intent.putExtra("cPhone", phone);
                    intent.putExtra("cID", contact.getId());
                    intent.putExtra("cFirst", firstname);
                    intent.putExtra("cLast", lastname);
                    intent.putExtra("cName", firstname + " " + lastname);
                    intent.putExtra("prevType", ice_contact);
                    intent.putExtra("update","y");
                    startActivity(intent);},
                error -> Log.e("Contact Table", "Could not save item to DataStore", error)
        );
    }

    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9+]|\\(\\d{1,3}\\))[0-9\\-. ]{3,15}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    // Image Upload Methods
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
        imageString = "contact" + "_" + ice_contact;

        if (TextUtils.isEmpty(uri))
        {
            storeContactData(firstName, lastName, title, thisEmail, phone, address, ice_contact, null);
        }

        StorageListOptions options = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();
        StorageRemoveOptions URLOptions2 = StorageRemoveOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.list(
                "",
                options,
                result -> {
                    Log.i("Getting List of Images", "From s3 Bucket:");
                    for (StorageItem item : result.getItems()) {
                        Log.i("Checking Images Names", "Image Key = Item: " + item.getKey());
                        if (imageString.equals(item.getKey())) {
                            Amplify.Storage.remove(
                                    item.getKey(), URLOptions2,
                                    result2 -> Log.i("Contact Image", "Successfully removed: " + result2.getKey()),
                                    error2 -> Log.e("Contact Image", "Remove failure", error2)
                            );
                        }
                    }
                    File file = new File(uri); // image path

                    // Accessing and setting access level = Private
                    StorageUploadFileOptions uploadOptions = StorageUploadFileOptions.builder()
                            .accessLevel(StorageAccessLevel.PRIVATE)
                            .build();
                    StorageGetUrlOptions URLOptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                            .build();

                    // Amplify code for image/file upload
                    Amplify.Storage.uploadFile(imageString, file, uploadOptions,
                            progress -> Log.i("uploadFile", "Fraction completed: " + progress.getFractionCompleted()),
                            finish -> {
                                Log.i("uploadFile", "Successfully uploaded: " + imageString);
                                iv_contactImage.setImageResource(R.drawable.upload_images);
                                Amplify.Storage.getUrl(
                                        finish.getKey(),
                                        URLOptions,
                                        find -> {
                                            Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                            storeContactData(firstName, lastName, title, thisEmail, phone, address, ice_contact, find.getUrl().toString());
                                        },
                                        error -> Log.e("Storage URL", "URL generation failure", error));
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
                iv_contactImage.setImageBitmap(thumbnail);

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