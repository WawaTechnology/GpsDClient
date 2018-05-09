package com.example.unsan.gpsdclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Created by Unsan on 3/5/18.
 */


public class AssignCarCustomer extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    EditText chineseRestName,englishrestname;
    Spinner spinner;
    List<String> carNumbers;
    ArrayAdapter<String> arrayAdapter;
    String carNumber;
    Button submitButton;
    DatabaseReference carReference;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car_customer);
        chineseRestName=(EditText)findViewById(R.id.rest_name);
        englishrestname=(EditText)findViewById(R.id.rest_english);
        spinner=(Spinner)findViewById(R.id.car_spinner);
        submitButton=(Button) findViewById(R.id.submit);
        carNumbers=new ArrayList<>();
        carReference= FirebaseDatabase.getInstance().getReference("CarsDb");
        for(int i=1;i<=20;i++)
            carNumbers.add("Car "+i);
        arrayAdapter=new ArrayAdapter<String>(AssignCarCustomer.this,R.layout.support_simple_spinner_dropdown_item,carNumbers);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(AssignCarCustomer.this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carReference.child(carNumber).child("Restaurants").child(chineseRestName.getText().toString()).setValue(englishrestname.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        chineseRestName.setText("");
                        englishrestname.setText("");

                        Toast.makeText(AssignCarCustomer.this,"Restauant added!",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });




    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        carNumber=adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}
