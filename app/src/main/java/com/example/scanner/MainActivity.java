package com.example.scanner;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    String method,start,end,id,lineName;
    String myData;
    final String  ENTRY_URL="http://192.168.1.4/Scanner/enter.php";
    final String  EXIT_URL="http://192.168.1.4/Scanner/exit.php";
    final String  ENTER_SUB="http://192.168.1.4/Scanner/entersub.php";
    final String  EXIT_SUB="http://192.168.1.4/Scanner/exitsub.php";

    int s,n,num;


    String AES="AES";
    String privateKey = "shamsmahmoudaboelmagd";
    String rCost,rToken,rStart,rEnd,rId,rStaionsNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
        scannerView=findViewById(R.id.zxscan);
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.setResultHandler(MainActivity.this);
                        scannerView.startCamera();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        Toast.makeText(MainActivity.this, "you must accept this permission", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        scannerView.setResultHandler(this);
        // Start camera on resume
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
      if (rawResult.getText() != null){
         String TheResult=rawResult.getText();
          try {
              myData = decryption(TheResult,privateKey);
          } catch (Exception e) {
              e.printStackTrace();
          }
          if (method.equals("daily_entry")){
            entry();
          }else if (method.equals("daily_exit")){
             exit();
          }else if (method.equals("sub_entry")){
              stationsNum();
          }else if (method.equals("sub_exit")){
              subExit();
          }

        }else{
            Toast.makeText(this, "data error", Toast.LENGTH_SHORT).show();
        }


    }
    private String decryption (String data,String password )throws Exception{
        SecretKeySpec keySpec=generatekey(password);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,keySpec);
        byte [] decrybt= Base64.decode(data,Base64.DEFAULT);
        byte [] decValue=c.doFinal(decrybt);
        String decryptedValue = new String(decValue);
        return decryptedValue;

    }
    private SecretKeySpec generatekey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes=password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte [] key=digest.digest();
        SecretKeySpec secretKey=new SecretKeySpec(key,"AES");
        return secretKey;
    }
    private void getData(){
        Intent intent = getIntent();
        method=intent.getStringExtra("method");
        start=intent.getStringExtra("start");
        end=intent.getStringExtra("end");
        id=intent.getStringExtra("id");
        lineName=intent.getStringExtra("lineName");

    }
    private void entry() {
        String[] words=myData.split("\n");
        rCost = words[0].toString().trim();
        rStart=words[1].toString().trim();
        rEnd=words[2].toString().trim();
        rStaionsNum=words[3].toString().trim();
        rToken=words[4].toString().trim();
        rId=words[5].toString().trim();
        final String serial = rToken;
        final String userid=rId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ENTRY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String respo = jsonObject.getString("message");
                     Intent x =new Intent(getApplicationContext(),ResultActivity.class);
                     x.putExtra("method","daily_entry");
                    x.putExtra("response",respo);
                        startActivity(x);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("startstation",start);
                params.put("user_id",userid);
                params.put("token",serial);
                return params;
            }
        };VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void exit() {
        String[] words=myData.split("\n");
        rCost = words[0].toString().trim();
        rStart=words[1].toString().trim();
        rEnd=words[2].toString().trim();
        rStaionsNum=words[3].toString().trim();
        rToken=words[4].toString().trim();
        rId=words[5].toString().trim();
        final String serial = rToken;
        final String userid=rId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,EXIT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String respo = jsonObject.getString("message");
                    Intent x =new Intent(getApplicationContext(),ResultActivity.class);
                    x.putExtra("method","daily_exit");
                    x.putExtra("response",respo);
                    startActivity(x);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("endstation",end);
                params.put("user_id",userid);
                params.put("token",serial);
                return params;
            }
        };VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

private String getSubData(){
    SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
    String start = prefs.getString("start", "No name defined");
    String end = prefs.getString("end", "No name defined");//"No name defined" is the default value.

    s=Integer.parseInt(start);
    n=Integer.parseInt(end);
    num=Math.abs(s-n);

    String data = String.valueOf(num);
    return data;

}
    private void subExit() {
        String[] words=myData.split("\n");
        rCost = words[0].toString().trim();
        rStart=words[1].toString().trim();
        rEnd=words[2].toString().trim();
        rStaionsNum=words[3].toString().trim();
        rToken=words[5].toString().trim();
        rId=words[4].toString().trim();
        final String serial = rToken;
        final String userid=rId;
        final String num = getSubData();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EXIT_SUB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String respo = jsonObject.getString("message");
                    Intent x =new Intent(getApplicationContext(),ResultActivity.class);
                    x.putExtra("method","sub_exit");
                    x.putExtra("response",respo);
                    startActivity(x);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("endstation",end);
                params.put("user_id",userid);
                params.put("token",serial);
                return params;
            }
        };VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void stationsNum() {
        String[] words=myData.split("\n");
        rCost = words[0].toString().trim();
        rStart=words[1].toString().trim();
        rEnd=words[2].toString().trim();
        rStaionsNum=words[3].toString().trim();
        rToken=words[5].toString().trim();
        rId=words[4].toString().trim();
        final String serial = rToken;
        final String userid=rId;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ENTER_SUB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String respo = jsonObject.getString("message");
                    Intent x =new Intent(getApplicationContext(),ResultActivity.class);
                    x.putExtra("method","sub_entry");
                    x.putExtra("response",respo);
                    startActivity(x);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("startstation",start);
                params.put("user_id",userid);
                params.put("token",serial);
                return params;
            }
        };VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
