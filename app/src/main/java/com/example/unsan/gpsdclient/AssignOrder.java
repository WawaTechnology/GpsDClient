package com.example.unsan.gpsdclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * Created by Unsan on 7/5/18.
 */

public class AssignOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    //ListView listView,alphaViewList;
    RecyclerView customerRecyclerView;
    DatabaseReference customerReference,driverDataRef,customerTodayRef,carsDbRef;

    Map<String, Integer> mapIndex;
    String email;
    DatabaseReference driverCarDetails;
    ValueEventListener valueEventListener,dvEventListner;
    TextView num_deliveries,total_order;
  int countDelivery,countOrder;
  CustomRecyclerAdapter customRecyclerAdapter;
  int k=1;





   // CustomAdapter customAdapter;
    ProgressBar pgbar;

    ActionBar actionBar;
    String carNumber;
    Spinner spinner,spinner2;
    // List<String> carNumbers;
    // List<String> vehicleNumbers;
    String vehicleNumber;
    String todayDate;
    List<CustomerOrder> customerOrderList;






    ArrayAdapter<String> arrayAdapter,vehicleadapter;
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.assign_order);
            Log.d("tag","oncreate");
            pgbar=(ProgressBar) findViewById(R.id.pgbar);
            spinner=(Spinner) findViewById(R.id.sp1);
            spinner2=(Spinner) findViewById(R.id.sp2);
            num_deliveries=(TextView) findViewById(R.id.num_deliveries);
            total_order=(TextView) findViewById(R.id.num_order);
            customerRecyclerView=(RecyclerView) findViewById(R.id.customerRecycler);
            // vehicleNumbers=new ArrayList<>();
            FirebaseDatabase fbd=FirebaseDatabase.getInstance();
            driverCarDetails= fbd.getReference("driverCarDb");
            carsDbRef=fbd.getReference("CarsDb");


            driverDataRef=fbd.getReference("Driver");
            customerTodayRef=fbd.getReference("CustomerTodayRecord");
            customerOrderList=new ArrayList<>();







            actionBar=getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);




            //fbd.setPersistenceEnabled(true);




            customerReference=fbd.getReference("CarsDb");
            customerReference.keepSynced(true);

            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
            Date today = new Date();
            todayDate=simpleDateFormat.format(today);

            //carNumbers=new ArrayList<>();
            //ADD vehicle number
            // addVehicleNumbers();
       /* for(int i=1;i<=20;i++)
        {
            carNumbers.add("car "+i);

        }
        */


            arrayAdapter=new ArrayAdapter<String>(AssignOrder.this,R.layout.spinner_item,getResources().getStringArray(R.array.carArray));
            vehicleadapter=new ArrayAdapter<String>(AssignOrder.this,R.layout.spinner_item,getResources().getStringArray(R.array.vehicleNames));


            spinner.setAdapter(arrayAdapter);
            carNumber=arrayAdapter.getItem(0);


            spinner.setOnItemSelectedListener(AssignOrder.this);

            spinner2.setAdapter(vehicleadapter);

            spinner2.setOnItemSelectedListener(AssignOrder.this);




            customRecyclerAdapter=new CustomRecyclerAdapter(AssignOrder.this,customerOrderList);


           customerRecyclerView.setAdapter(customRecyclerAdapter);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(AssignOrder.this,LinearLayoutManager.VERTICAL,false);
            customerRecyclerView.setLayoutManager(linearLayoutManager);

            pgbar.setVisibility(View.VISIBLE);






        }

   /* private void addVehicleNumbers() {
        vehicleNumbers.add("YP2095H");
        vehicleNumbers.add("GBF6032M");
        vehicleNumbers.add("GBD498C");
        vehicleNumbers.add("GBD5898Z");
        vehicleNumbers.add("GBG9001C");
        vehicleNumbers.add("GBG3898X");
        vehicleNumbers.add("GBC7432B");
        vehicleNumbers.add("GBG7570P");


    }
    */

        public void onStart()
        {
            super.onStart();




            // getDriverDetail();
        }
        public void onResume()
        {
            super.onResume();
            Log.d("tag","onresume");
            getCustomerData();



        }


    public AssignOrder() {
        super();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assign,menu);
      /*  TextView tv = new TextView(this);
        String text="car 5";
        tv.setText(text);
        tv.setTextColor(getResources().getColor(R.color.WHITE));
        tv.setOnClickListener(this);
        tv.setPadding(5, 0, 55, 0);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(14);
        menu.add(0, 0, 1, text).setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        */
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh: {
                getDeliveryData();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
        }





        public void getDeliveryData()
        {
            countDelivery=0;
            countOrder=0;
           final int len=customerOrderList.size();
            k=1;
          for(final CustomerOrder customerOrder:customerOrderList)
          {

              String customer=customerOrder.getCustomer();
              customerTodayRef.child(todayDate).child(carNumber).child(customer).addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {

                      if(dataSnapshot.exists())
                      {
                          Log.d("datasnapshot","exists");

                              customerOrder.checked=true;
                              if(dataSnapshot.getValue().equals("Ordered"))
                              {
                                  countOrder+=1;

                              }
                              else
                              {
                                  countDelivery+=1;
                                  customerOrder.deliveryChecked=true;
                                  Log.d("deliverycount",countDelivery+"");
                              }




                      }
                      else {
                          customerOrder.checked = false;

                      }
                      if(k==len)
                      {
                          Log.d("textviewcalled","yes");
                          num_deliveries.setText(countDelivery+"");
                          total_order.setText(countOrder+"");

                      }
                      k+=1;
                      customRecyclerAdapter.notifyDataSetChanged();
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });
          }



        }

    private void getCustomerData() {
        Log.d("chksee","we are here");


       customerReference.child(carNumber).child("Restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("checkk","here");

                customerOrderList.clear();
                if(dataSnapshot.hasChildren()) {


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                        Log.d("checkkey", key);
                        Object valueobj=ds.getValue(Object.class);
                        String value=valueobj.toString();


                        customerOrderList.add(new CustomerOrder(key,value,false,false));


                    }
                    getDeliveryData();
                    pgbar.setVisibility(GONE);

                }
                else
                {
                    Toast.makeText(AssignOrder.this,"No records found!",Toast.LENGTH_LONG).show();
                    pgbar.setVisibility(GONE);
                    customRecyclerAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("dberror",databaseError.getMessage());

            }
        });


    }


        public void onPause()
        {
            super.onPause();
            Log.d("tag","pasue");
        }
        public void onStop()
        {
            super.onStop();
            Log.d("tag","stop");




        }
        @Override
        public void onBackPressed() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
        }


        public void onRestart()
        {
            super.onRestart();
            Log.d("tag","restart");
        }


        public void onDestroy()
        {
            super.onDestroy();

        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch(adapterView.getId()) { //Run Code For Major Spinner
                case R.id.sp1: { // code for first spinner. Depending on spinner.getselecteditem assign adapter to second spinner
                    carNumber = adapterView.getItemAtPosition(i).toString();
                    getCustomerData();
                    getDeliveryData();
                   // getTotalDeliveryCount();


                    break;
                }
                case R.id.sp2: { // code for second spinner
                    //Use get item selected and get selected item position
                    vehicleNumber = adapterView.getItemAtPosition(i).toString();



                    break;
                }
            }





        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
