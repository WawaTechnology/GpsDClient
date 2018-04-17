package com.example.unsan.gpsdclient;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Unsan on 16/4/18.
 */

public class DriverActivity extends AppCompatActivity {
    DatabaseReference driverRef;
    TextView dName, pNumber;
    long phone;
    String driver;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_details);
        dName = (TextView) findViewById(R.id.dname);
        pNumber = (TextView) findViewById(R.id.phone);
        driverRef = FirebaseDatabase.getInstance().getReference("Driver");
        Intent intent = getIntent();
        driver = intent.getStringExtra("drivername");
        driverRef.child(driver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Driver d = dataSnapshot.getValue(Driver.class);
                phone=d.getPhone();
                dName.setText(driver);
                pNumber.setText(d.getPhone() + "");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        pNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(DriverActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });



    }
}
