package com.example.unsan.gpsdclient;

/**
 * Created by Unsan on 8/5/18.
 */

public class CustomerOrder {
    String customer;
    String engCustomerName;
    int order;
    boolean checked;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

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

    public CustomerOrder(String customer, String engCustomerName, boolean checked,boolean deliveryChecked,int order) {
        this.customer = customer;
        this.engCustomerName=engCustomerName;

        this.checked = checked;
        this.deliveryChecked=deliveryChecked;
        this.order=order;
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
