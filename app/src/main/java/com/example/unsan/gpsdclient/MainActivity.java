package com.example.unsan.gpsdclient;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    final int permCheck=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Log.d("checkpermission"," "+permissionCheck);

           int permission= checkSelfPermission(Manifest.permission.CALL_PHONE);
           Log.d("getpermissiom",""+permission);
           if(permission<=0)
           {
               requestPermissions(new String[]{Manifest.permission.CALL_PHONE},permCheck);
           }
        }

        Intent intent=new Intent(MainActivity.this,NewDisplayActivity.class);
        startActivity(intent);
    }

   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case permCheck:
            {
               int result= grantResults[0];
               Log.d("checkresult",result+"");
            }
        }
    }

}
