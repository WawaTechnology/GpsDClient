package com.example.unsan.gpsdclient;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.ULocale;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Unsan on 8/5/18.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    Context context;
    List<CustomerOrder> objects;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
    Date todayDate = new Date();
    String thisDate ;
    DatabaseReference customerTodayReference;
    CustomRecyclerAdapter(Context context, List<CustomerOrder> objects)
    {
        this.context=context;
        this.objects=objects;
        thisDate= simpleDateFormat.format(todayDate);
        customerTodayReference = FirebaseDatabase.getInstance().getReference("CustomerTodayRecord");
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
        else
            holder.deliverytv.setVisibility(View.INVISIBLE);



       holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (compoundButton.isShown()) {
                    if (compoundButton.isChecked()) {
                        customer.setChecked(true);

                        customerTodayReference.child(thisDate).child(((AssignOrder) context).carNumber).child(customer.getCustomer()).setValue("Ordered");

                    } else {
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
                    ((AssignOrder) context).getDeliveryData();


                }
            }
        });


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
        AppCompatCheckBox chkBox;


        public ViewHolder(View itemView) {
            super(itemView);
             tv=(TextView)itemView.findViewById(R.id.custnamedisp);
             engtv=(TextView)itemView.findViewById(R.id.customereng);
             deliverytv=(TextView) itemView.findViewById(R.id.delivery_status);

         chkBox=(AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
