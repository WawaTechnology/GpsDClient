package com.example.unsan.gpsdclient;

/**
 * Created by Unsan on 27/4/18.
 */

public class DriverDelivery {
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    String deliveryTime;
    String driverName;
    String vehicleNumber;


    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public DriverDelivery(String deliveryTime, String deliveryDate, long timeval, String customer, String destinationAddress, String carNumber, String driverName, String vehicleNumber) {
        this.deliveryTime = deliveryTime;
        this.deliveryDate = deliveryDate;

        this.timeval = timeval;
        this.customer = customer;
        this.destinationAddress = destinationAddress;

        this.carNumber = carNumber;

        this.driverName=driverName;
        this.vehicleNumber=vehicleNumber;
    }
    public DriverDelivery()
    {

    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }



    public long getTimeval() {
        return timeval;
    }

    public void setTimeval(long timeval) {
        this.timeval = timeval;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }



    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    String deliveryDate;

    long timeval;
    String customer;
    String destinationAddress;

    String carNumber;





}

