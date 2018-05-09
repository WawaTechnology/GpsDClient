package com.example.unsan.gpsdclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
 * Created by Unsan on 12/4/18.
 */

public class HistoryActivity extends AppCompatActivity {
    HistoryAdapter historyAdapter;
    RecyclerView recyclerView;
    List<DeliveryDriver> deliveryList;
    FirebaseDatabase fbd;
    DatabaseReference historyDb;
    ValueEventListener valueEventListener;
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
                  Toast.makeText(HistoryActivity.this,"No Internet Connection Available!",Toast.LENGTH_SHORT).show();
              }
            }

        }
    };


    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        Log.d("oncreate","called");
        setContentView(R.layout.history_activity);

        recyclerView=(RecyclerView)findViewById(R.id.history_list);
        deliveryList=new ArrayList<>();
        fbd=FirebaseDatabase.getInstance();
        fbd.setPersistenceEnabled(true);
        historyDb=fbd.getReference("deliveryDriver");
        historyAdapter=new HistoryAdapter(HistoryActivity.this,deliveryList);
        recyclerView.setAdapter(historyAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(HistoryActivity.this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
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
              //  Intent intent = new Intent(HistoryActivity.this, SearchResultsActivity.class);
                //TODO CHANGE IT BACK TO SEARCHRESULTACTIVITY
                Intent intent=new Intent(HistoryActivity.this,NewDisplayActivity.class);
                startActivity(intent);
                break;


            }
            case R.id.addCustomer:
            {
                Intent intent=new Intent(HistoryActivity.this,AddCustomerActivity.class);
                startActivity(intent);


                break;

            }



        }
        return super.onOptionsItemSelected(item);
    }

    private void findDelivery() {

        historyDb.orderByChild("timeval").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    deliveryList.clear();
                    // Log.d("actchk", "dbchk");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //  if(ds.getValue().equals("driverName"))
                        //   {
                        DeliveryDriver rbd = ds.getValue(DeliveryDriver.class);
                        // Log.d("titleck", rbd.getTitle());

                        deliveryList.add(rbd);

                        // }
                    }
                    historyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(brd);

    }
}

