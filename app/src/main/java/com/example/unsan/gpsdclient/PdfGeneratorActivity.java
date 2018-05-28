package com.example.unsan.gpsdclient;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Unsan on 4/5/18.
 */

public class PdfGeneratorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    EditText dateText;
    Button submit;
    ArrayAdapter arrayAdapter;
    String driverName;
    private File pdfFile;
    DatabaseReference driverDeliveryReference,driverDayDatabase,imgReference;
    List<String> imgList;
    List<DriverDelivery> driverDeliveryList;
    String dateString;
    ValueEventListener valueEventListener;
    int totalCount;
    int k;
    DatePickerDialog datePickerDialog;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_creator);
        spinner=(Spinner)findViewById(R.id.driver_spinner);
        dateText=(EditText)findViewById(R.id.ent_dt);
        submit=(Button)findViewById(R.id.generate);
        driverDeliveryReference= FirebaseDatabase.getInstance().getReference("DriverDelivery");
        driverDayDatabase= FirebaseDatabase.getInstance().getReference("driverDayRecord");
        imgReference=FirebaseDatabase.getInstance().getReference("imgReferences");
        arrayAdapter=new ArrayAdapter(PdfGeneratorActivity.this,R.layout.spinner_item,getResources().getStringArray(R.array.driverNames));
        imgList=new ArrayList<>();
        driverDeliveryList=new ArrayList<>();
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(PdfGeneratorActivity.this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecord();
            }
        });
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c=Calendar.getInstance();
                int mYear=c.get(Calendar.YEAR);
                int mMonth=c.get(Calendar.MONTH);
                int mDay=c.get(Calendar.DAY_OF_MONTH);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datePickerDialog=new DatePickerDialog(PdfGeneratorActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            i1=i1+1;
                            dateText.setText(i2 + "-"
                                    + (i1<10?("0"+i1):(i1)) + "-" + i);

                        }
                    },mYear,mMonth,mDay);
                    datePickerDialog.show();

                }
            }
        });









    }
    public void getRecord() {

        imgList.clear();
        driverDeliveryList.clear();
        dateString=dateText.getText().toString();
        Log.d("checkdate",dateString);
        k=0;




        valueEventListener = driverDayDatabase.child(dateString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    totalCount=(int)dataSnapshot.getChildrenCount();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        k+=1;

                        String key = ds.getKey();
                        Log.d("checkdkey", key);

                        getDeliveryRecord(key);



                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void createPdf() throws FileNotFoundException, DocumentException {


        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("TAG", "Created a new directory for PDF");
        }



            pdfFile = new File(docsFolder.getAbsolutePath(), "Delivery.pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();
            document.add(new Paragraph("Driver Name: " + driverName));

            document.add(new Paragraph("Date: " + dateString));


            document.add(new Paragraph("Total Deliveries: " + driverDeliveryList.size()));

            int index = 0;
            if (driverDeliveryList.size() > 0) {
                for (DriverDelivery driverDelivery : driverDeliveryList) {
                    index += 1;

                    document.add(new Paragraph(index + "  " + driverDelivery.customer + "        " +"Vehicle Number: "+driverDelivery.vehicleNumber+ "        " +"Time: " + driverDelivery.getDeliveryTime(), setChineseFont()));


                }
            }


            document.close();
            previewPdf();


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
            Uri uri = FileProvider.getUriForFile(PdfGeneratorActivity.this, BuildConfig.APPLICATION_ID + ".provider",pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            startActivity(intent);
        }else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
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


    private void getDeliveryRecord(final String key) {
        driverDeliveryReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DriverDelivery driverDelivery=dataSnapshot.getValue(DriverDelivery.class);

                if(driverDelivery.driverName.equals(driverName))
                {
                    driverDeliveryList.add(driverDelivery);
                    if(k==totalCount)
                    {
                        try {
                            createPdf();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }



                    imgReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if(dataSnapshot.exists()) {

                                String img = dataSnapshot.getValue(String.class);
                                if (img != null) {
                                    Log.d("checkim",img);
                                    imgList.add(img);

                                }



                            }
                            else {
                                imgList.add("no image");

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        driverName=  adapterView.getItemAtPosition(i).toString();
        Log.d("drivernm",driverName);
        driverDeliveryList.clear();
        imgList.clear();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
