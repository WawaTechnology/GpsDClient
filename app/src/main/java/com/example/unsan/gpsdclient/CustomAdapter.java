package com.example.unsan.gpsdclient;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
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
 * Created by Unsan on 7/5/18.
 */

class CustomAdapter extends ArrayAdapter<CustomerEngChinese> {
    Context context;
    List<CustomerEngChinese> objects;


    @Override
    public int getCount() {
        if(objects!=null)
            return objects.size();
        else
            return -1;
    }

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<CustomerEngChinese> objects) {
        super(context, resource,0, objects);
        this.context=context;
        this.objects=objects;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listV=convertView;
        final int as=position;
        if(listV==null) {
            listV = LayoutInflater.from(context).inflate(R.layout.single_disp, parent, false);


        }
        final CustomerEngChinese customer=objects.get(position);

        TextView tv=(TextView)listV.findViewById(R.id.chinese_name);
        TextView engtv=(TextView)listV.findViewById(R.id.english_name);
        tv.setText(customer.getChinese());
        tv.setText(customer.getEnglish());





        return listV;

    }
    class ViewHolder
    {

    }
}

