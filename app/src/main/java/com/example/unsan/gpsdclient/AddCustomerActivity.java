package com.example.unsan.gpsdclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Unsan on 18/4/18.
 */

public class AddCustomerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button submit;
    EditText restName,engrestname,addressText,cityText,zipText,contactPText,contactNumberText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference customerRef;
    String restaurant;
    Spinner carSpinner;
    ArrayAdapter arrayAdapter;
    String carNumber;
    DatabaseReference cardbref;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_customer);
        submit=(Button)findViewById(R.id.submit);
        restName=(EditText)findViewById(R.id.rest_name);
        addressText=(EditText)findViewById(R.id.address);
        engrestname=(EditText) findViewById(R.id.rest_engname);
        cityText=(EditText)findViewById(R.id.city);
        zipText=(EditText)findViewById(R.id.zip);
        contactPText=(EditText)findViewById(R.id.contact_person);
        contactNumberText=(EditText)findViewById(R.id.contact_number);
        carSpinner=(Spinner)findViewById(R.id.sp_car);

        arrayAdapter=new ArrayAdapter<String>(AddCustomerActivity.this,R.layout.spinner_item,getResources().getStringArray(R.array.carArray));
        carSpinner.setAdapter(arrayAdapter);
        carSpinner.setOnItemSelectedListener(AddCustomerActivity.this);


        firebaseDatabase=FirebaseDatabase.getInstance();
        customerRef=firebaseDatabase.getReference("Customer");
        cardbref=firebaseDatabase.getReference("carsRecord");
        contactNumberText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                long phoneNumber=Long.parseLong(contactNumberText.getText().toString());
                Customer customer=new Customer(addressText.getText().toString(),cityText.getText().toString(),phoneNumber,contactPText.getText().toString(),zipText.getText().toString());

                restaurant=restName.getText().toString();
                customerRef.child(restaurant).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        cardbref.child(carNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                               long count= dataSnapshot.getChildrenCount();
                               count=count+1;

                               cardbref.child(carNumber).child(count+"").setValue(new CustomerEngChinese(restaurant,engrestname.getText().toString()));
                                restName.setText("");
                                addressText.setText("");
                                cityText.setText("");
                                zipText.setText("");
                                contactPText.setText("");
                                contactNumberText.setText("");
                                engrestname.setText("");
                                Toast.makeText(AddCustomerActivity.this,"Customer record added",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });



            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       carNumber= adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
