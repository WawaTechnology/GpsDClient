package com.example.unsan.gpsdclient;

import android.*;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * Created by Unsan on 7/5/18.
 */

public class AssignOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener

{


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
  Button reportButton;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    EditText dateText;
    int CountPreviousOrder,CountPreviousDelivery;
    DatabaseReference customerDeliveryRef;
    int totalcount;
    DatePickerDialog datePickerDialog;








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
    List<CustomerDeliveryDetail> previousOrderList;
    String yesterdayDate;






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
            reportButton=(Button)findViewById(R.id.generate_report);
            dateText=(EditText) findViewById(R.id.select_dt);


            // vehicleNumbers=new ArrayList<>();
            FirebaseDatabase fbd=FirebaseDatabase.getInstance();
            driverCarDetails= fbd.getReference("driverCarDb");
            carsDbRef=fbd.getReference("CarsDb");
            customerDeliveryRef=fbd.getReference("DriverDelivery");


            driverDataRef=fbd.getReference("Driver");
            customerTodayRef=fbd.getReference("CustomerTodayRecord");
            customerOrderList=new ArrayList<>();
            previousOrderList=new ArrayList<>();
            dateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar c=Calendar.getInstance();
                    int date=c.get(Calendar.DAY_OF_MONTH);
                    int month=c.get(Calendar.MONTH);
                    int year=c.get(Calendar.YEAR);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        datePickerDialog=new DatePickerDialog(AssignOrder.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                i1+=1;
                                dateText.setText(i2+"-"+(i1<10?("0"+i1):i1)+"-"+i);



                            }
                        },year,month,date);
                        datePickerDialog.show();
                    }

                }
            });








            actionBar=getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);




            //fbd.setPersistenceEnabled(true);




            customerReference=fbd.getReference("carsRecord");






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
            reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            });
            yesterdayDate=getYesterdayDateString();
           // getCustomerData();






        }
    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(yesterday());
    }
    public static Font setChineseFont() {
        BaseFont bf = null;
        Font fontChinese = null;
        try {
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bf, 10, Font.NORMAL);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fontChinese;
    }


    private void createPdfWrapper() throws FileNotFoundException,DocumentException{

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }


                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            getPreviousOrder();
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void createPdf() throws FileNotFoundException, DocumentException {


        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("TAG", "Created a new directory for PDF");
        }

        pdfFile = new File(docsFolder.getAbsolutePath(),"Delivery.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        String dt=yesterdayDate;
        if(dateText.getText().toString().length()>0)
        {
            dt=dateText.getText().toString();
        }
        document.add(new Paragraph("Date: "+dt));
        document.add(new Paragraph("Car Number: "+carNumber));
        Log.d("inpdf",CountPreviousDelivery+"");

        document.add(new Paragraph("Total Deliveries: "+CountPreviousDelivery));
        document.add(new Paragraph("Total Order Waiting: "+CountPreviousOrder));
        int index=0;
        if(previousOrderList!=null&&previousOrderList.size()>0) {
            for (CustomerDeliveryDetail order : previousOrderList) {
                index+=1;
                if (order.getTime()!=null) {
                    document.add(new Paragraph(index+"  "+order.getCustomer() + "        " + "delivered"+"       "+"Time: "+order.getTime(),setChineseFont()));
                }
                else {

                        document.add(new Paragraph(index+"  "+order.getCustomer() + "        " + "waiting",setChineseFont()));

                }
            }
        }

        document.close();
        previewPdf();

    }

    private void getPreviousOrder() throws FileNotFoundException, DocumentException {
            previousOrderList.clear();
        CountPreviousOrder=0;
        CountPreviousDelivery=0;
        totalcount=0;


        //default setting date to yesterday
        String dtSearch=yesterdayDate;
           //String yesterday= getYesterdayDateString();
        //if date is given in editbox
        if(dateText.getText().toString().length()>0)
        {
            dtSearch=dateText.getText().toString();
        }


                customerTodayRef.child(dtSearch).child(carNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            Log.d("prevdatasnapshot", "exists");
                             totalcount=(int)dataSnapshot.getChildrenCount();
                             Log.d("checktota",totalcount+"");

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {



                                 String customer = ds.getKey();
                                String val = ds.getValue(String.class);
                                if (val.equals("Ordered")) {
                                    CountPreviousOrder += 1;
                                    CustomerDeliveryDetail customerDeliveryDetail = new CustomerDeliveryDetail(customer, null);
                                    previousOrderList.add(customerDeliveryDetail);

                                } else {
                                    CountPreviousDelivery += 1;
                                    previousOrderList.add(new CustomerDeliveryDetail(customer,val));




                                }
                                Log.d("checkprepsize",previousOrderList.size()+"");

                                try {
                                    if(previousOrderList.size()==totalcount)
                                        createPdf();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                }



                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            // Uri uri = Uri.fromFile(pdfFile);
            Uri uri = FileProvider.getUriForFile(AssignOrder.this, BuildConfig.APPLICATION_ID + ".provider",pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            startActivity(intent);
        }else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
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
           Log.d("checkcustomerLSize",len+"");
            k=1;
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sf=new SimpleDateFormat("HH a");
            Date today = new Date();
            todayDate=simpleDateFormat.format(today);
            String hho=sf.format(today);
            String hh1= hho.substring(3);
            Log.d("checkam",hh1);
            Log.d("checkh",hho);


           // String yester=getYesterdayDateString();
           // Log.d("getyes",yester);

           if(hh1.equals("am")) {
               List<String> hourList=new ArrayList<>();
               hourList.add("00 am");
               hourList.add("01 am");
               hourList.add("02 am");
               hourList.add("03 am");
               hourList.add("04 am");
               hourList.add("05 am");
               if(hourList.contains(hho))
               {
                   todayDate=getYesterdayDateString();
               }


           }
          for(final CustomerOrder customerOrder:customerOrderList)
          {

              String customer=customerOrder.getCustomer();
              //checktime here and set to previous date
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
        customerOrderList.clear();
        customRecyclerAdapter.notifyDataSetChanged();


       customerReference.child(carNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("checkk","here");


                if(dataSnapshot.hasChildren()) {


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        CustomerEngChinese customerEngChinese= ds.getValue(CustomerEngChinese.class);


                        customerOrderList.add(new CustomerOrder(customerEngChinese.chinese,customerEngChinese.english.toString(),false,false));


                    }
                        Log.d("orderlistsizefunc1",customerOrderList.size()+"");
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
                   // getDeliveryData();
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

