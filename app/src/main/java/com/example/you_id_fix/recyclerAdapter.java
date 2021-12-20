package com.example.you_id_fix;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.options.StorageDownloadFileOptions;
import com.amplifyframework.storage.options.StorageGetUrlOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<String> imagenames;
    private Context context;
    private Activity activity;

    public recyclerAdapter(Context context, ArrayList<String> imagesNames, ArrayList<String> imagesURL, Activity activity)
    {
        this.imagenames = imagesNames;
        this.context = context;
        this.activity = activity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView image_fileName;
        ImageView imageView_imagelist, imageView_download;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_fileName = itemView.findViewById(R.id.imagefilename);
            imageView_imagelist = itemView.findViewById(R.id.imageview_imageList);
            imageView_download = itemView.findViewById(R.id.imageView_Download);
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.image_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {

        holder.image_fileName.setText(imagenames.get(position));

        StorageGetUrlOptions URLoptions = StorageGetUrlOptions.builder().accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        Amplify.Storage.getUrl(
                imagenames.get(position),
                URLoptions,
                find -> {
                    Log.i("Storage URL", "Successfully generated: " + find.getUrl());
                    new Handler(Looper.getMainLooper()).post(
                            () -> Picasso.get().load(find.getUrl().toString()).resize(50,50).centerCrop().into(holder.imageView_imagelist));
                    //this.runOnUiThread((Runnable) () -> Picasso.get().load(find.getUrl().toString()).resize(50,50).centerCrop().into(holder.imageView_imagelist));
                },
                error -> Log.e("Storage URL", "URL generation failure", error) );
        //Picasso.get().load(imageUrl.get(position)).resize(50,50).centerCrop().into(holder.imageView_imagelist);

        // to download one image from recycler
        String imagename = imagenames.get(position);
        holder.imageView_download.setOnClickListener(v -> {
            Log.d("Recycler Item:", "Downloading: " + imagenames.get(position));
           downloadImage(imagename);

        });

    }
    // to download one image from recycler
    private void downloadImage(String imagename)
    {
        StorageDownloadFileOptions options = StorageDownloadFileOptions.builder()
                .accessLevel(StorageAccessLevel.PRIVATE)
                .build();

        String revisedString = null;

        if (imagename.startsWith("travel"))
        {
            revisedString = imagename.substring(7);
        }
        if (imagename.startsWith("health"))
        {
            revisedString = imagename.substring(7);
        }
        if (imagename.startsWith("legal"))
        {
            revisedString = imagename.substring(6);
        }
        if (imagename.startsWith("merchant"))
        {
            revisedString = imagename.substring(9);
        }
        if (imagename.startsWith("contact_ICE"))
        {
            revisedString = imagename.substring(12);
        }

        Amplify.Storage.downloadFile(
                imagename,
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+revisedString+".jpg"),
                options,
                progress -> Log.i("downloadImage", "Fraction completed: " + progress.getFractionCompleted()),
                result -> { Log.i("downloadImage", "Successfully downloaded: " );
                  //  DataS3 ileobj = new DataS3(result.getFile().getAbsolutePath(),result.getFile().getName(), imagename);
                    Log.i("getResult : Path", result.getFile().getAbsolutePath() + " key: " + result.getFile().getName() + " origin: " + imagename);
                    Toast.makeText(activity.getApplicationContext(), "Successfully downloaded: " + imagename , Toast.LENGTH_LONG).show();
                },
                error -> { Log.e("downloadImage",  "Download Failure", error);
                Toast.makeText(activity.getApplicationContext(), "Download Failure", Toast.LENGTH_LONG).show();}
        );

        // Generate download URL
        Amplify.Storage.getUrl(
                imagename,
                result -> Log.i("downloadImage", "Successfully generated: " + result.getUrl()),
                error -> Log.e("downloadImage", "URL generation failure", error)

        );
    }

    @Override
    public int getItemCount() {
        return imagenames.size();
    }
}