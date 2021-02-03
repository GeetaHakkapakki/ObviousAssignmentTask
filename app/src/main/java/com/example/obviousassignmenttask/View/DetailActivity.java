package com.example.obviousassignmenttask.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.obviousassignmenttask.R;

public class DetailActivity extends AppCompatActivity {
    TextView txt_title,txt_date,txt_media_type,txt_service_version,txt_description;
    ImageView img_nasa;
    String url,title,media_type,date,service_version,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        isNetworkConnectionAvailable();


        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        media_type = getIntent().getStringExtra("media_type");
        service_version = getIntent().getStringExtra("service_version");
        date = getIntent().getStringExtra("date");
        description = getIntent().getStringExtra("description");

        txt_title  = findViewById(R.id.txt_nasa_title);
        txt_date  = findViewById(R.id.txt_date);
        txt_media_type  = findViewById(R.id.txt_media_type);
        txt_service_version  = findViewById(R.id.txt_service_version);
        txt_description  = findViewById(R.id.txt_description);
        img_nasa  = findViewById(R.id.img_nasa);

        txt_title.setText("Title : "+title);
        txt_date.setText("Date : "+date);
        txt_media_type.setText("Media Type : "+media_type);
        txt_service_version.setText("Service Version : "+service_version);
        txt_description.setText("Explanation : "+description);
        Glide.with(this).load(url).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(img_nasa);
    }


    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
            return true;
        }
        else{
            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }
}