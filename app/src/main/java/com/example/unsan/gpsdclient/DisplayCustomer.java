package com.example.unsan.gpsdclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Unsan on 9/5/18.
 */

public class DisplayCustomer extends AppCompatActivity {
    ListView lvw;
    CustomAdapter customAdapter;
    List<CustomerEngChinese> customerEngChineseList;
    DatabaseReference dbr;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_customer);
        lvw=(ListView)findViewById(R.id.lst_view);
        dbr= FirebaseDatabase.getInstance().getReference("carsRecord");
        customerEngChineseList=new ArrayList<>();
        customAdapter=new CustomAdapter(DisplayCustomer.this,R.layout.single_disp,customerEngChineseList);
        lvw.setAdapter(customAdapter);
        getData();

    }
    public void getData()
    {
        dbr.child("Car 1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {

                           CustomerEngChinese customerEngChinese= ds.getValue(CustomerEngChinese.class);
                           Log.d("chinesename",customerEngChinese.chinese);



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
