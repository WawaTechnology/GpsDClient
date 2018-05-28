package com.example.unsan.gpsdclient;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unsan on 13/4/18.
 */

public class SearchResultsActivity extends AppCompatActivity {
    ListView lvw;
    TextView tvw;
    DriverAdapter historyitemAdapter;

    List<DriverDelivery> deliveryList;
    List<String> imgsList;
    RecyclerView rcview;
    String img;
    DatabaseReference restaurantReference,imageDb;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        rcview=(RecyclerView)findViewById(R.id.rcview);
        tvw=(TextView)findViewById(R.id.noResult);

        deliveryList=new ArrayList<>();
        imgsList=new ArrayList<>();
        FirebaseDatabase fbd=FirebaseDatabase.getInstance();
        restaurantReference= fbd.getReference("DriverDelivery");
        imageDb=fbd.getReference("imgReferences");
        historyitemAdapter=new DriverAdapter(SearchResultsActivity.this,deliveryList,imgsList);
        rcview.setAdapter(historyitemAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(SearchResultsActivity.this, LinearLayoutManager.VERTICAL,false);
        rcview.setLayoutManager(linearLayoutManager);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem=menu.findItem(R.id.searchi);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchItem.expandActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(final String query) {
                deliveryList.clear();
                restaurantReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            DriverDelivery dr=ds.getValue(DriverDelivery.class);

                            String key=dr.getCustomer();
                            if(key.toLowerCase().contains(query.toLowerCase()))

                            {

                                Log.d("checkkey",key);

                                deliveryList.add(dr);
                                img= getImage(key);


                                tvw.setVisibility(View.GONE);
                            }

                        }
                        if(deliveryList.isEmpty())
                        {
                            tvw.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private String getImage(String key) {
        Log.d("checkkey",key);

        imageDb.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                img= (String)dataSnapshot.getValue();

                imgsList.add(img);
                historyitemAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return img;

    }

}