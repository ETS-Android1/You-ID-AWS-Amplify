package com.example.you_id_fix;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Contacts;
import com.amplifyframework.datastore.generated.model.UserInfo;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class AmplifyApp  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // Add this line, to include the Auth plugin.

            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException failure) {
            Log.e("Tutorial", "Could not initialize Amplify", failure);
        }

        //TO FETCH UPDATED DATA FROM DATABASE
        Amplify.DataStore.observe(UserInfo.class,
                started -> Log.i("Tutorial", "Observation began."),
                change -> Log.i("Tutorial", change.item().toString()),
                failure -> Log.e("Tutorial", "Observation failed.", failure),
                () -> Log.i("Tutorial", "Observation complete.")
        );

        // TO FETCH UPDATED DATA FROM DATABASE
        Amplify.DataStore.observe(Contacts.class,
                started -> Log.i("Contacts", "Observation began."),
                change -> Log.i("Contacts", change.item().toString()),
                failure -> Log.e("Contacts", "Observation failed.", failure),
                () -> Log.i("Contacts", "Observation complete.")
        );

        // QUERY ONE
        Amplify.DataStore.query(UserInfo.class,
                todos -> {
                    while (todos.hasNext()) {
                        UserInfo todo = todos.next();

                        Log.i("Tutorial", "==== YouID Application ====");
                        Log.i("Tutorial", "First Name: " + todo.getFirstname());

                        if (todo.getLastname() != null) {
                            Log.i("Tutorial", "Last Name: " + todo.getLastname());
                        }
                        if (todo.getAge()!= null) {
                            Log.i("Tutorial", "Age: " + todo.getAge());
                        }
                        if (todo.getEmail() != null) {
                            Log.i("Tutorial", "Email: " + todo.getEmail());
                        }
                    }
                },
                failure -> Log.e("Tutorial", "Could not query DataStore", failure)
        );
    }
}