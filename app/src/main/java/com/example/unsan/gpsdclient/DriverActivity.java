package com.example.unsan.gpsdclient;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Unsan on 16/4/18.
 */

public class DriverActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    DatabaseReference driverRef;
    TextView dName, pNumber;
    long phone;
    String driver;
    RecyclerView recycler_driver;
    DatabaseReference driverDayDatabase,driverDeliveryReference;
    ValueEventListener valueEventListener;
    SwipeRefreshLayout swipeRefreshLayout;


    DatabaseReference imgReference;
    List<String> imgList;
    List<DriverDelivery> driverDeliveryList;

    DayRecordAdapter dayRecordAdapter;
    String dateString;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_details);
        dName = (TextView) findViewById(R.id.dname);
        pNumber = (TextView) findViewById(R.id.phone);
        recycler_driver=(RecyclerView) findViewById(R.id.recycler_today);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        driverDeliveryList=new ArrayList<>();
        driverRef = FirebaseDatabase.getInstance().getReference("Driver");
        driverDayDatabase= FirebaseDatabase.getInstance().getReference("driverDayRecord");
        driverDeliveryReference=FirebaseDatabase.getInstance().getReference("DriverDelivery");
        imgReference=FirebaseDatabase.getInstance().getReference("imgReferences");
        imgList=new ArrayList<>();
        dayRecordAdapter=new DayRecordAdapter(DriverActivity.this,imgList,driverDeliveryList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(DriverActivity.this,LinearLayoutManager.VERTICAL,false);
        recycler_driver.setLayoutManager(linearLayoutManager);
        recycler_driver.setAdapter(dayRecordAdapter);
        Intent intent = getIntent();
        driver = intent.getStringExtra("drivername");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Date today=new Date();
        dateString=simpleDateFormat.format(today);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        //TODO add driver day record

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
        getDeliveries();



    }

    private void getDeliveries() {
        imgList.clear();
        driverDeliveryList.clear();
        valueEventListener=driverDayDatabase.child(dateString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String key=ds.getKey();
                        Log.d("checkdkey",key);

                        getDeliveryRecord(key);


                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDeliveryRecord(final String key) {
        driverDeliveryReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DriverDelivery driverDelivery=dataSnapshot.getValue(DriverDelivery.class);

                if(driverDelivery.driverName.equals(driver))
                {
                    driverDeliveryList.add(driverDelivery);


                    imgReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if(dataSnapshot.exists()) {

                                String img = dataSnapshot.getValue(String.class);
                                if (img != null) {
                                    Log.d("checkim",img);
                                    imgList.add(img);
                                    dayRecordAdapter.notifyDataSetChanged();
                                }



                            }
                            else {
                                imgList.add("no image");
                                dayRecordAdapter.notifyDataSetChanged();
                            }



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRefresh() {
        getDeliveries();
        swipeRefreshLayout.setRefreshing(false);

    }
}
