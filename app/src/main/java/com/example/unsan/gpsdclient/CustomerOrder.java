package com.example.unsan.gpsdclient;

/**
 * Created by Unsan on 8/5/18.
 */

public class CustomerOrder {
    String customer;
    String engCustomerName;
    boolean checked;
    boolean deliveryChecked;

    public boolean isDeliveryChecked() {
        return deliveryChecked;
    }

    public void setDeliveryChecked(boolean deliveryChecked) {
        this.deliveryChecked = deliveryChecked;
    }

    public String getEngCustomerName() {
        return engCustomerName;

    }

    public void setEngCustomerName(String engCustomerName) {
        this.engCustomerName = engCustomerName;
    }

    public CustomerOrder(String customer, String engCustomerName, boolean checked,boolean deliveryChecked) {
        this.customer = customer;
        this.engCustomerName=engCustomerName;

        this.checked = checked;
        this.deliveryChecked=deliveryChecked;
    }

    public String getCustomer() {
        return customer;

    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
