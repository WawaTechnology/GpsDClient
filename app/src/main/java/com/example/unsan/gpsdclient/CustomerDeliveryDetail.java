package com.example.unsan.gpsdclient;

/**
 * Created by Unsan on 18/5/18.
 */

public class CustomerDeliveryDetail {
    String customer;

    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }







    public CustomerDeliveryDetail(String customer,String time) {
        this.customer = customer;

        this.time=time;

    }

    public String getCustomer() {
        return customer;

    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }


}
