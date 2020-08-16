package com.example.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ResultActivity extends AppCompatActivity {
    TextView result ,data,status;
    String AES="AES";
    String privateKey = "shamsmahmoudaboelmagd";
    String method,start,end,id,getResult,lineName;
    LottieAnimationView image;
    String rCost,rToken,rStart,rEnd,rId,rStaionsNum;
    String myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        result =findViewById(R.id.textView);
        data =findViewById(R.id.data);
        status=findViewById(R.id.tv_status);
        image=findViewById(R.id.animationView);

        Intent intent = getIntent();
        if (intent!=null){
        String response=intent.getStringExtra("response");
        String method=intent.getStringExtra("method");
        if (method.equals("daily_entry")){
            if (!response.isEmpty()&&response.equals("Gate Opened")){
                status.setText(response);
            }else if (!response.isEmpty()&&response.equals("This Ticket Expired")){
                status.setText(response);
                image.setAnimation(R.raw.error);
                image.playAnimation();
                image.loop(true);
            }
        }else if (method.equals("daily_exit")){
            if (!response.isEmpty()&&response.equals("Gate Opened")) {
                status.setText(response);
                image.setAnimation(R.raw.exit);
                image.playAnimation();
                image.loop(true);
            }else if (!response.isEmpty()&&response.equals("This Ticket Expired")){
                status.setText(response);
                image.setAnimation(R.raw.error);
                image.playAnimation();
                image.loop(true);
            }
        }else if (method.equals("sub_entry")){
            if (!response.isEmpty()&&response.equals("Gate Opened")){
                status.setText(response);
            }else if (!response.isEmpty()&&response.equals("This Ticket Expired")){
                status.setText(response);
                image.setAnimation(R.raw.error);
                image.playAnimation();
                image.loop(true);
            }
        }else if (method.equals("sub_exit")){
            if (!response.isEmpty()&&response.equals("Gate Opened")) {
                status.setText(response);
                image.setAnimation(R.raw.exit);
                image.playAnimation();
                image.loop(true);
            }else if (!response.isEmpty()&&response.equals("This Ticket Expired")){
                status.setText(response);
                image.setAnimation(R.raw.error);
                image.playAnimation();
                image.loop(true);
            }
        }

    }
    }


}
