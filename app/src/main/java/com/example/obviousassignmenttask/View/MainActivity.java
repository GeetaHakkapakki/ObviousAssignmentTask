package com.example.obviousassignmenttask.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obviousassignmenttask.Model.ImageModel;
import com.example.obviousassignmenttask.R;
import com.example.obviousassignmenttask.ViewModel.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<ImageModel> imagesList;
    private ProgressDialog progressBar;
    GridView gridView;
    ImageAdapter imageAdapter;
    String date,title,media_type,service_version,description,imageUrl,copyright,hdurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isNetworkConnectionAvailable();

        gridView = findViewById(R.id.gridview);

        imagesList = new ArrayList<>();
        new GetNasaImages().execute();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this,DetailActivity.class);
                i.putExtra("title",imagesList.get(position).getTitle());
                i.putExtra("date",imagesList.get(position).getDate());
                i.putExtra("description",imagesList.get(position).getExplanation());
                i.putExtra("media_type",imagesList.get(position).getMediaType());
                i.putExtra("service_version",imagesList.get(position).getServiceVersion());
                i.putExtra("url",imagesList.get(position).getUrl());
                startActivity(i);
            }
        });


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

    private class GetNasaImages extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(MainActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Please Wait...");
            progressBar.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            if(loadDataFromAsset() != null){
                try {
                    JSONArray jsonArray = new JSONArray(loadDataFromAsset());
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        date = object.getString("date");
                        description = object.getString("explanation");
                        media_type = object.getString("media_type");
                        service_version = object.getString("service_version");
                        title = object.getString("title");
                        imageUrl = object.getString("url");

                        ImageModel nasaimages = new ImageModel(date,description,media_type,service_version,title,imageUrl);
                        imagesList.add(nasaimages);
                        Log.d("size", String.valueOf(imagesList.size()));


                    }

                }catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("error",String.valueOf(e.getMessage()));
                          //  Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    });
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(progressBar.isShowing()){
                progressBar.dismiss();
            }

            Collections.reverse(imagesList);
            imageAdapter = new ImageAdapter(MainActivity.this,imagesList);
            gridView.setAdapter(imageAdapter);
            imageAdapter.notifyDataSetChanged();
        }
    }
    public String loadDataFromAsset(){
        String json = null;
        try {
            InputStream is = MainActivity.this.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}