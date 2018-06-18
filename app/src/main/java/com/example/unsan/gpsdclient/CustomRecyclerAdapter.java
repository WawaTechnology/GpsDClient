package com.example.unsan.gpsdclient;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.ULocale;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Unsan on 8/5/18.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    Context context;
    List<CustomerOrder> objects;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("HH a");
    Date todayDate = new Date();
    String thisDate ;
    String hour;
    List<String> hourList;
    String hh1;
    DatabaseReference carsRecordReference;

    DatabaseReference customerTodayReference;
    CustomRecyclerAdapter(Context context, List<CustomerOrder> objects)
    {
        this.context=context;
        this.objects=objects;
        thisDate= simpleDateFormat.format(todayDate);
        customerTodayReference = FirebaseDatabase.getInstance().getReference("CustomerTodayRecord");
        carsRecordReference=FirebaseDatabase.getInstance().getReference("carsRecord");
        hour=simpleDateFormat1.format(todayDate);
        hh1=hour.substring(3);


         hourList=new ArrayList<>();
        hourList.add("00 am");
        hourList.add("01 am");
        hourList.add("02 am");
        hourList.add("03 am");
        hourList.add("04 am");
        hourList.add("05 am");
    }
    @Override
    public CustomRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.simple_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerAdapter.ViewHolder holder, int position) {
        final CustomerOrder customer=objects.get(position);

        holder.tv.setText(customer.getCustomer());
        holder.engtv.setText(customer.getEngCustomerName());
        // holder.engtv.setText(customer.getRestEnglish());
        if(customer.isChecked())
        {
            holder.chkBox.setChecked(true);


        }
        else
            holder.chkBox.setChecked(false);
        if(customer.deliveryChecked)
        {
            holder.deliverytv.setVisibility(View.VISIBLE);
            holder.deliverytv.setText("Delivered");


        }
        else {
            holder.deliverytv.setVisibility(View.INVISIBLE);
        }

        Log.d("checkdeliveryorder",objects.get(position).order+"");
            holder.seqEditText.setText(objects.get(position).order+"");






        holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (compoundButton.isShown()) {
                    if(hh1.equals("am")) {
                        if(hourList.contains(hour))
                        {
                            thisDate=getYesterdayDateString();
                            Log.d("checkDate",thisDate);
                        }

                    }




                    if (compoundButton.isChecked()) {
                        customer.setChecked(true);

                        //TODO change next date

                        OrderStatus orderStatus=new OrderStatus(customer.engCustomerName,"Ordered",customer.getOrder());
                        customerTodayReference.child(thisDate).child(((AssignOrder) context).carNumber).child(customer.getCustomer()).setValue(orderStatus);







                        // customerTodayReference.child(thisDate).child(((AssignOrder) context).carNumber).child(customer.getCustomer()).setValue("Ordered");


                        //todo add carsRecord db adding extra key order
                      //  carsRecordReference.child(((AssignOrder) context).carNumber).child(customer.getOrder()+"").setValue(customer.getCustomer(),customer.engCustomerName);


                    }

                    else {
                        if(customer.deliveryChecked)
                        {
                            Toast.makeText(context,"Cannot cancel Delivered Order",Toast.LENGTH_LONG).show();
                            holder.chkBox.setChecked(true);

                        }
                        else {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context).setMessage("Are you sure ! You want to cancel the order?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    customerTodayReference.child(thisDate).child(((AssignOrder) context).carNumber).child(customer.getCustomer()).removeValue();
                                    customer.setChecked(false);

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    holder.chkBox.setChecked(true);
                                    customer.setChecked(true);


                                }
                            });
                            alertDialog.show();
                        }

                    }
                    ((AssignOrder) context).getDeliveryData();


                }
            }
        });


    }
    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(yesterday());
    }
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @Override
    public int getItemCount() {
        if(objects!=null)
            return objects.size();
        else
            return -1;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView engtv;
        TextView deliverytv;
        EditText seqEditText;
        AppCompatCheckBox chkBox;


        public ViewHolder(View itemView) {
            super(itemView);
             tv=(TextView)itemView.findViewById(R.id.custnamedisp);
             engtv=(TextView)itemView.findViewById(R.id.customereng);
             deliverytv=(TextView) itemView.findViewById(R.id.delivery_status);

         chkBox=(AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
         seqEditText=(EditText)itemView.findViewById(R.id.seqnumText);
            seqEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(charSequence.length()>0)

                    objects.get(getAdapterPosition()).setOrder(Integer.parseInt(seqEditText.getText().toString()));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }
    }
}
