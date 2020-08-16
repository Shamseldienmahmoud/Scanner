package com.example.scanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.scanner.adapter.StationsAdapter;
import com.example.scanner.model.StationsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StationsActivity extends AppCompatActivity {
    ArrayList<StationsItem> stationslists;
    RecyclerView recyclerView;
    StationsAdapter adapter;
    String URL = "http://192.168.1.4/mohamed/ST.php";
    String lang,entry,exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        lang = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0).getLanguage();
        recyclerView = findViewById(R.id.stations_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        stationslists = new ArrayList<>();
        if (lang.contains("en")) {
            getStations(URL, "station_name_en");
        }else if (lang.contains("ar")){
            getStations(URL, "station_name");
        }
       getData();
    }

    private void getData(){
        Intent intent = getIntent();
        if (intent!=null){
            entry = intent.getStringExtra("entry");
            exit = intent.getStringExtra("exit");
        }else {
            return;
        }

    }
    private void getStations(String url, final String lang) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        stationslists.add(
                                new StationsItem(
                                        jsonObject.getString(lang),
                                        jsonObject.getInt("station_line"),
                                        jsonObject.getInt("station_id")
                                )
                        );
                    }
                    adapter = new StationsAdapter(StationsActivity.this, stationslists);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StationsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(StationsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                //dialogError();

            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void dialogError(){
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(StationsActivity.this);
        builder.setMessage("Network error");
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Retry",
                new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        if (lang.contains("en")) {
                            getStations(URL, "station_name_en");
                        }else if (lang.contains("ar")){
                            getStations(URL, "station_name");
                        }
                    }
                });
        builder
                .setNegativeButton(
                        "Cancel",
                        new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                finish();
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
