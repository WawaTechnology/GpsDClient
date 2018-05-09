package com.example.unsan.gpsdclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unsan on 27/4/18.
 */

public class NewDisplayActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    DriverAdapter driverAdapter;
    RecyclerView recyclerView;
    String img;
    List<DriverDelivery> deliveryList;
    FirebaseDatabase fbd;
    DatabaseReference historyDb,imageDb;
    List<String> imgList;
    ValueEventListener valueEventListener;
    SwipeRefreshLayout swipeRefreshLayout;
    NetworkInfo networkInfo;
    BroadcastReceiver brd=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
            {
                ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo= cm.getActiveNetworkInfo();
                if(networkInfo==null)
                {
                    Toast.makeText(NewDisplayActivity.this,"No Internet Connection Available!",Toast.LENGTH_SHORT).show();
                }
            }

        }
    };


    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        setContentView(R.layout.history_activity);

        recyclerView=(RecyclerView)findViewById(R.id.history_list);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        deliveryList=new ArrayList<>();
        imgList=new ArrayList<>();
        fbd=FirebaseDatabase.getInstance();


        historyDb=fbd.getReference("DriverDelivery");
        imageDb=fbd.getReference("imgReferences");
        driverAdapter=new DriverAdapter(NewDisplayActivity.this,deliveryList,imgList);
        recyclerView.setAdapter(driverAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(NewDisplayActivity.this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(brd,intentFilter);



        findDelivery();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.searchid: {
                Intent intent = new Intent(NewDisplayActivity.this, SearchResultsActivity.class);
                startActivity(intent);
                break;


            }
            case R.id.addCustomer:
            {
                Intent intent=new Intent(NewDisplayActivity.this,AddCustomerActivity.class);
                startActivity(intent);


                break;

            }
            case R.id.add_car:
            {
                Intent intent=new Intent(NewDisplayActivity.this,AssignCarCustomer.class);
                startActivity(intent);


                break;

            }
            case R.id.pdfGenerator:
            {
                Intent intent=new Intent(NewDisplayActivity.this,DisplayCustomer.class);
                startActivity(intent);
                break;





            }
            case R.id.assignorder:
            {
                Intent intent=new Intent(NewDisplayActivity.this,AssignOrder.class);
                startActivity(intent);
            }



        }
        return super.onOptionsItemSelected(item);
    }

    private void findDelivery() {
        deliveryList.clear();
        imgList.clear();

      historyDb.orderByChild("timeval").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.hasChildren()) {

                    // Log.d("actchk", "dbchk");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        //  if(ds.getValue().equals("driverName"))
                        //   {
                        String key=ds.getKey();
                        Log.d("checkkeyc",key);


                        DriverDelivery rbd = ds.getValue(DriverDelivery.class);
                        // Log.d("titleck", rbd.getTitle());

                        deliveryList.add(rbd);
                        img= getImage(key);



                        // }
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String getImage(String key) {
        Log.d("checkkey",key);

        imageDb.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               img= (String)dataSnapshot.getValue();

                imgList.add(img);
                driverAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return img;

    }

    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(brd);

    }

    @Override
    public void onRefresh() {
        findDelivery();
        swipeRefreshLayout.setRefreshing(false);
    }
}
