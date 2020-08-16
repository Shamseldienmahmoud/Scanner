package com.example.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity {
Button entry,exit,subEntery,subExit;
String start,end,id,method,linename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        entry=findViewById(R.id.entry_btn);
        exit=findViewById(R.id.exit_btn);
        subEntery=findViewById(R.id.sub_enter);
        subExit=findViewById(R.id.sub_exit);
        getData();
    }
    private void getData(){
        Intent intent = getIntent();
        start=intent.getStringExtra("start_station");
        end=intent.getStringExtra("end_station");
        id=intent.getStringExtra("station_id");
        linename=intent.getStringExtra("lineName");
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.entry_btn:
                method = "daily_entry";
                Intent entry = new Intent(this,MainActivity.class);
                entry.putExtra("method",method);
                entry.putExtra("start",start);
                entry.putExtra("end",end);
                entry.putExtra("id",id);
                entry.putExtra("lineName",linename);
                startActivity(entry);
                break;
            case R.id.exit_btn:
                method="daily_exit";
                Intent exit = new Intent(this,MainActivity.class);
                exit.putExtra("method",method);
                exit.putExtra("start",start);
                exit.putExtra("end",end);
                exit.putExtra("id",id);
                exit.putExtra("lineName",linename);
                startActivity(exit);
                break;
            case R.id.sub_enter:
                method = "sub_entry";
                Intent sub_entry = new Intent(this,MainActivity.class);
                sub_entry.putExtra("method",method);
                sub_entry.putExtra("start",start);
                sub_entry.putExtra("end",end);
                sub_entry.putExtra("id",id);
                sub_entry.putExtra("lineName",linename);
                startActivity(sub_entry);
                break;
            case R.id.sub_exit:
                method="sub_exit";
                Intent sub_exit = new Intent(this,MainActivity.class);
                sub_exit.putExtra("method",method);
                sub_exit.putExtra("start",start);
                sub_exit.putExtra("end",end);
                sub_exit.putExtra("id",id);
                sub_exit.putExtra("lineName",linename);
                startActivity(sub_exit);
                break;
            default:
                break;

        }
    }
}
