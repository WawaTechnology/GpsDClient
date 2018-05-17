package com.example.unsan.gpsdclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Unsan on 12/4/18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;
    List<DeliveryDriver> deliveryList;
    HistoryAdapter(Context context,List<DeliveryDriver> deliveryList)
    {
        this.context=context;
        this.deliveryList=deliveryList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DeliveryDriver delivery=deliveryList.get(position);

        holder.deliverdTo.setText(delivery.destinationAddress);
        holder.reachedTime.setText(delivery.deliveryTime);
        holder.customernm.setText( delivery.customer);
       // holder.gpsLocation.setText(delivery.gpsDestinationAddress);
        holder.dateText.setText(delivery.getDeliveryDate());
        holder.carNumber.setText(delivery.carNumber);
        holder.driverName.setText(delivery.driverName);
        Log.d("checkvnum",delivery.vehicleNumber);
        holder.vehicleNumber.setText(delivery.vehicleNumber);
        holder.driverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,DriverActivity.class);
                intent.putExtra("drivername",delivery.driverName);
                context.startActivity(intent);
            }
        });


        String photo= delivery.getPhoto();
        Glide.with(context)
                .load(photo)
                .into(holder.prodimage);


    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView carNumber,reachedTime,customernm,deliverdTo,dateText,driverName,vehicleNumber;
        ImageView prodimage;
        public ViewHolder(View itemView) {
            super(itemView);

            reachedTime=(itemView).findViewById(R.id.reachedtime);
            customernm=(itemView).findViewById(R.id.cust_name);
            deliverdTo=(itemView).findViewById(R.id.delivered);
            prodimage=(itemView).findViewById(R.id.prod_img);
           // gpsLocation=(itemView).findViewById(R.id.car_address);
            dateText=(itemView).findViewById(R.id.travel_date);
            carNumber=(itemView).findViewById(R.id.car_num);
            driverName=(itemView).findViewById(R.id.driver_name);
            vehicleNumber=(itemView).findViewById(R.id.v_num);

        }
    }


}
