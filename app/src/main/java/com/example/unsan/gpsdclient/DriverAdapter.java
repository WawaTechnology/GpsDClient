package com.example.unsan.gpsdclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Unsan on 27/4/18.
 */

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.ViewHolder> {


    Context context;
    List<DriverDelivery> deliveryList;
    List<String> imgList;
    DriverAdapter(Context context,List<DriverDelivery> deliveryList,List<String> imgList)
    {
        this.context=context;
        this.deliveryList=deliveryList;
        this.imgList=imgList;

    }


    @Override
    public DriverAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_view,parent,false);
        return new DriverAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DriverAdapter.ViewHolder holder, int position) {
        final DriverDelivery delivery=deliveryList.get(position);
        if(imgList.size()>position) {
            final String img = imgList.get(position);
            if(img!=null) {
                Glide.with(context)
                        .load(img)
                        .into(holder.prodimage);
            }
            else
                Glide.with(context).load(R.drawable.dff).into(holder.prodimage);
        }

        holder.deliverdTo.setText(delivery.destinationAddress);
        holder.reachedTime.setText(delivery.deliveryTime);
        holder.customernm.setText( delivery.customer);
        holder.gpsLocation.setText(delivery.gpsDestinationAddress);
        holder.dateText.setText(delivery.getDeliveryDate());
        holder.carNumber.setText(delivery.carNumber);
        holder.driverName.setText(delivery.driverName);
        holder.driverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,DriverActivity.class);
                intent.putExtra("drivername",delivery.driverName);
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView carNumber,reachedTime,customernm,deliverdTo,gpsLocation,dateText,driverName;
        ImageView prodimage;
        public ViewHolder(View itemView) {
            super(itemView);

            reachedTime=(itemView).findViewById(R.id.reachedtime);
            customernm=(itemView).findViewById(R.id.cust_name);
            deliverdTo=(itemView).findViewById(R.id.delivered);
            prodimage=(itemView).findViewById(R.id.prod_img);
            gpsLocation=(itemView).findViewById(R.id.car_address);
            dateText=(itemView).findViewById(R.id.travel_date);
            carNumber=(itemView).findViewById(R.id.car_num);
            driverName=(itemView).findViewById(R.id.driver_name);

        }
    }
}
