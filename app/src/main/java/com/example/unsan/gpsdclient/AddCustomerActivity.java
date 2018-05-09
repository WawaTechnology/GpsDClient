package com.example.unsan.gpsdclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Unsan on 18/4/18.
 */

public class AddCustomerActivity extends AppCompatActivity {
    Button submit;
    EditText restName,addressText,cityText,zipText,contactPText,contactNumberText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference customerRef;
    String restaurant;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_customer);
        submit=(Button)findViewById(R.id.submit);
        restName=(EditText)findViewById(R.id.rest_name);
        addressText=(EditText)findViewById(R.id.address);
        cityText=(EditText)findViewById(R.id.city);
        zipText=(EditText)findViewById(R.id.zip);
        contactPText=(EditText)findViewById(R.id.contact_person);
        contactNumberText=(EditText)findViewById(R.id.contact_number);


        firebaseDatabase=FirebaseDatabase.getInstance();
        customerRef=firebaseDatabase.getReference("Customer");
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
                Customer customer=new Customer(addressText.getText().toString(),cityText.getText().toString(),phoneNumber,contactPText.getText().toString(),contactNumberText.getText().toString());

                restaurant=restName.getText().toString();
                customerRef.child(restaurant).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddCustomerActivity.this,"Customer record added",Toast.LENGTH_LONG).show();
                        restName.setText("");
                        addressText.setText("");
                        cityText.setText("");
                        zipText.setText("");
                        contactPText.setText("");
                        contactNumberText.setText("");

                    }
                });


            }
        });



    }
}
