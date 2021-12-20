package com.example.you_id_fix;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Contacts;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.options.StorageGetUrlOptions;
import com.amplifyframework.storage.options.StorageListOptions;
import com.amplifyframework.storage.options.StorageRemoveOptions;
import com.amplifyframework.storage.options.StorageUploadFileOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class IceEditContacts extends AppCompatActivity implements Serializable {
    String contactName, contactProfession, contactEmail, contactAddress, contactPhone, contactID, contactFirst, contactLast, type, fullName, email, profilePic, picturePath, imageString;
    EditText ev_first, ev_last, ev_title, ev_address, ev_email, ev_phone;
    CircleImageView editContactPicture;
    Button updateInfo, changePic;
    ImageView backArrow;
    Uri image_uri;
    public static final String TAG ="LOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_edit_contacts);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            contactName = extras.getString("cName");
            contactProfession = extras.getString("cProfession");
            contactEmail = extras.getString("cEmail");
            contactAddress = extras.getString("cAddress");
            contactPhone = extras.getString("cPhone");
            contactID = extras.getString("cID");
            contactFirst = extras.getString("cFirst");
            contactLast = extras.getString("cLast");
            type = extras.getString("cType");
            fullName = extras.getString("thisName");
            email = extras.getString("thisEmail");
            profilePic = extras.getString("thisProfilePic");
        }

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        ev_first = findViewById(R.id.etv_firstname);
        ev_last = findViewById(R.id.etv_lastname);
        ev_title = findViewById(R.id.etv_title);
        ev_address = findViewById(R.id.etv_address);
        ev_email = findViewById(R.id.etv_email);
        ev_phone = findViewById(R.id.etv_phoneNumber);
        editContactPicture = findViewById(R.id.IV_ContactPic);
        updateInfo = findViewById(R.id.btn_ice_contact_update);
        changePic = findViewById(R.id.btn_ice_contact_upload_new);
        backArrow = findViewById(R.id.backArrow);

        // .accessLevel(StorageAccessLevel.PRIVATE)
        StorageListOptions options = StorageListOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        // Access to private files URL
        StorageGetUrlOptions URLOptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.list(
                "",
                options,
                result -> {
                    Log.i("Getting List of Images", "From s3 Bucket:");
                    for (StorageItem item : result.getItems()) {
                        Log.i("List Files function", "Checking for ID image - Image Key = Item: " + item.getKey());
                        if (item.getKey().equals("contact_" + type))
                        {
                            Amplify.Storage.getUrl(
                                    item.getKey(),
                                    URLOptions,
                                    find -> {
                                        Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                        runOnUiThread(() -> Picasso.get().load(find.getUrl().toString()).into(editContactPicture));
                                    },
                                    error -> Log.e("Storage URL", "URL generation failure", error));
                        }
                    }
                } ,error -> Log.e("Storage URL", "URL generation failure", error));

        ev_first.setText(contactFirst);
        ev_last.setText(contactLast);
        ev_title.setText(contactProfession);
        ev_address.setText(contactAddress);
        ev_email.setText(contactEmail);
        ev_phone.setText(contactPhone);

        updateInfo.setOnClickListener(v -> {
            String fName = ev_first.getText().toString();
            String lName = ev_last.getText().toString();
            String title = ev_title.getText().toString();
            String address = ev_address.getText().toString();
            String cMail = ev_email.getText().toString();
            String phoneNum = ev_phone.getText().toString();

            // Validating user inputs
            if (TextUtils.isEmpty(fName)) {
                ev_first.setError("First Name Required.");
                return;
            }
            if (fName.length() < 3 || fName.length() > 30) {
                ev_first.setError("First Name not valid!");
                return;
            }
            if (TextUtils.isEmpty(lName)) {
                ev_last.setError("Last Name Required.");
                return;
            }
            if (TextUtils.isEmpty(title)) {
                ev_title.setError("Last Name Required.");
                return;
            }
            if (TextUtils.isEmpty(address)) {
                ev_address.setError("Address Required.");
                return;
            }
            if (address.length() < 3) {
                ev_address.setError("Address not valid!");
                return;
            }
            if (TextUtils.isEmpty(cMail)) {
                ev_email.setError("Email Required.");
                return;
            }
            if (!(cMail.contains("@")) || !(cMail.contains(".com"))) {
                ev_email.setError("Invalid Email!");
                return;
            }
            if (TextUtils.isEmpty(phoneNum)) {
                ev_phone.setError("Phone Number Required.");
                return;
            }
            if (!isValidPhone(phoneNum)) {
                ev_phone.setError("Invalid Phone Number!");
                return;
            }

            Amplify.DataStore.query(Contacts.class, Where.id(contactID),
                    Contacts -> {
                        if (Contacts.hasNext()) {
                            Contacts original = Contacts.next();
                            Contacts edited = original.copyOfBuilder()
                                    .firstname(fName)
                                    .lastname(lName)
                                    .profession(title)
                                    .email(cMail)
                                    .address(address)
                                    .phone(phoneNum)
                                    .build();
                            Amplify.DataStore.save(edited,
                                    updated -> {Log.i("MyAmplifyApp", "Updated a Contact.");
                                        Intent intent = new Intent(getApplicationContext(), IceDisplayContacts.class);
                                        intent.putExtra("cName", fName + " " + lName);
                                        intent.putExtra("cFirst", fName);
                                        intent.putExtra("cLast", lName);
                                        intent.putExtra("cProfession", title);
                                        intent.putExtra("cEmail", cMail);
                                        intent.putExtra("cAddress", address);
                                        intent.putExtra("cPhone", phoneNum);
                                        intent.putExtra("cID", contactID);
                                        intent.putExtra("cType", type);
                                        intent.putExtra("thisName", fullName);
                                        intent.putExtra("thisEmail", email);
                                        intent.putExtra("thisProfilePic", profilePic);
                                        startActivity(intent);
                                    },
                                    failure -> Log.e("MyAmplifyApp", "Update failed.", failure)
                            );
                        }
                    },
                    failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
            );
        });

        changePic.setOnClickListener(v -> PickImageFromGallery());

        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), IceDisplayContacts.class);
            intent.putExtra("cName", contactName);
            intent.putExtra("cFirst", contactFirst);
            intent.putExtra("cLast", contactLast);
            intent.putExtra("cProfession", contactProfession);
            intent.putExtra("cEmail", contactEmail);
            intent.putExtra("cAddress", contactAddress);
            intent.putExtra("cPhone", contactPhone);
            intent.putExtra("cID", contactID);
            intent.putExtra("cType",type);
            intent.putExtra("thisName", fullName);
            intent.putExtra("thisEmail", email);
            intent.putExtra("thisProfilePic", profilePic);
            startActivity(intent);
        });
    }

    private void uploadFile(String uri) {
        imageString = "contact" + "_" + type;

        if (TextUtils.isEmpty(uri))
        {
            return;
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
                                Amplify.Storage.getUrl(
                                        finish.getKey(),
                                        URLOptions,
                                        find -> {
                                            Log.i("Storage URL ID", "Successfully generated ID Image URL: " + find.getUrl());
                                            Intent intent = new Intent(getApplicationContext(), IceDisplayContacts.class);
                                            intent.putExtra("cName", contactName);
                                            intent.putExtra("cFirst", contactFirst);
                                            intent.putExtra("cLast", contactLast);
                                            intent.putExtra("cProfession", contactProfession);
                                            intent.putExtra("cEmail", contactEmail);
                                            intent.putExtra("cAddress", contactAddress);
                                            intent.putExtra("cPhone", contactPhone);
                                            intent.putExtra("cID", contactID);
                                            intent.putExtra("cType",type);
                                            intent.putExtra("thisName", fullName);
                                            intent.putExtra("thisEmail", email);
                                            intent.putExtra("thisProfilePic", profilePic);
                                            startActivity(intent);
                                            runOnUiThread(() -> Toast.makeText(IceEditContacts.this, "Updated Contact Picture Successfully.", Toast.LENGTH_SHORT).show());
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

                uploadFile(picturePath);

                // Setting thumbnail
                //Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //thumbnail = getResizedBitmap(thumbnail, 400);
                //Log.w("Image path", picturePath + "");
                //editContactPicture.setImageBitmap(thumbnail);

            } catch (Exception e)  {
                e.printStackTrace();
                Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {

            Log.e("onActivityResult", "Image Upload Cancelled");
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

    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9+]|\\(\\d{1,3}\\))[0-9\\-. ]{3,15}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
